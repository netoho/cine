package code.model

import scala.slick.driver.MySQLDriver.simple._

/**
 * Created by netoho on 1/22/14.
 */

case class Ticket(id: Option[Int], status: Int, cost: Double, contestId: Int, userId: Option[Int])

class Tickets(tag: Tag) extends Table[Ticket](tag, "TICKETS") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def status = column[Int]("status")
  def cost = column[Double]("cost")
  def contestId = column[Int]("contest_id")
  def userId = column[Option[Int]]("user_id")

  def contest = foreignKey("contest_FK", contestId, TableQuery[Contests])(_.id)
  def user = foreignKey("user_FK", userId, TableQuery[Users])(_.id)

  def * = (id.?, status, cost, contestId, userId) <> (Ticket.tupled, Ticket.unapply)
}

object Ticket extends ((Option[Int], Int, Double, Int, Option[Int]) => Ticket){

  val db = DataBase.db
  val contests = TableQuery[Contests]
  val tickets = TableQuery[Tickets]

  def find(id: Int): Option[Ticket] = {
    db.withSession{
      implicit session =>
        tickets.filter(_.id === id).firstOption
    }
  }

  def find(c: Contest): List[Ticket] = {
    db.withSession{
      implicit session =>
        tickets.filter(_.contestId === c.id).list
    }
  }

  def update(ticket: Ticket): Ticket = {
    db.withSession{
      implicit session =>
        val q = for {
          t <- tickets if t.id === ticket.id
        } yield (t.status, t.userId)
        q.update(ticket.status, ticket.userId)
        tickets.filter(_.id === ticket.id).first()
    }
  }
}