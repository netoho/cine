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
      Menu.i("Home") / "index"
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
      DataBase.db.withSession {
        implicit session =>
          println("Saliendo")
      }
    })
  }
}
