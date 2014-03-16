package bootstrap.liftweb

import net.liftweb._
import util._

import common._
import http._
import js.jquery.JQueryArtifacts
import sitemap._
import Loc._

import net.liftmodules.JQueryModule

import omniauth.Omniauth
import omniauth.lib._

import code.model._


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */



class Boot {

  def boot {

    DataBase.initDatabase()

    // where to search snippet
    LiftRules.addToPackages("code")

    // Build SiteMap

    val entries = List(
      Menu(Loc("index", "index" :: Nil, "Página de Inicio", Hidden)),
      Menu(Loc("profile", "profile" :: Nil, "Mi Perfil", LoginHelpers.loggedIn)),
      Menu(Loc("balance", "balance" :: Nil, "Mi Saldo", LoginHelpers.loggedIn)),
      Menu(Loc("invitations", "invitations" :: Nil, "Mis Invitaciones", LoginHelpers.loggedIn)),
      Menu(Loc("myContests", "contests" :: "index" :: Nil, "Mis Concursos", LoginHelpers.loggedIn)),
      Menu(Loc("allContests", "contests" :: "show" :: Nil, "Concursos", Hidden)),
      Menu(Loc("movies", ("movies" :: Nil) -> true, "Las Películas", Hidden))
    ) ::: Omniauth.sitemap

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries: _*))

    Omniauth.init
//    Omniauth.initWithProviders(List(new FacebookProvider("196869340515530", "b21e2b451fa8b336da6d9cfa271cbdde")))

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
      DataBase.db.withSession {
        implicit session =>
          println("Saliendo")
      }
    })
  }
}
