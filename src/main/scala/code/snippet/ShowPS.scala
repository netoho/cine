package code.snippet

import net.liftweb.util.Helpers._
import net.liftweb.common.Full
import code.lib.paySession

/**
 * Created by neto on 3/21/14.
 */
class ShowPS {
  def render = "*" #> paySession.is.get.toString
}
