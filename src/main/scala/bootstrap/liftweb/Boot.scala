package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import js.jquery.JQueryArtifacts
import sitemap._
import Loc._

import code.model._
import net.liftmodules.JQueryModule

import omniauth.lib._
import omniauth.Omniauth

import code.model._
import scala.slick.driver.PostgresDriver.simple._


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */

object Connections {
  val db = Database.forURL("jdbc:postgresql://localhost:5432/moviefree?user=postgres&password=lol", driver = "org.postgresql.Driver")
}

class Boot {


  val movies = TableQuery[Movies]
  val contests = TableQuery[Contests]
  val tikects = TableQuery[Tickets]

  def initDatabase() {

    Connections.db.withSession {
      implicit session =>
        try {
          (movies.ddl ++ contests.ddl ++ tikects.ddl).create
        }
        catch {
          case ex: Exception => (movies.ddl ++ contests.ddl ++ tikects.ddl).drop; (movies.ddl ++ contests.ddl ++ tikects.ddl).create;
        }
        movies ++= Seq(
          Movie(Some(1), "The Godfather", "Cuenta la vida de Vito Corleone y su hijo Michael", "http://ia.media-imdb.com/images/M/MV5BMjEyMjcyNDI4MF5BMl5BanBnXkFtZTcwMDA5Mzg3OA@@._V1_SX214_.jpg"),
          Movie(Some(2), "Pulp Fiction", "", "http://ia.media-imdb.com/images/M/MV5BMjE0ODk2NjczOV5BMl5BanBnXkFtZTYwNDQ0NDg4._V1_SY317_CR4,0,214,317_.jpg"),
          Movie(Some(3), "Fight Club", "", "http://ia.media-imdb.com/images/M/MV5BMjIwNTYzMzE1M15BMl5BanBnXkFtZTcwOTE5Mzg3OA@@._V1_SX214_.jpg"),
          Movie(Some(4), "City of God", "", "http://ia.media-imdb.com/images/M/MV5BMjA4ODQ3ODkzNV5BMl5BanBnXkFtZTYwOTc4NDI3._V1_SX214_.jpg"),
          Movie(Some(5), "Casablanca", "", "http://ia.media-imdb.com/images/M/MV5BMTcwNDI5MjI1Ml5BMl5BanBnXkFtZTYwODE4NDI2._V1_SX214_.jpg"),
          Movie(Some(6), "Life is Beautiful", "", "http://ia.media-imdb.com/images/M/MV5BMTM3NDg0OTkxOV5BMl5BanBnXkFtZTcwMTk2NzIyMQ@@._V1_SY317_CR4,0,214,317_.jpg"),
          Movie(Some(7), "Léon", "", "http://ia.media-imdb.com/images/M/MV5BMTgzMzg4MDkwNF5BMl5BanBnXkFtZTcwODAwNDg3OA@@._V1_SY317_CR4,0,214,317_.jpg"))

        contests ++= Seq(
          Contest(Some(1), 10, 1, 1),
          Contest(Some(2), 10, 1, 2),
          Contest(Some(3), 10, 1, 3),
          Contest(Some(4), 10, 2, 1),
          Contest(Some(5), 10, 3, 1)
        )

        tikects ++= Seq(
          Ticket(Some(1), 10, 1),
          Ticket(Some(2), 20, 1),
          Ticket(Some(3), 10, 1),
          Ticket(Some(4), 10, 1),
          Ticket(Some(5), 10, 2),
          Ticket(Some(6), 10, 2)
        )

        (for (m <- movies) yield m.name).list().foreach(println)
    }
  }

  def boot {

    initDatabase()

    // where to search snippet
    LiftRules.addToPackages("code")

    // Build SiteMap

    val entries = List(
      Menu.i("Home") / "index",
      Menu.i("Home2") / "index_noLog.html",
      Menu.i("Home3") / "index_siLog.html",
      Menu.i("Home4") / "masSaldo.html",
      Menu.i("Home5") / "miPerfil.html",
      Menu.i("Home6") / "miSaldo.html",
      Menu.i("Home7") / "misConcursos.html",
      Menu.i("Home8") / "misInvitaciones.html",
      Menu.i("Home9") / "participar-concurso.html",
      Menu.i("Home10") / "pelicula-concurso.html",
      Menu(Loc("Static", Link(List("static"), true, "/static/index"),
        "Static Content"))
    ) ::: Omniauth.sitemap

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    //LiftRules.setSiteMap(SiteMap(entries: _*))

    Omniauth.init

    //Init the jQuery module, see http://liftweb.net/jquery for more information.
    LiftRules.jsArtifacts = JQueryArtifacts
    JQueryModule.InitParam.JQuery = JQueryModule.JQuery172
    JQueryModule.init()

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => true)

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    LiftRules.unloadHooks.append(() => {
      Connections.db.withSession {
        implicit session => movies.ddl.drop
      }
    })
  }
}
