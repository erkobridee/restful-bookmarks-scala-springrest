package com.erkobridee.restful.bookmarks.scala.springrest.dao.impl

import java.util.List
import scala.annotation.serializable
import org.hibernate.SessionFactory
import org.hibernate.criterion.Restrictions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.dao.TraitBookmarkDAO
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity.Bookmark
import javax.persistence.Entity
import javax.persistence.Table
import javax.xml.bind.annotation.XmlRootElement
import org.springframework.orm.hibernate3.support.HibernateDaoSupport
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.dao.TraitBookmarkDAO

@Repository("bookmarkDAO")
class BookmarkDAO extends HibernateDaoSupport with TraitBookmarkDAO {

  // --------------------------------------------------------------------------
  
  val log : Logger = LoggerFactory.getLogger(classOf[Bookmark])
  
  // --------------------------------------------------------------------------
  
  @Autowired
  def init(sessionFactory: SessionFactory): Unit = {
  
    log.debug("setSessionFactory")
    
    super.setSessionFactory( sessionFactory )
    
    generateInitData
  }
  
  // --------------------------------------------------------------------------
  
  def generateInitData(): Unit = {
    val list: List[Bookmark] = listAll
    
    if(list.size() == 0) {
      log.debug("generateInitData")
      
      var vo: Bookmark = null
      
      vo = new Bookmark()
      vo.name = "twitter"
      vo.description = "@ErkoBridee"
      vo.url = "https://twitter.com/ErkoBridee"
      save(vo)
      
      vo = new Bookmark()
      vo.name = "github"
      vo.description = "github/erkobridee"
      vo.url = "https://github.com/erkobridee"
      save(vo)
      
      vo = new Bookmark()
      vo.name = "Site"
      vo.description = "Site Erko Bridee"
      vo.url = "http://about.erkobridee.com/"
      save(vo)

      vo = new Bookmark()
      vo.name = "delicious"
      vo.description = "delicious/erko.bridee"
      vo.url = "http://www.delicious.com/erko.bridee"
      save(vo)
      
    }
      
  }
  
  // --------------------------------------------------------------------------
  
  @Transactional(readOnly = true)
  def listAll(): List[Bookmark] = 
    getSession
      .createCriteria(classOf[Bookmark])
      .list()
      .asInstanceOf[List[Bookmark]]

  @Transactional(readOnly = true)
  def findById(id: Long): Bookmark = 
    getSession
      .get(classOf[Bookmark], Long.box(id))
      .asInstanceOf[Bookmark]

  @Transactional(readOnly = true)
  def findByName(name: String): List[Bookmark] = 
    getSession
      .createCriteria(classOf[Bookmark])
      .add(Restrictions.like("name", "%"+ name + "%" ))
      .list()
      .asInstanceOf[List[Bookmark]]

  @Transactional
  def save(value: Bookmark): Bookmark = 
    getSession
      .merge(value)
      .asInstanceOf[Bookmark]

  @Transactional
  def remove(id: Long): Boolean = {
    var flag : Boolean = true
    try {
      getSession.delete(findById(id))
    } catch {
      case e : DataAccessException => {
        flag = false
        log.error("DataAccessException", e)       
      }
    }
     
    flag
  }

}