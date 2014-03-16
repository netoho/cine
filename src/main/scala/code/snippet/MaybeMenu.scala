package code.snippet

import scala.xml.NodeSeq
import net.liftweb.http.S

/**
 * Created by neto on 3/14/14.
 */
object MaybeMenu {
  def render(in: NodeSeq): NodeSeq = {
    if (!(S.request.map(_.buildMenu.lines)
               openOr Nil).isEmpty) in(0).child else NodeSeq.Empty
  }
}