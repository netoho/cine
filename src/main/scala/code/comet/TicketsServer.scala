package code.comet

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import code.snippet.ContestRequestVar
import code.model.{User, Ticket}
import net.liftweb.common.{Full, Loggable}
import bootstrap.liftweb.UserSessionVar

/**
 * Created by neto on 3/17/14.
 */

case class Data(ticketId: Int, userId: Int, tikectCost: Double)

object TicketsServer extends LiftActor with ListenerManager with Loggable {

  val user = UserSessionVar.is.get

  def createUpdate {}

  override def lowPriority = {
    case d: Data =>
      Ticket.find(d.ticketId) match {
        case Some(t) =>
          if (t.status == 10) {
            val currentBalance = user.balance
            val uu = User.update(user.copy(balance = currentBalance - d.tikectCost))
            UserSessionVar.set(Full(uu))
            val newTicket = t.copy(status = 20, userId = user.id)
            updateListeners(Ticket.update(newTicket))
          }
        case _ =>
          logger.info(s"A ticket data object arrived but none Ticket was found. Data: $d")
      }
  }

}
