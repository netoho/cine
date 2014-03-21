package code.comet

import net.liftweb.http.{SHtml, CometListener, CometActor}
import code.snippet.ContestRequestVar
import code.model.{Ticket, User}
import net.liftweb.common.{Full, Loggable}
import net.liftweb.http.js.JsCmds
import net.liftweb.util.Helpers._
import bootstrap.liftweb.UserSessionVar

/**
 * Created by neto on 3/17/14.
 */
class TicketActor extends CometActor with CometListener with Loggable {

  val user = UserSessionVar.is.get
  val contest = ContestRequestVar.is.get
  var tickets = Ticket.find(contest)

  override def lifespan = Full(60 seconds)

  def registerWith = TicketsServer

  override def lowPriority = {
    case t: Ticket =>
      tickets = Ticket.find(contest)
      logger.info(s"The new and updated ticket: $t")
      reRender()
  }

  def sendingMessageActor(t: Ticket) = {
    val data = Data(t.id.get, user.id.get, t.cost)
    logger.info(s"Enviando: $data")
    TicketsServer ! data
    JsCmds.Noop
  }

  def render = {
    ".ticket *" #> tickets.map(
      t => {
        if (t.status == 10)
          "img [src]" #> "/images/interrogacion.jpg" &
            "img [alt]" #> "Boleto Disponible" &
            "img [onclick]" #> SHtml.ajaxInvoke(() => sendingMessageActor(t))
        else {
          val u = User.find(t.userId.get).get
          "img [src]" #> u.profileImage & "img [alt]" #> s"${u.firstName} compró el boleto"
        }
      })
  }
}