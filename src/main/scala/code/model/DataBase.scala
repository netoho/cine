package code.model

import scala.slick.driver.MySQLDriver.simple._

/**
 * Created by neto on 3/11/14.
 */
object DataBase {

  val db = Database.forURL("jdbc:mysql://localhost:3306/CINE?user=root", driver = "com.mysql.jdbc.Driver")

  def initDatabase() {

    DataBase.db.withSession {
      implicit session =>
        val users = TableQuery[Users]
        val movies = TableQuery[Movies]
        val contests = TableQuery[Contests]
        val tikects = TableQuery[Tickets]
        try {
          (users.ddl ++ movies.ddl ++ contests.ddl ++ tikects.ddl).create
        } catch {
          case ex: Exception =>
            (users.ddl ++ movies.ddl ++ contests.ddl ++ tikects.ddl).drop
            (users.ddl ++ movies.ddl ++ contests.ddl ++ tikects.ddl).create
        }
        movies ++= Seq(
          Movie(Some(1), "The Godfather", "Cuenta la vida de Vito Corleone y su hijo Michael", "http://ia.media-imdb.com/images/M/MV5BMjEyMjcyNDI4MF5BMl5BanBnXkFtZTcwMDA5Mzg3OA@@._V1_SX214_.jpg"),
          Movie(Some(2), "Pulp Fiction", "", "http://ia.media-imdb.com/images/M/MV5BMjE0ODk2NjczOV5BMl5BanBnXkFtZTYwNDQ0NDg4._V1_SY317_CR4,0,214,317_.jpg"),
          Movie(Some(3), "Fight Club", "", "http://ia.media-imdb.com/images/M/MV5BMjIwNTYzMzE1M15BMl5BanBnXkFtZTcwOTE5Mzg3OA@@._V1_SX214_.jpg"),
          Movie(Some(4), "City of God", "", "http://ia.media-imdb.com/images/M/MV5BMjA4ODQ3ODkzNV5BMl5BanBnXkFtZTYwOTc4NDI3._V1_SX214_.jpg"),
          Movie(Some(5), "Casablanca", "", "http://ia.media-imdb.com/images/M/MV5BMTcwNDI5MjI1Ml5BMl5BanBnXkFtZTYwODE4NDI2._V1_SX214_.jpg"),
          Movie(Some(6), "Life is Beautiful", "", "http://ia.media-imdb.com/images/M/MV5BMTM3NDg0OTkxOV5BMl5BanBnXkFtZTcwMTk2NzIyMQ@@._V1_SY317_CR4,0,214,317_.jpg"),
          Movie(Some(7), "Léon", "", "http://ia.media-imdb.com/images/M/MV5BMTgzMzg4MDkwNF5BMl5BanBnXkFtZTcwODAwNDg3OA@@._V1_SY317_CR4,0,214,317_.jpg"))

        contests ++= Seq(
          Contest(Some(1), 10, 1, 1),
          Contest(Some(2), 10, 1, 2),
          Contest(Some(3), 10, 1, 3),
          Contest(Some(4), 10, 2, 1),
          Contest(Some(5), 10, 3, 1)
        )

        tikects ++= Seq(
          Ticket(Some(1), 10, 1),
          Ticket(Some(2), 20, 1),
          Ticket(Some(3), 10, 1),
          Ticket(Some(4), 10, 1),
          Ticket(Some(5), 10, 4),
          Ticket(Some(6), 10, 4)
        )

        (for (m <- movies) yield m.name).list().foreach(println)
    }
  }

}
