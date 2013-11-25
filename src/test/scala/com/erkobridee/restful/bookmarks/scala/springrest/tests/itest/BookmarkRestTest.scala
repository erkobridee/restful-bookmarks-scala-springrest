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


@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(locations = Array("classpath:spring/itest-context.xml"))
class BookmarkRestTest {

  // ---------------------------------------------------------------------------

  @Autowired
  val restTemplate: RestTemplate = null
  
  // ---------------------------------------------------------------------------
  
  def getBaseUrl(
    port:String = "8080",
    app:String = "restful-bookmarks-scala-springrest",
    model:String = "bookmarks"
  ):String = {
    "http://localhost:" + port + "/" + app + "/rest/" + model
  }
  
  // ---------------------------------------------------------------------------
  
  @Test
  def testInsert(): Unit = {
    vo = new Bookmark()
    vo.name ="IT RESTFul"
    vo.description = "Insert : Integration Test RESTful"
    vo.url = "http://it.bookmarks.domain/"
	
    vo = restTemplate.postForObject(getBaseUrl(), vo, classOf[Bookmark])
	
    Assert.assertNotNull(vo)	
  }
  
  @Test
  def testListAll(): Unit = {
    val list: List[Bookmark] = restTemplate.getForObject(getBaseUrl(), classOf[List[Bookmark]])
    Assert.assertTrue(list.size() > 0)
  }

  //--- get by id
  def getById(id: Long): Bookmark = {
    val vars: Map[String, String] = Collections.singletonMap("id", id + "")
    restTemplate.getForObject(getBaseUrl()+"/{id}", classOf[Bookmark], vars)    
  }
  
  @Test
  def testGetByInvalidId(): Unit = {
    Assert.assertNull( getById( -1 ) ) 
  }

  @Test
  def testGetById(): Unit = {
    vo = getById(vo.id)
    Assert.assertNotNull(vo)
  }
  
  //--- get by name
  def getByName(name: String): List[Bookmark] = {
    val vars: Map[String, String] = Collections.singletonMap("name", name + "")
    restTemplate.getForObject(getBaseUrl()+"/search/{name}", classOf[List[Bookmark]], vars)
  }
  
  @Test
  def testGetByInvalidName(): Unit = {
    val list: List[Bookmark] = getByName( "IT RESTFul Invalid Name" )
    Assert.assertFalse(list.size() > 0)
  }

  @Test
  def testGetByName(): Unit = {
    val list: List[Bookmark] = getByName(vo.getName())	
    Assert.assertTrue(list.size() > 0)
  }  
  
  //---
  
  @Test
  def testUpdate(): Unit = {
    val nameUpdated: String = vo.name + " ... updated"

    vo.name =  nameUpdated
    vo.description = vo.description + " ... updated"
    vo.url = vo.url + "/updated"

    val vars: Map[String, String] = Collections.singletonMap("id", vo.id + "")
    restTemplate.put(getBaseUrl() + "/{id}", vo, vars)
	
    vo = getById(vo.id)
	
    Assert.assertEquals(nameUpdated, vo.name)
  }  
  
  @Test
  def testDelete(): Unit = {
    val vars: Map[String, String] = Collections.singletonMap("id", vo.id + "");
    restTemplate.delete(getBaseUrl() + "/{id}", vars)
	
    vo = getById(vo.id)
	
    Assert.assertNull(vo)
  }  
  
}