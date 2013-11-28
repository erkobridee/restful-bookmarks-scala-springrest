package com.erkobridee.restful.bookmarks.scala.springrest.dao.hibernate

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
import org.hibernate.criterion.Criterion
import org.hibernate.Criteria
import java.lang.Integer
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity.BookmarkResultData
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity.BookmarkResultData
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity.BookmarkResultData
import java.lang.Integer
import org.hibernate.criterion.Projections

@Repository("bookmarkDAO")
class BookmarkDAO extends HibernateDaoSupport with TraitBookmarkDAO {

  //----------------------------------------------------------------------------
  
  val log : Logger = LoggerFactory.getLogger( classOf[BookmarkDAO] )
  
  //----------------------------------------------------------------------------
  
  @Autowired
  def init( sessionFactory: SessionFactory ): Unit = {
  
    log.debug( "setSessionFactory" )
    
    super.setSessionFactory( sessionFactory )
    
    generateInitData
  }
  
  //----------------------------------------------------------------------------
  
  def generateInitData(): Unit = {
    
    val bookmarksCount: Int = this.count
    
    if( bookmarksCount == 0 ) {
      
      log.debug("generateInitData")
      
      var vo: Bookmark = null
      
      vo = new Bookmark
      vo.name = "twitter"
      vo.description = "@ErkoBridee"
      vo.url = "https://twitter.com/ErkoBridee"
      save( vo )
      
      vo = new Bookmark
      vo.name = "github"
      vo.description = "github/erkobridee"
      vo.url = "https://github.com/erkobridee"
      save( vo )
      
      vo = new Bookmark
      vo.name = "Site"
      vo.description = "Site Erko Bridee"
      vo.url = "http://about.erkobridee.com/"
      save( vo )

      vo = new Bookmark
      vo.name = "delicious"
      vo.description = "delicious/erko.bridee"
      vo.url = "http://www.delicious.com/erko.bridee"
      save( vo )
      
    }
      
  }
  
  //----------------------------------------------------------------------------
  
  def count(): Int = {
    
    this.count(null)
    
  }
  
  def count( criterion: Criterion ): Int = {
	
    var c: Criteria = getSession.createCriteria( classOf[Bookmark] )
	
    c.setProjection( Projections.rowCount )
	
	if( criterion != null ) {
		
	  c.add(criterion)
	  
	}
	
	c.list.get( 0 ).asInstanceOf[Int]
	
  }
    
    
  //----------------------------------------------------------------------------
  
  @Transactional(readOnly = true)
  def list(): BookmarkResultData = 
  	this.list( 0, 0 )

  	
  @Transactional(readOnly = true)
  def list( page: Int, size: Int = 0 ): BookmarkResultData = {
    
    log.debug( "list [ page: " + page + " | size: " + size + " ]" )
    
    var c: Criteria = getSession.createCriteria( classOf[Bookmark] )
    
    if( size == 0 ) {
      
      c.setMaxResults( 10 )
    	
	} else {
	  
	  c.setMaxResults( size )
		
	}
    
    if( page > 1 ) {
    
      c.setFirstResult( ( page-1 ) * size )
      
	} else {
	  
	  c.setFirstResult( 0 )
	  
	}
    
    new BookmarkResultData(
        c.list.asInstanceOf[List[Bookmark]],
        this.count,
        page, size
    )
  }
  
  //----------------------------------------------------------------------------
  
  @Transactional(readOnly = true)
  def findById( id: Long ): Bookmark = {
    
	log.debug( "findById: " + id )
    
	getSession
      .get(classOf[Bookmark], Long.box( id ) )
      .asInstanceOf[Bookmark]
  }
  
  //----------------------------------------------------------------------------
  
  @Transactional(readOnly = true)
  def findByName( name: String ): BookmarkResultData = 
    this.findByName( name, 0, 0 )

    
  @Transactional(readOnly = true)
  def findByName( name : String, page: Int, size: Int ): BookmarkResultData = {
    
    log.debug( "findByName: " + name + " [ page: " + page + " | size: " + size + " ]" )
    
    val criterion: Criterion = Restrictions.like( "name", "%"+ name + "%" )
    
    var c: Criteria = getSession.createCriteria( classOf[Bookmark] )
    
    c.add( criterion )
    
    if( size == 0 ) {
    
      c.setMaxResults( 10 )
      
	} else {
	  
	  c.setMaxResults( size )
	  
	}
    
    if( page > 1 ) {
      
      c.setFirstResult( ( page-1 ) * size )
      
	} else {
	  
	  c.setFirstResult( 0 )
	  
	}
    
    new BookmarkResultData(
        c.list.asInstanceOf[List[Bookmark]],
        this.count( criterion ),
        page, size
    )
  }
  
  //----------------------------------------------------------------------------
  
  @Transactional
  def save( value: Bookmark ): Bookmark = {
    
    log.debug( "save" )
    
    getSession
      .merge( value )
      .asInstanceOf[Bookmark]
  }

  @Transactional
  def remove( id: Long ): Boolean = {
    
    log.debug( "remove: " + id )
    
    var flag: Boolean = false
    
    var bookmark: Bookmark = findById( id ) 
    
    if( bookmark != null ) {
    
	    try {
	      
	      getSession.delete( bookmark )
	      flag = true
	      
	    } catch {
	      case e : DataAccessException => {
	        log.error( "DataAccessException", e )       
	      }
	    }
	    
    }
     
    flag
  }

}
