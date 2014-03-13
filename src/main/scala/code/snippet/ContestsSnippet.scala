package code.snippet

import net.liftweb.util.Helpers._
import code.model._
import scala.slick.lifted.TableQuery
import net.liftweb.http.RequestVar
import net.liftweb.common.{Empty, Box}
import scala.slick.driver.MySQLDriver.simple._


/**
 * Created by netoho on 1/24/14.
 */

object ContestRequestVar extends RequestVar[Box[Contest]](Empty)

class ContestsSnippet {

  def participar = {
    val movie = MovieRequestVar.is.get
    val contest = ContestRequestVar.is.get
    val tickets = Ticket.find(contest)

    "#movie-name *" #> movie.name &
      "#movie-poster [src]" #> movie.posterUrl &
      "#movie-poster [alt]" #> movie.name &
      ".ticket *" #> tickets.map(
        t =>
          if (t.status == 10)
            "img [src]" #> "/images/interrogacion.jpg" & "img [alt]" #> "Boleto Disponible"
          else "img [src]" #> "/images/feliz.png" & "img [alt]" #> "Boleto No Disponible"
      )
  }
}
