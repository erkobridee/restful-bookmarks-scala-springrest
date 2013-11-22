package com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity

import javax.xml.bind.annotation.XmlRootElement
import java.util.List
import scala.reflect.BeanProperty

@XmlRootElement
@scala.serializable
class BookmarkResultData extends AbstractResultData[List[Bookmark]]  {

  @BeanProperty
  var data : List[Bookmark] = null
  
}