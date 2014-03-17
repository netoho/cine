package code.snippet

import omniauth.{AuthInfo, Omniauth}
import net.liftweb.common.{Loggable, Full}
import net.liftmodules.ext_api.facebook._
import scala.xml.Text
import net.liftweb.util.Helpers._
import net.liftweb.http.{S, SHtml}
import bootstrap.liftweb.{UserSessionVar, RefererSessionVar, LoginHelpers}
import code.model.User

/**
 * Created by neto on 3/13/14.
 */
class UsersSnippet extends Loggable {
  def menu = {

    RefererSessionVar.is match {
      case Full(url: String) =>
        logger.info(s"There is a value in RefererSessionVar: $url")
        S.redirectTo(url)
      case _ =>
        logger.info("There is nothing in the RefererSessionVar so we can load this page")
    }

    val isSession_? = LoginHelpers.checkUserSession
    val userOp = UserSessionVar.is

    "#name" #> (if(isSession_?)
        Text(s"${userOp.get.firstName.getOrElse("")} ${userOp.get.lastName.getOrElse("")}") ++ <i class="dropdown icon"></i>
      else
        SHtml.link("/auth/facebook/signin", () => {}, Text("Inicia Sesión")))
  }
}
