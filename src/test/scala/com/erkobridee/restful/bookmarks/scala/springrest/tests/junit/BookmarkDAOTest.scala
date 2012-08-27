package com.erkobridee.restful.bookmarks.scala.springrest.tests.junit

import java.util.List
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import com.erkobridee.restful.bookmarks.scala.springrest.dao.TraitBookmarkDAO
import com.erkobridee.restful.bookmarks.scala.springrest.entity.Bookmark
import junit.framework.Assert
import com.erkobridee.restful.bookmarks.scala.springrest.tests.Singleton.vo
import com.erkobridee.restful.bookmarks.scala.springrest.tests.Singleton.{vo_= => vo_=}
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(locations = Array("classpath:META-INF/spring/applicationContext.xml"))
class BookmarkDAOTest {
  
  // ---------------------------------------------------------------------------

  @Autowired
  val dao: TraitBookmarkDAO = null

  // ---------------------------------------------------------------------------  

  def insertTestData(): Unit = {
    var vo: Bookmark = null
    for (i <- 0 until 10) {
      vo = new Bookmark()
      vo.name = "fake name " + i
      vo.description = "fake description " + i
      vo.url = "http://fake.bookmark" + i + ".domain/"
      dao.save(vo)
    }
  }

  // ---------------------------------------------------------------------------

  @Test
  def testListAll(): Unit = {
    val list: List[Bookmark] = dao.listAll
    Assert.assertNotNull(list);

    val hasObjects: Boolean = list.size() > 0

    if (!hasObjects) {
      Assert.assertFalse(hasObjects)
      insertTestData
    }

  }

  @Test
  def testInsert(): Unit = {
    val time: Long = System.currentTimeMillis()
    
    vo = new Bookmark()
    vo.name = "Name Bookmark Test " + time
    vo.description = "Description Bookmark Test " + time
    vo.url = "http://test.bookmarksdomain.ext/" + time + "/"
    
    vo = dao.save(vo)
  }
  
  @Test
  def testFindByValidId(): Unit = {
    Assert.assertNotNull( dao.findById( vo.id ) )
  }
  
  @Test
  def testFindByInvalidId(): Unit = {
    Assert.assertNull( dao.findById( Long.box(-100) ) )
  }
  
  @Test
  def testFindByValidName(): Unit = {
    val list: List[Bookmark] = dao.findByName( vo.name )
    Assert.assertTrue(list.size() > 0)
  }
  
  @Test
  def testFindByInvalidName(): Unit = {
    val list: List[Bookmark] = dao.findByName("***" + vo.name + "***")
    Assert.assertFalse(list.size() > 0)
  }
  
  @Test
  def testUpdate(): Unit = {
    val nameUpdated: String = vo.name + "++"
    
    vo.name = nameUpdated
    vo.description = vo.description + "++"
    vo.url = vo.url + "/updated"
    
    vo = dao.save(vo)
    
    Assert.assertEquals(vo.name, nameUpdated)
    
  }
	
  @Test
  def testRemove(): Unit = {
    Assert.assertTrue( dao.remove( vo.id ) )
  }
  
  @Test
  def testCheckRemoved(): Unit = {
	vo = dao.findById( vo.id )
    Assert.assertNull( vo )
  }
  
}