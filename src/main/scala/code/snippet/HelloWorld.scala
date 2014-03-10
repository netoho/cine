package code 
package snippet 

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import code.lib._
import Helpers._
import omniauth.Omniauth
import omniauth.AuthInfo

class HelloWorld {
  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  // replace the contents of the element with id "time" with the date
  def howdy = "#time *" #> date.map(_.toString) & "#nom" #> (Omniauth.currentAuth match {
    case Full(auth:AuthInfo) => auth.email getOrElse "no hay email"
    case _ => "no neto"
  })

  /*
   lazy val date: Date = DependencyFactory.time.vend // create the date via factory

   def howdy = "#time *" #> date.toString
   */
}

