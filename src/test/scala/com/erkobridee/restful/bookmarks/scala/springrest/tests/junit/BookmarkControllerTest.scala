package com.erkobridee.restful.bookmarks.scala.springrest.tests.junit

import java.util.List

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.erkobridee.restful.bookmarks.scala.springrest.controller.BookmarkController
import com.erkobridee.restful.bookmarks.scala.springrest.entity.Bookmark
import com.erkobridee.restful.bookmarks.scala.springrest.tests.Singleton.vo
import com.erkobridee.restful.bookmarks.scala.springrest.tests.Singleton.{vo_= => vo_=}

import junit.framework.Assert

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(locations = Array("classpath:META-INF/spring/applicationContext.xml"))
class BookmarkControllerTest {

  // ---------------------------------------------------------------------------
  
  @Autowired
  val controller: BookmarkController = null
  
  // ---------------------------------------------------------------------------
  
  // RESTful POST
  @Test
  def testInsert(): Unit = {
    vo = new Bookmark()    
    vo.name = "BookmarkServiceTest Name"
    vo.description = "BookmarkServiceTest Description"
    vo.url = "http://service.bookmarkdomain.test/" + System.currentTimeMillis() + "/"
    vo = controller.insert(vo)

    Assert.assertNotNull(vo.id)    
  }
  
  // RESTful GET .../{id}
  @Test
  def testGetById(): Unit = {
    Assert.assertNotNull( controller.getById( vo.id ) )
  }

  // RESTful GET .../search/{name}
  @Test
  def testGetByName(): Unit = {
    val list: List[Bookmark] = controller.getByName( vo.name )
    Assert.assertTrue(list.size() > 0)
  }
  
  // RESTful PUT .../{id}
  @Test
  def testUpdate(): Unit = {
    val nameUpdated = vo.name + "++"
    vo.name = nameUpdated
    vo.description = vo.description + "++"
    vo.url = vo.url + "/updated"
    
    vo = controller.update(vo)
    
    Assert.assertEquals(vo.name, nameUpdated)
  }
  
  // RESTful GET
  @Test
  def testGetAll(): Unit = {
    val list: List[Bookmark] = controller.getAll
    Assert.assertTrue(list.size() > 0)
  }
  
  // RESTful DELETE
  @Test
  def testDelete(): Unit = {
    val id: Long = vo.id
    
    controller.remove(id)
    
    vo = controller.getById(id)
    
    Assert.assertNull(vo)
  }

}