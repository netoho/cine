package code.snippet

import net.liftweb.util.Helpers._
import code.model._
import scala.slick.lifted.TableQuery
import net.liftweb.http.RequestVar
import net.liftweb.common.{Empty, Box}
import bootstrap.liftweb.Connections
import scala.slick.driver.PostgresDriver.simple._


/**
 * Created by netoho on 1/24/14.
 */

object ContestRequestVar extends RequestVar[Box[Contest]](Empty)

class ContestsSnippet {

  def participar = {
    val contest = ContestRequestVar.is.get
    val tickets = TableQuery[Tickets]
    val ticketsM = Connections.db.withSession {
      implicit session =>
        tickets.filter(_.contestId === contest.id).list()
    }

    "#selectable2 *" #> {
      ".ui-widget-content *" #> ticketsM.map(
        t => "img [src]" #> (if (t.status == 10) "/img/interrogacion.jpg" else "/img/feliz.png")
      )
    }
  }
}
