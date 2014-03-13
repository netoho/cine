package code.snippet

import net.liftweb.util.Helpers._

import code.model._
import scala.slick.driver.MySQLDriver.simple._
import net.liftweb.http.{RequestVar, SHtml}
import net.liftweb.common.{Empty, Box}
import scala.xml.Text
import net.liftweb.common.Full

/**
 * Created by netoho on 1/11/14.
 */

object MovieRequestVar extends RequestVar[Box[Movie]](Empty)

class MoviesSnippet {

  def moviesMainPage = {

    ".column *" #>
      (Movie.all map (
        movie => ".header *" #> SHtml.link("/movies/show", () => MovieRequestVar.set(Full(movie)), Text(movie.name)) &
          "img" #> SHtml.link("/movies/show", () => MovieRequestVar.set(Full(movie)), <img class="ui image large" src={movie.posterUrl}></img>)))
  }

  def movieDetails = {
    val movie = MovieRequestVar.is.get
    val movieContests = Contest.find(movie)

    "#movie-poster [src]" #> movie.posterUrl &
      "#movie-name *" #> movie.name &
      "#movie-synopsis *" #> movie.synopsis &
    "#movie-contests" #> {
      "h4 *" #>  s"Actualmente existen ${movieContests.length} concursos para está película" &
      ".movie-contest *" #> movieContests.map(mc => ".visible *" #> (<br/> ++ Text(s"¡Quedan ${Ticket.find(mc).length} boletos!") ++ <br/> ++ <br/>))1
    }

  }

}
