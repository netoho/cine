package code.model

import scala.slick.driver.PostgresDriver.simple._

/**
 * Created by netoho on 1/10/14.
 */

case class Movie(id: Option[Int], name: String, sypnosis: String, posterUrl: String)

class Movies(tag: Tag) extends Table[Movie](tag, "MOVIES") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def synopsis = column[String]("synopsis")

  def posterUrl = column[String]("posterUrl")

  def * = (id.?, name, synopsis, posterUrl) <> (Movie.tupled, Movie.unapply)
}