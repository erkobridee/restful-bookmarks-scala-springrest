package com.erkobridee.restful.bookmarks.scala.springrest.controller

import java.util.List
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.erkobridee.restful.bookmarks.scala.springrest.dao.TraitBookmarkDAO
import com.erkobridee.restful.bookmarks.scala.springrest.entity.Bookmark
import org.springframework.web.bind.annotation.RequestMethod._

@Controller 
@RequestMapping(value = Array("/bookmarks"))
class BookmarkController {

  import collection.JavaConversions._
  
  // --------------------------------------------------------------------------
  
  val log : Logger = LoggerFactory.getLogger(classOf[Bookmark])
  
  // --------------------------------------------------------------------------
  
  @Autowired
  val dao : TraitBookmarkDAO = null
  
  // --------------------------------------------------------------------------
  @RequestMapping(value = Array("/test"))
  @ResponseBody
  def test(): String = {
    "ok"
  }
  
  @RequestMapping(method = Array(GET))
  @ResponseBody
  def getAll(): List[Bookmark] = {
    log.debug("getAll")
    dao.listAll
  }
  
  
  @RequestMapping(
    value = Array("/{id}"), 
    method = Array(GET)
  )
  @ResponseBody
  def getById(
    @PathVariable id: Long
  ): Bookmark = {
    log.debug("getById: " + id)
    dao.findById(id).asInstanceOf[Bookmark]
  }
  
  
  @RequestMapping(
    value = Array("/search/{name}"), 
    method = Array(GET)
  )
  @ResponseBody
  def getByName(
    @PathVariable name: String
  ): List[Bookmark] = {
    log.debug("getByName: " + name)
	dao.findByName(name)
  }
  
  
  @RequestMapping(method = Array(POST))
  @ResponseBody
  def insert(
    @RequestBody value: Bookmark
  ): Bookmark = {
    log.debug("insert")
	dao.save(value)
  }
  
  
  @RequestMapping(
    value = Array("/{id}"), 
    method = Array(PUT)
  )
  @ResponseBody
  def update(
    @RequestBody value: Bookmark
  ): Bookmark = {
    log.debug("update")
	dao.save(value)
  }
 

  @RequestMapping(
    value = Array("/{id}"), 
    method = Array(DELETE)
  )
  @ResponseBody
  def remove(
    @PathVariable id: Long
  ): String = {
    val flag = dao.remove(id)
    log.debug("remove: " + id + " | status: " + flag)
    "ok"
  }
  
}