package code.snippet

import net.liftweb.util.Helpers._

/**
 * Created by neto on 3/17/14.
 */
  class GetTicketActor {

  val contest = ContestRequestVar.is.get

  def render = {
    "#tickets-container [data-lift]" #> s"comet?type=TicketActor&name=contest-${contest.id.get}"
  }
}
