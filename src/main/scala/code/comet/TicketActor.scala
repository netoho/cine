package code.comet

import net.liftweb.http.{SHtml, CometListener, CometActor}
import code.snippet.ContestRequestVar
import code.model.Ticket
import net.liftweb.common.{Full, Loggable}
import net.liftweb.http.js.JsCmds
import net.liftweb.util.Helpers._

/**
 * Created by neto on 3/17/14.
 */
class TicketActor extends CometActor with CometListener with Loggable {

  val contest = ContestRequestVar.is.get
  logger.info(contest)
  var tickets = Ticket.find(contest)

  override def lifespan = Full(60 seconds)

  def registerWith = TicketsServer

  override def lowPriority = {
    case t: Ticket => {
      tickets = Ticket.find(contest)
      logger.info(t)
      //      val (src, alt) =
      //        if (t.status == 10)
      //          ("/images/interrogacion.jpg", "Boleto Disponible")
      //        else
      //          ("/images/feliz.png", "Boleto No Disponible")
      //      val id = s"ticket.${t.contestId}.${t.id.get}"
      //      val html =
      //        <div class="column ticket" id={id}>
      //          <img class="ui small rounded image" src={src} alt={alt}/>
      //        </div>
      //      JsCmds.SetHtml(id, html)
      reRender()
    }
  }

  def sendingMessageActor(t: Ticket) = {
    TicketsServer ! t
    JsCmds.Alert(s"Enviando: $t")
  }

  def render = {
    ".ticket *" #> tickets.map(
      t => {
        (if (t.status == 10)
          "img [src]" #> "/images/interrogacion.jpg" & "img [alt]" #> "Boleto Disponible"
        else "img [src]" #> "/images/feliz.png" & "img [alt]" #> "Boleto No Disponible"
          ) andThen ("img [onclick]" #> SHtml.ajaxInvoke(() => sendingMessageActor(t)))
      })
  }
}