package code.comet

import code.snippet.ContestRequestVar
import code.model.Ticket
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds

/**
 * Created by neto on 3/17/14.
 */
object TicketsFrontend {

  val contest = ContestRequestVar.is.get
  val tickets = Ticket.find(contest)

  def sendingMessageActor(t: Ticket) = {
    TicketsServer ! t
    JsCmds.Alert(s"Enviando: $t")
  }

  def render = {
    "#ticketsContainer *" #> tickets.map(
      t => {
        (if (t.status == 10)
          "img [src]" #> "/images/interrogacion.jpg" & "img [alt]" #> "Boleto Disponible"
        else "img [src]" #> "/images/feliz.png" & "img [alt]" #> "Boleto No Disponible"
          ) andThen (".ticket [onClick]" #> sendingMessageActor(t) & ".ticket [id]" #> s"ticket.${t.contestId}.${t.id}")
      }
    )
  }

}
