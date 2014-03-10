package code.snippet

import net.liftweb.util.Helpers._

import code.model._
import scala.slick.driver.PostgresDriver.simple._
import bootstrap.liftweb.Connections
import net.liftweb.http.{RequestVar, SHtml}
import net.liftweb.common.{Empty, Full, Box}
import scala.xml.Text
import net.liftweb.common.Full

/**
 * Created by netoho on 1/11/14.
 */

object MovieRequestVar extends RequestVar[Box[Movie]](Empty)

class MoviesSnippet {

  def moviesMainPage = {
    val movies = TableQuery[Movies]

    ".sec_pel" #>
      Connections.db.withSession {
        implicit session =>
          movies.list() map (
            movie => {
              <div class="span3 sec_pel">
                <div class="textc">
                  <h2>
                    {SHtml.link("/pelicula-concurso.html",
                    () => MovieRequestVar.set(Full(movie)),
                    Text(movie.name))}
                  </h2>{SHtml.link("/pelicula-concurso.html",
                  () => MovieRequestVar.set(Full(movie)),
                    <img src={movie.posterUrl} alt=" " class="img-rounded"/>)}
                </div>
              </div>
            })
      }
  }

  def movieDetails = {
    val movie = MovieRequestVar.is.get
    val movies = TableQuery[Movies]
    val contests = TableQuery[Contests]
    val movieContests = Connections.db.withSession {
      implicit session =>
        contests.filter(_.movieId === movie.id).list()
    }
    val tickets = TableQuery[Tickets]

    ".contest *" #>
      movieContests.map(
        contest =>
          ".concurso *" #> SHtml.link("/participar-concurso", () => ContestRequestVar.set(Full(contest)),
            <p>
              {contest.number}
            </p> ++
              <p>Quedan
                {Connections.db.withSession {
                implicit session =>
                  tickets.filter(t => t.contestId === contest.id && t.status === 10).list().length
              }}
                boletos</p>)) &
      ".img-rounded [src]" #> movie.posterUrl &
      ".nC *" #> ("Actualmente existen " + movieContests.length + " concursos para esta película.") &
      "#movie_detalis" #> {
        "h4 *" #> movie.name & "p *" #> movie.sypnosis
      }
  }

}
