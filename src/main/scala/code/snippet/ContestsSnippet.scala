package code.snippet

import net.liftweb.util.Helpers._
import code.model._
import omniauth.{Omniauth, AuthInfo}
import net.liftweb.http.{CurrentReq, S, RequestVar}
import net.liftweb.common.{Loggable, Full, Empty, Box}
import bootstrap.liftweb.RefererSessionVar


/**
 * Created by netoho on 1/24/14.
 */

object ContestRequestVar extends RequestVar[Box[Contest]](Empty)

class ContestsSnippet extends Loggable {

  def participar = {

    Omniauth.currentAuth match {
      case Full(auth: AuthInfo) =>
        logger.info("There is an auth so it is safe to load this page")
      case _ =>
        logger.info("There is not auth so redirecting to facebook signin")
        val uri = CurrentReq.value.request.uri
        val qs = CurrentReq.value.request.queryString openOr ""
        val completeURI = s"$uri?$qs"
        logger.info(s"URI: $uri")
        logger.info(s"QUERY STRING: $qs")
        logger.info(s"Current COMPLETE url: $completeURI")
        RefererSessionVar.set(Full(completeURI))
        S.redirectTo("/auth/facebook/signin")
    }

    RefererSessionVar.is match {
      case Full(url: String) =>
        logger.info(s"There is a value in RefererSessionVar: $url")
        if (url.contains("/contests/show")) {
          logger.info("The value in RefererSessionVar is one from this page so it is safe to load this page and reset Referer")
          RefererSessionVar.set(Empty)
        } else {
          logger.info("The value in RefererSessionVar is no the same, so we redirect to that url")
          S.redirectTo(url)
        }
      case _ =>
        logger.info("RefererSessionVar is Empty so it is safe to load this page")
    }

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
