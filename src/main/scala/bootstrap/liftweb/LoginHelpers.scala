package bootstrap.liftweb

import net.liftweb.sitemap.Loc.If
import omniauth.{AuthInfo, Omniauth}
import net.liftweb.common.{Full, Box, Empty}
import code.model.User
import net.liftweb.http.{S, SessionVar}

/**
 * Created by neto on 3/13/14.
 */

object UserSessionVar extends SessionVar[Box[User]](Empty)
object RefererSessionVar extends SessionVar[Box[String]](Empty)

object LoginHelpers {

  def checkUserSession = Omniauth.currentAuth match{
    case Full(auth:AuthInfo) =>
      val user = User.registerOrUpdate(User(None, auth.firstName, auth.lastName, auth.uid, auth.token.token, s"http://graph.facebook.com/${auth.uid}/picture?type=normal"))
      UserSessionVar.set(Full(user))
      true
    case _ =>
      false
  }

  val loggedIn = If(checkUserSession _, () => {println(S.referer); S.redirectTo("/auth/facebook/signin")})
}
