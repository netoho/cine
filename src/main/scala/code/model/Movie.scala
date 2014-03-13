package code.model

import scala.slick.driver.MySQLDriver.simple._

/**
 * Created by netoho on 1/10/14.
 */


case class Movie(id: Option[Int], name: String, synopsis: String, posterUrl: String)

class Movies(tag: Tag) extends Table[Movie](tag, "MOVIES") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def synopsis = column[String]("synopsis")

  def posterUrl = column[String]("posterUrl")

  def * = (id.?, name, synopsis, posterUrl) <> (Movie.tupled, Movie.unapply)
}

object Movie extends ((Option[Int], String, String, String) => Movie){

  val movies = TableQuery[Movies]
  val db = DataBase.db

  def all: List[Movie] = {
    db.withSession{
      implicit session =>
        movies.list
    }
  }



}

