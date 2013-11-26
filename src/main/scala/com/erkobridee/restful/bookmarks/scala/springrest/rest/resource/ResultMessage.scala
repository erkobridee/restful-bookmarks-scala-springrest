package com.erkobridee.restful.bookmarks.scala.springrest.rest.resource

import javax.xml.bind.annotation.XmlRootElement
import scala.reflect.BeanProperty

@XmlRootElement
@scala.serializable
class ResultMessage {

  @BeanProperty
  var code: Int = 0
  
  @BeanProperty
  var message: String = ""
  
  //----------------------------------------------------------------------------
  // constructor
  
  def this(code: Int, message: String) {
    this()
    
    this.code = code
    this.message = message
  }
  
  //----------------------------------------------------------------------------
  
}