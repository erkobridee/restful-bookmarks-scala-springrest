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
  def testInsert(): Unit = {
    vo = new Bookmark    
    vo.name = "BookmarkServiceTest Name"
    vo.description = "BookmarkServiceTest Description"
    vo.url = "http://service.bookmarkdomain.test/" + System.currentTimeMillis + "/"
    vo = rest.create( vo ).getBody

    Assert.assertNotNull( vo.id ) 
  }
  
  // RESTful GET .../{id}
  def getById( id: Long ): ResponseEntity[_] =
    rest.get( id )
  
  
  @Test
  def testGetById(): Unit = {
    val response: ResponseEntity[_] = this.getById( vo.getId )
    
    Assert.assertTrue( response.getBody.isInstanceOf[Bookmark] )
  }
  
  @Test
  def testGetByInvalidId(): Unit = {
    val response: ResponseEntity[_] = this.getById( -1 )
    
    Assert.assertTrue( response.getBody.isInstanceOf[ResultMessage] )
    
    val resultMessage: ResultMessage = response.getBody.asInstanceOf[ResultMessage]
    
    Assert.assertEquals( 404, resultMessage.code )
  }
  
  
  // RESTful GET .../search/{name}
  def getByName( name: String ): BookmarkResultData =
    rest.search( name, 1, 10 ).getBody
  
  @Test
  def testGetByName(): Unit = {
    val r: BookmarkResultData = this.getByName( vo.getName )
    
    Assert.assertTrue( r.getData.size > 0 )
  }
  
  @Test
  def getByInvalidName(): Unit = {
    val r: BookmarkResultData = getByName( "IT RESTFul Invalid Name" )
    
    Assert.assertFalse( r.getData.size > 0 )
  } 
  
  
  // RESTful PUT .../{id}
  @Test
  def testUpdate(): Unit = {
    val nameUpdated: String = vo.name + "++"
    
    vo.name = nameUpdated
    vo.description = vo.description + "++"
    vo.url = vo.url + System.currentTimeMillis
    
    vo = rest.update( vo ).getBody
    
    Assert.assertEquals( nameUpdated, vo.name )
  }
  
  // RESTful GET
  @Test
  def getList(): Unit = {
    val r: BookmarkResultData = rest.getList( 1, 10 ).getBody
    
    Assert.assertTrue( r.getData.size > 0 )
  }
  
  // RESTful DELETE
  @Test
  def testDelete(): Unit = {
    val id: Long = vo.id
    
    var message: ResultMessage = rest.remove( id ).getBody

    Assert.assertEquals( 202, message.code )
    
    val response: ResponseEntity[_] = rest.get( id )
    
    Assert.assertTrue( response.getBody.isInstanceOf[ResultMessage] )
    
    message = response.getBody.asInstanceOf[ResultMessage]
    
    Assert.assertEquals( 404, message.code )    
  }
  
}