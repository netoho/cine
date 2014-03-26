package code.snippet

import net.liftmodules.paypal.snippet.BuyNowSnippet
import scala.xml.NodeSeq
import net.liftmodules.paypal.PaypalRules
import net.liftweb.util.Props

/**
 * Created by neto on 3/19/14.
 */
class TransactionSumary extends BuyNowSnippet {

  override def amount: Double = 70

  val currency_code = Props.get("paypal.currency") openOr "USD"
  val email = Props.get("paypal.email") openOr "hatreun-facilitator@gmail.com"
  val image = "/images/btn_buynow_LG.gif"

  override val values = Map(
    "currency_code" -> currency_code,
    "business" -> email,
    "item_number" -> "1",
    "item_name" -> "Carga de saldo por 70")

  override def buynow(xhtml: NodeSeq): NodeSeq =
    <form name="_xclick"
          action={PaypalRules.connection.vend().protocol + "://" + PaypalRules.mode.vend().domain + "/cgi-bin/webscr"}
          method="post">
      <input type="hidden" name="cmd" value="_xclick"/>
      <input type="hidden" name="amount" value={amount.toString}/>
      <input type="hidden" name="currency_code" value={currency_code}/>
      {values.-("amount", "cmd", "submit").map(x => <input type="hidden" name={x._1} value={x._2}/>)}
      <input type="image" src={image} name="submit" alt=" "/>
    </form>

}
