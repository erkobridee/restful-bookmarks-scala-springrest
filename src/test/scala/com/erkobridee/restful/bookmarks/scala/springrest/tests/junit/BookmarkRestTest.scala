package com.erkobridee.restful.bookmarks.scala.springrest.tests.junit

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.junit.Test
import com.erkobridee.restful.bookmarks.scala.springrest.rest.controller.BookmarkRESTController
import com.erkobridee.restful.bookmarks.scala.springrest.tests.Singleton.vo
import com.erkobridee.restful.bookmarks.scala.springrest.tests.Singleton.{vo_= => vo_=}
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity.Bookmark
import org.junit.Assert
import org.springframework.http.ResponseEntity
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity.BookmarkResultData
import com.erkobridee.restful.bookmarks.scala.springrest.rest.resource.ResultMessage



@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(locations = Array("classpath:META-INF/spring/applicationContext.xml"))
class BookmarkRestTest {

  //----------------------------------------------------------------------------
  
  @Autowired
  val rest: BookmarkRESTController = null
  
  //----------------------------------------------------------------------------
  // RESTful POST
  
  @Test
  def test_01_Create(): Unit = {
    vo = new Bookmark    
    vo.name = "BookmarkServiceTest Name"
    vo.description = "BookmarkServiceTest Description"
    vo.url = "http://service.bookmarkdomain.test/" + System.currentTimeMillis + "/"
    
    vo = rest.create( vo ).getBody

    Assert.assertNotNull( vo.id ) 
  }
  
  //----------------------------------------------------------------------------
  // RESTful GET
  
  @Test
  def test_02_List(): Unit = {
    val r: BookmarkResultData = rest.list( 1, 10 ).getBody
    
    Assert.assertTrue( r.getData.size > 0 )
  }
  
  //---------------------------------------------------------------------------- 
  // RESTful GET .../{id}
  
  def getById( id: Long ): ResponseEntity[_] =
    rest.get( id )
  
  
  @Test
  def test_03_GetById(): Unit = {
    val response: ResponseEntity[_] = this.getById( vo.getId )
    
    Assert.assertTrue( response.getBody.isInstanceOf[Bookmark] )
  }
  
  @Test
  def test_03_GetByInvalidId(): Unit = {
    val response: ResponseEntity[_] = this.getById( -1 )
    
    Assert.assertTrue( response.getBody.isInstanceOf[ResultMessage] )
    
    val resultMessage: ResultMessage = response.getBody.asInstanceOf[ResultMessage]
    
    Assert.assertEquals( 404, resultMessage.code )
  }
  
  //----------------------------------------------------------------------------
  // RESTful GET .../search/{name}
  
  def getByName( name: String ): BookmarkResultData =
    rest.search( name, 1, 10 ).getBody
  
  @Test
  def test_04_GetByName(): Unit = {
    val r: BookmarkResultData = this.getByName( vo.getName )
    
    Assert.assertTrue( r.getData.size > 0 )
  }
  
  @Test
  def test_04_GetByInvalidName(): Unit = {
    val r: BookmarkResultData = getByName( "IT RESTFul Invalid Name" )
    
    Assert.assertFalse( r.getData.size > 0 )
  } 
  
  //----------------------------------------------------------------------------
  // RESTful PUT .../{id}
  
  @Test
  def test_05_Update(): Unit = {
    val nameUpdated: String = vo.name + "++"
    
    vo.name = nameUpdated
    vo.description = vo.description + "++"
    vo.url = vo.url + System.currentTimeMillis
    
    vo = rest.update( vo ).getBody
    
    Assert.assertEquals( nameUpdated, vo.name )
  }
  
  //----------------------------------------------------------------------------
  // RESTful DELETE
  
  def deleteById( id: Long ): ResultMessage =
    rest.remove( id ).getBody
  
  @Test
  def test_06_DeleteByInvalidId(): Unit = {
    val message: ResultMessage = this.deleteById( -1 )
		
	Assert.assertEquals( 404, message.code )
  }
  
  @Test
  def test_06_DeleteById(): Unit = {
    val message: ResultMessage = this.deleteById( vo.id )
		
	Assert.assertEquals( 202, message.code )
  }
  
  //----------------------------------------------------------------------------
  
}