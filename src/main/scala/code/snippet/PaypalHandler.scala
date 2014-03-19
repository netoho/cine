package code.snippet

import net.liftmodules.paypal.{PayPalInfo, PaypalPDT, PaypalIPN}
import net.liftmodules.paypal.PaypalTransactionStatus._
import net.liftweb.common.{Full, Loggable}
import net.liftweb.util.Props
import net.liftweb.http.DoRedirectResponse

/**
 * Created by unam on 18/03/14.
 */
object PaypalHandler extends PaypalIPN with PaypalPDT with Loggable {

  override def paypalAuthToken = Props.get("paypal.authtoken") openOr("EABjlBBZYjd3N-M2ALX-C2FH4UroavsgXLzYa4vY2DjTF5LWafTFGkx_8LJo")

  override def pdtResponse = {
    case (info, response) =>
      info.paymentStatus match {
        case Full(CompletedPayment) =>
          DoRedirectResponse.apply("/paypal/success")
        case _ =>
          DoRedirectResponse.apply("/paypal/failure")
      }
  }

  override def actions = {
    case (CompletedPayment, info, _) =>

  }

  private def updateOrder(info: PayPalInfo, status: OrderStat)
}
