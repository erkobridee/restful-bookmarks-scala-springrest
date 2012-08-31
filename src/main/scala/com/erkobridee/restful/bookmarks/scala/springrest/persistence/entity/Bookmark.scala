package com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity

import scala.reflect.BeanProperty
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.xml.bind.annotation.XmlRootElement
import javax.persistence.GenerationType

@Entity
@Table(name = "bookmark")
@XmlRootElement
@scala.serializable
class Bookmark() {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column
  @BeanProperty
  var id : Long = 0;
  
  @Column(nullable = false, length = 250)
  @BeanProperty
  var name : String = null;
  
  @Column
  @BeanProperty
  var description : String = null;
  
  @Column(nullable = false, length = 250)
  @BeanProperty
  var url : String = null;
  
}