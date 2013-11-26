package com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity

import javax.xml.bind.annotation.XmlRootElement
import java.util.List
import scala.reflect.BeanProperty

@XmlRootElement
@scala.serializable
class BookmarkResultData extends AbstractResultData[List[Bookmark]]  {

  @BeanProperty
  var data : List[Bookmark] = null
  
  //----------------------------------------------------------------------------
  // constructors
  
  def this(count: Int, page: Int, size: Int) {
    this()
    
    super.updateInfo(count, page, size)
  }
  
  def this(data: List[Bookmark], count: Int, page: Int, size: Int) {
    this()
    
    this.data = data
    super.updateInfo(count, page, size)
  }
  
  //----------------------------------------------------------------------------
  
}