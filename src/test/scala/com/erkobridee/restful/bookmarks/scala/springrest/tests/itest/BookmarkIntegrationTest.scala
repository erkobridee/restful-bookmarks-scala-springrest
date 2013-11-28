package com.erkobridee.restful.bookmarks.scala.springrest.tests.itest

import java.util.List
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.web.client.RestTemplate
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity.Bookmark
import com.erkobridee.restful.bookmarks.scala.springrest.tests.Singleton.vo
import com.erkobridee.restful.bookmarks.scala.springrest.tests.Singleton.vo_=
import junit.framework.Assert
import java.util.Map
import java.util.Collections
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity.BookmarkResultData


@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(locations = Array("classpath:spring/itest-context.xml"))
class BookmarkIntegrationTest {

  //----------------------------------------------------------------------------

  @Autowired
  val restTemplate: RestTemplate = null
  
  //----------------------------------------------------------------------------
  
  def getBaseUrl(
    port:String = "8080",
    app:String = "restful-bookmarks-scala-springrest",
    context:String = "rest",
    model:String = "bookmarks"
  ):String = {
    "http://localhost:" + port + "/" + app + "/" + context + "/" + model
  }
  
  //----------------------------------------------------------------------------
  // RESTful POST
  
  @Test
  def test_01_Post(): Unit = {
    vo = new Bookmark
    vo.name ="IT RESTFul"
    vo.description = "Insert : Integration Test RESTful"
    vo.url = "http://it.bookmarks.domain/"
	
    vo = restTemplate.postForObject( getBaseUrl(), vo, classOf[Bookmark] )
	
    Assert.assertNotNull( vo )	
  }
  
  //----------------------------------------------------------------------------
  // RESTful GET
  
  @Test
  def test_02_Get(): Unit = {
    val r: BookmarkResultData = restTemplate.getForObject( getBaseUrl(), classOf[BookmarkResultData] )
    
    Assert.assertTrue( r.getData.size > 0 )
  }

  //----------------------------------------------------------------------------
  // RESTful GET .../{id}
  
  def getById( id: Long ): Bookmark = {
    var bookmark: Bookmark = null
    
    val vars: Map[String, String] = Collections.singletonMap( "id", id + "" )
    
    try{
    	bookmark = restTemplate.getForObject( getBaseUrl()+"/{id}", classOf[Bookmark], vars )
    } catch {
      case e: Exception => {}
    }
    
    // return
    bookmark
  }
  
  @Test
  def test_03_GetByInvalidId(): Unit = {
    Assert.assertNull( getById( -1 ) ) 
  }

  @Test
  def test_03_GetById(): Unit = {
    vo = getById( vo.id )
    Assert.assertNotNull( vo )
  }
  
  //----------------------------------------------------------------------------
  // RESTful GET .../search/{name}
  
  def getByName( name: String ): BookmarkResultData = {
    val vars: Map[String, String] = Collections.singletonMap( "name", name + "" )
    
    restTemplate.getForObject( getBaseUrl()+"/search/{name}", classOf[BookmarkResultData], vars )
  }
  
  @Test
  def test_04_GetByInvalidName(): Unit = {
    val r: BookmarkResultData = getByName( "IT RESTFul Invalid Name" )
    
    Assert.assertFalse( r.getData.size > 0 )
  }

  @Test
  def test_04_GetByName(): Unit = {
    val r: BookmarkResultData = getByName( vo.getName() )
    
    Assert.assertTrue( r.getData.size > 0 )
  }  
  
  //----------------------------------------------------------------------------
  // RESTful PUT .../{id}
  
  @Test
  def test_05_Update(): Unit = {
    val nameUpdated: String = vo.name + " ... updated"

    vo.name =  nameUpdated
    vo.description = vo.description + " ... updated"
    vo.url = vo.url + "/updated"

    val vars: Map[String, String] = Collections.singletonMap( "id", vo.id + "" )
    restTemplate.put( getBaseUrl() + "/{id}", vo, vars )
	
    vo = getById( vo.id )
	
    Assert.assertEquals( nameUpdated, vo.name )
  }
  
  //----------------------------------------------------------------------------
  // RESTful DELETE .../{id}
  
  @Test
  def testDelete(): Unit = {
    val vars: Map[String, String] = Collections.singletonMap( "id", vo.id + "" )
    restTemplate.delete( getBaseUrl() + "/{id}", vars )
	
    vo = getById( vo.id )
	
    Assert.assertNull( vo )
  }
  
  //----------------------------------------------------------------------------
  
}