package code.model

/**
 * Created by unam on 18/03/14.
 */
object OrderStatus extends Enumeration {
  val Open = Value(1, "open")
  val Pending = Value(2, "pending")
  val Complete = Value(3, "complete")
  val Failed = Value(4, "failed")
}
