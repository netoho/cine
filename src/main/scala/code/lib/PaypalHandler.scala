package code.lib

import net.liftmodules.paypal.{PaypalSandbox, PayPalInfo, PaypalPDT, PaypalIPN}
import net.liftmodules.paypal.PaypalTransactionStatus._
import net.liftweb.common.{Empty, Full, Loggable, Box}
import net.liftweb.util.Props
import net.liftweb.http.{SessionVar, DoRedirectResponse}
import code.model.{User, OrderStatus}
import bootstrap.liftweb.UserSessionVar

/**
 * Created by unam on 18/03/14.
 */

object paySession extends SessionVar[Box[PayPalInfo]](Empty)

object PaypalHandler extends PaypalIPN with PaypalPDT with Loggable {


  val paypalAuthToken = Props.get("paypal.authtoken") openOr "AJfpE0H4KABQ7cWs7PXxH0f0LzHz8ILozaJdVJAvRjZg4uxDfWOj4mttB38"

  override def pdtResponse = {
    case (info, response) =>
      info.paymentStatus match {
        case Full(CompletedPayment) =>
          println("Pago completado!")
          paySession.set(Full(info))
          updateOrder(info, OrderStatus.Complete)
          DoRedirectResponse.apply("/paypal/success")
        case _ =>
          DoRedirectResponse.apply("/paypal/failure")
      }
  }

  def actions = {
    case (Full(status), info, response) => {
      paySession.set(Full(info))
      updateOrder(info, OrderStatus.Complete)
      println("actions - case Full")

      logger.info("Got a verified PayPal IPN: "+status)
      logger.debug("  PayPalInfo object: ")
      logger.debug("  item_number: " + info.itemNumber)
      logger.debug("  quantity: " + info.quantity)
      logger.debug("  paymentGross: " + info.paymentGross)
      logger.debug("  paymentCurrency: " + info.paymentCurrency)
      logger.debug("  txn_id: " + info.txnId)
      logger.debug("  custom: " + info.custom)
      logger.debug("\n")
      logger.debug("  item_name: " + info.itemName)
      logger.debug("  business: " + info.business)
      logger.debug("  reasonCode: " + info.reasonCode)
      logger.debug("  pendingReason: " + info.pendingReason)
      logger.debug("  paymentDate: " + info.paymentDate)
      logger.debug("  paymentFee: " + info.paymentFee)
      logger.debug("  tax: " + info.tax)
      logger.debug("  paymentType: " + info.paymentType)
      logger.debug("  settleAmount: " + info.settleAmount)
      logger.debug("  txnType: " + info.txnType)
      logger.debug("  payerId: " + info.payerId)
      logger.debug("  payerEmail: " + info.payerEmail)
      logger.debug("  receiverId: " + info.receiverId)
      logger.debug("  receiverEmail: " + info.receiverEmail)

      status match {
        case CompletedPayment => {
          // process your completed
          println("CompletedPayment")
        }
        case RefundedPayment => {
          // process refund
          println("RefundedPayment")
        }
        case other => {
          // process other (everything else)
          println("other")
        }
      } // end status match
    }// end case(Full(status), info, response)
    case _ => {

      println("actions - case default")

    }
  } // end actions

  private def updateOrder(info: PayPalInfo, status: OrderStatus.Value) {
    val user = UserSessionVar.is.get
    if(status == OrderStatus.Complete){
      val previousBalance = user.balance
      val userUpdated = user.copy(balance = previousBalance + (info.mcGross openOr "0.0").toDouble)
      UserSessionVar.set(Full(User.update(userUpdated)))
    }
    logger.debug(s"status: $status, info: $info")
  }
}
