package code
package model

import net.liftweb.util._
import net.liftweb.common._
import scala.slick.driver.MySQLDriver.simple._

/**
 * An O-R mapped "User" class that includes first name, last name, password and we add a "Personal Essay" to it
 */
case class User(id: Option[Int], name: String, uid: String, profileImage: String)

class Users(tag: Tag) extends Table[User](tag, "USERS") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def uid = column[String]("uid")

  def profileImage = column[String]("profile_image")

  def * = (id.?, name, uid, profileImage) <>(User.tupled, User.unapply)
}

object User extends ((Option[Int], String, String, String) => User) {
  val users = TableQuery[Users]
  val db = DataBase.db

  def find(uid: String): Option[User] = {
    db.withSession {
      implicit session =>
        users.filter(_.uid === uid).firstOption
    }
  }
}