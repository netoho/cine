package code.comet

import net.liftweb.actor.LiftActor
import net.liftweb.http.ListenerManager
import code.snippet.ContestRequestVar
import code.model.Ticket
import net.liftweb.common.Loggable

/**
 * Created by neto on 3/17/14.
 */

object TicketsServer extends LiftActor with ListenerManager with Loggable {

  def createUpdate {}

  override def lowPriority = {
    case t:Ticket =>
      val newTicket = if(t.status == 10) t.copy(status = 20) else t.copy(status = 10)
      logger.info(newTicket)
      updateListeners(Ticket.save(newTicket))
  }

}
