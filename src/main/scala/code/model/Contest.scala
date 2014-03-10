package code.model

import scala.slick.driver.PostgresDriver.simple._

/**
 * Created by netoho on 1/10/14.
 */
case class Contest(id: Option[Int], status: Int, movieId: Int, number: Int)

class Contests(tag: Tag) extends Table[Contest](tag, "CONTESTS") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def status = column[Int]("status")
  def movieId = column[Int]("movie_id")
  def number = column[Int]("number")

  def movie = foreignKey("MOVIE_FK", movieId, TableQuery[Movies])(_.id)

  def * = (id.?, status, movieId, number) <> (Contest.tupled, Contest.unapply)

}