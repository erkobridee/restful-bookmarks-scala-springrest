package com.erkobridee.restful.bookmarks.scala.springrest.dao

import java.util.List
import com.erkobridee.restful.bookmarks.scala.springrest.entity.Bookmark
import javax.transaction.Transaction
import org.springframework.orm.hibernate3.support.HibernateDaoSupport

trait TraitBookmarkDAO {

  def listAll : List[Bookmark]

  def findById(id: Long) : Bookmark

  def findByName(name : String) : List[Bookmark]
  
  def save(value : Bookmark) : Bookmark
  
  def remove(id : Long) : Boolean
  
}