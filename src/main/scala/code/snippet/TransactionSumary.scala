package code.snippet

import net.liftmodules.paypal.snippet.BuyNowSnippet

/**
 * Created by neto on 3/19/14.
 */
class TransactionSumary extends BuyNowSnippet{




  override def amount: Double = 0.1

  override val values = Map(
    "business" -> "hatreun@gmail.com",
    "item_number" -> "1",
    "item_name" -> ("Auction Order: " + "1"))
}
