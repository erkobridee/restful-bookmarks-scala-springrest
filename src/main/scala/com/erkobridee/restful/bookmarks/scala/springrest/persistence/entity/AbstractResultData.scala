package com.erkobridee.restful.bookmarks.scala.springrest.persistence.entity

import javax.xml.bind.annotation.XmlRootElement
import scala.reflect.BeanProperty

/*
	ref's
	
	A Tour of Scala: Generic Classes | Scala Lang
	http://www.scala-lang.org/old/node/113
	
	Scala type programming resources | StackOverflow
	http://stackoverflow.com/questions/4415511/scala-type-programming-resources

		PT_BR
		
	Generics, como Scala trata o Problema de Generalização de Classes | DClick Blog
	http://www.dclick.com.br/2010/11/19/generics-como-scala-trata-o-problema-de-generalizacao-de-classes/

 */

@XmlRootElement
@scala.serializable
abstract class AbstractResultData[T] {
  
  var data : T
  
  @BeanProperty
  var count : Int = 0
  
  @BeanProperty
  var page : Int = 0 // page index
  
  @BeanProperty
  var pages : Int = 0 // available pages
  
}