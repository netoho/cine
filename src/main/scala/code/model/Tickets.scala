package code.model

import scala.slick.driver.MySQLDriver.simple._

/**
 * Created by netoho on 1/22/14.
 */

case class Ticket(id: Option[Int], status: Int, contestId: Int)

class Tickets(tag: Tag) extends Table[Ticket](tag, "TICKETS") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def status = column[Int]("status")
  def contestId = column[Int]("contest_id")

  def contest = foreignKey("contest_FK", contestId, TableQuery[Contests])(_.id)

  def * = (id.?, status, contestId) <> (Ticket.tupled, Ticket.unapply)
}

object Ticket extends ((Option[Int], Int, Int) => Ticket){

  val db = DataBase.db
  val contests = TableQuery[Contests]
  val tickects = TableQuery[Tickets]

  def find(c: Contest): List[Ticket] = {
    db.withSession{
      implicit session =>
        tickects.filter(_.contestId === c.id).list
    }
  }
}