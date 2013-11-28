package com.erkobridee.restful.bookmarks.scala.springrest.rest.controller

import java.net.URI
import java.net.URISyntaxException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestMethod._
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.dao.TraitBookmarkDAO
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity.BookmarkResultData
import javax.xml.bind.annotation.XmlRootElement
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import java.util.TreeSet
import java.util.Set
import org.springframework.http.HttpStatus
import com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity.Bookmark
import com.erkobridee.restful.bookmarks.scala.springrest.rest.resource.ResultMessage
import org.springframework.web.bind.annotation.RequestBody


@Controller 
@RequestMapping(value = Array("/bookmarks"))
class BookmarkRESTController {

  val log: Logger = LoggerFactory.getLogger( classOf[BookmarkRESTController] )
  
  //----------------------------------------------------------------------------
  
  @Autowired
  val dao: TraitBookmarkDAO = null
  
  //----------------------------------------------------------------------------
  
  def getLocation(): URI = 
    this.getLocation( "" )
  
  def getLocation( id: Long ): URI =
    this.getLocation( "" + id )
    
  def getSearchLocation(): URI = 
    this.getLocation( "search/:find" )

  def getLocation( add: String ): URI = {
    var uri: URI = null
    
    try {
      var sURI: String = "/bookmarks"
      if( add != null && !"".equals( add ) ) sURI += "/" + add
      uri = new URI( sURI )
    } catch {
      case e : URISyntaxException => {
        log.error( "Location URI Exception", e )
      }
    }
    
    // return
    uri
  }

  //----------------------------------------------------------------------------
  
  @RequestMapping(
    value = Array("/search/{find}"), 
    method = Array(GET)
  )
  @ResponseBody
  def search(
    
	  @PathVariable find: String,
	  @RequestParam(value="page", required=false, defaultValue="1") page: Int,
	  @RequestParam(value="size", required=false, defaultValue="10") size: Int
    
  ): ResponseEntity[BookmarkResultData] = {
    
    log.debug( "search: " + find + " [ page: " + page + " | size: " + size + " ]" )
    
    val r: BookmarkResultData = dao.findByName( find, page, size )
    
    var responseHeader: HttpHeaders = new HttpHeaders
		
	var allowedMethods: Set[HttpMethod] = new TreeSet[HttpMethod]
	
	allowedMethods.add( HttpMethod.GET )
	
	responseHeader.setAllow( allowedMethods )
	responseHeader.setLocation( this.getSearchLocation )
	
	// return
	new ResponseEntity[BookmarkResultData]( r, responseHeader, HttpStatus.OK )	
  }
  
  
  @RequestMapping(method = Array(GET))
  @ResponseBody
  def list(
    
	  @RequestParam(value="page", required=false, defaultValue="1") page: Int,
	  @RequestParam(value="size", required=false, defaultValue="10") size: Int
      
  ): ResponseEntity[BookmarkResultData] = {
    
    log.debug( "list [ page: " + page + " | size: " + size + " ]" )
    
    val r: BookmarkResultData = dao.list( page, size )
    
    var responseHeader: HttpHeaders = new HttpHeaders
		
	var allowedMethods: Set[HttpMethod] = new TreeSet[HttpMethod]
	
	allowedMethods.add( HttpMethod.GET )
	allowedMethods.add( HttpMethod.POST )
	
	responseHeader.setAllow( allowedMethods )
	responseHeader.setLocation( this.getLocation )
	
	new ResponseEntity[BookmarkResultData]( r, responseHeader, HttpStatus.OK )    
  }
  
  
  @RequestMapping(
    value = Array("/{id}"), 
    method = Array(GET)
  )
  @ResponseBody
  def get(
	  
      @PathVariable id: Long
      
  ): ResponseEntity[_] = {
    
    log.debug( "get: " + id )
    
    var bookmark: Bookmark = dao.findById( id )
    
    if(bookmark != null) {
      
    	var responseHeader: HttpHeaders = new HttpHeaders
		
		var allowedMethods: Set[HttpMethod] = new TreeSet[HttpMethod]
		
		allowedMethods.add( HttpMethod.PUT )
		allowedMethods.add( HttpMethod.DELETE )
		
		responseHeader.setAllow( allowedMethods )
		responseHeader.setLocation( this.getLocation( bookmark.getId ) )
		
		// return
		new ResponseEntity[Bookmark]( bookmark, responseHeader, HttpStatus.OK )  
      
    } else {
      
    	val resultMessage: ResultMessage = new ResultMessage( 404, "id: " + id + " not found." )
    	
    	// return
		new ResponseEntity[ResultMessage](
		    resultMessage, HttpStatus.NOT_FOUND		    
		)
		
    }
  }
  
  
  @RequestMapping(method = Array(POST))
  @ResponseBody
  def create(
      
      @RequestBody value: Bookmark
      
  ): ResponseEntity[Bookmark] = {
    
    log.debug( "create" )
    
    val bookmark: Bookmark = dao.save( value )
    
    var responseHeader: HttpHeaders = new HttpHeaders
		
	var allowedMethods: Set[HttpMethod] = new TreeSet[HttpMethod]
	
	allowedMethods.add( HttpMethod.GET )
	allowedMethods.add( HttpMethod.PUT )
	allowedMethods.add( HttpMethod.DELETE )
	
	responseHeader.setAllow( allowedMethods )
	responseHeader.setLocation( this.getLocation( bookmark.getId ) )
	
	// return
	new ResponseEntity[Bookmark]( bookmark, responseHeader, HttpStatus.CREATED ) 
  }
  
  
  @RequestMapping(
    value = Array("/{id}"), 
    method = Array(PUT)
  )
  @ResponseBody
  def update(
      
      @RequestBody value: Bookmark
      
  ): ResponseEntity[Bookmark] = {
    
    log.debug( "update" )
    
    val bookmark: Bookmark = dao.save( value )
    
    var responseHeader: HttpHeaders = new HttpHeaders
		
	var allowedMethods: Set[HttpMethod] = new TreeSet[HttpMethod]
	
	allowedMethods.add( HttpMethod.GET )
	allowedMethods.add( HttpMethod.PUT )
	allowedMethods.add( HttpMethod.DELETE )
	
	responseHeader.setAllow( allowedMethods )
	responseHeader.setLocation( this.getLocation( bookmark.getId ) )
	
	// return
	new ResponseEntity[Bookmark]( bookmark, responseHeader, HttpStatus.CREATED ) 
  }
  
  
  @RequestMapping(
    value = Array("/{id}"), 
    method = Array(DELETE)
  )
  @ResponseBody
  def remove(
	  
      @PathVariable id: Long
	  
  ):ResponseEntity[ResultMessage] = {
    
    val flag = dao.remove( id )
    
    log.debug( "remove: " + id + " | status: " + flag )
    
    var message: ResultMessage = null
    var response: ResponseEntity[ResultMessage] = null
    
	if( flag ) {
	  
		message = new ResultMessage( 202, "id: " + id + " removed." )
		response = new ResponseEntity[ResultMessage]( message, HttpStatus.ACCEPTED )
		
	} else {
	  
		message = new ResultMessage(404, "id: " + id + " not found.");
		response = new ResponseEntity[ResultMessage]( message, HttpStatus.NOT_FOUND )
		
	}    
    
    // return
    response
  }
  
  
}