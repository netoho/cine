package code
package model

import net.liftweb.util._
import net.liftweb.common._
import scala.slick.driver.MySQLDriver.simple._

/**
 * User that contains id (database PK) first name, last name,
 * uid (the identifier from the auth provider, in our case facebook) and
 * the url to the profile image
 */
case class User(id: Option[Int], firstName: Option[String], lastName: Option[String], uid: String, authToken: String, profileImage: String)

class Users(tag: Tag) extends Table[User](tag, "USERS") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def firstName = column[Option[String]]("first_name", O.Nullable)

  def lastName = column[Option[String]]("last_name", O.Nullable)

  def uid = column[String]("uid")

  def authToken = column[String]("auth_token")

  def profileImage = column[String]("profile_image")

  def * = (id.?, firstName, lastName, uid, authToken, profileImage) <> (User.tupled, User.unapply)
}

object User extends ((Option[Int], Option[String], Option[String], String, String, String) => User) {
  val users = TableQuery[Users]
  val db = DataBase.db

  def find(uid: String): Option[User] = {
    db.withSession {
      implicit session =>
        users.filter(_.uid === uid).firstOption
    }
  }

  def register(u: User): Int = {
    db.withSession {
      implicit session =>
        users += u
    }
  }

  def update(user: User): User = {
    db.withSession{
      implicit session =>
        val q = for(u <- users if u.uid===user.uid) yield (u.firstName, u.lastName, u.authToken, u.profileImage)
        q.update(user.firstName, user.lastName, user.authToken, user.profileImage)
        users.filter(_.uid === user.uid).first()
    }
  }

  def registerOrUpdate(u: User): User = {
    find(u.uid) match {
      case None =>
        val userId = register(u)
        u.copy(id = Some(userId))
      case _ =>
        update(u)
    }
  }
}