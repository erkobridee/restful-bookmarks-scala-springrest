package com.erkobridee.restful.bookmarks.scala.springrest.tests.junit

import java.util.List
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.dao.TraitBookmarkDAO
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity.Bookmark
import junit.framework.Assert
import com.erkobridee.restful.bookmarks.scala.springrest.tests.Singleton.vo
import com.erkobridee.restful.bookmarks.scala.springrest.tests.Singleton.{vo_= => vo_=}
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity.BookmarkResultData
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity.BookmarkResultData

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
  def testCount(): Unit = {
    Assert.assertTrue(dao.count > 0)
  }
  
  @Test
  def testListOffsetLimit(): Unit = {
    val r: BookmarkResultData = dao.list( 0, 3 )
    
    Assert.assertNotNull( r )
    
    Assert.assertNotNull( r.getData )
    
    Assert.assertTrue(r.getData.size == 3)
  }
  
  @Test
  def testListAll(): Unit = {
    val r: BookmarkResultData = dao.list
    
    Assert.assertNotNull( r )
    
    Assert.assertNotNull( r.getData )

    val hasObjects: Boolean = r.getData.size > 0

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
    
    vo = dao.save( vo )
    
    Assert.assertNotNull( vo.getId )
  }
  
  @Test
  def testFindByValidId(): Unit = {
    Assert.assertNotNull( dao.findById( vo.id ) )
  }
  
  @Test
  def testFindByInvalidId(): Unit = {
    Assert.assertNull( dao.findById( -100 ) )
  }
  
  @Test
  def testFindByValidName(): Unit = {
    val r: BookmarkResultData = dao.findByName( vo.name )
    Assert.assertTrue( r.getData.size > 0 )
  }
  
  @Test
  def testFindByInvalidName(): Unit = {
    val r: BookmarkResultData = dao.findByName("***" + vo.name + "***")
    Assert.assertFalse( r.getData.size > 0 )
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