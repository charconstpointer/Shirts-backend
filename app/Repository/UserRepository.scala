package Repository


import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcProfile
import slick.sql.SqlAction

import scala.concurrent.{ExecutionContext, Future}

case class User(id: Long, firstName: String, lastName: String)

object User {
  implicit val writeUser = Json.writes[User]
  implicit val readUser = Json.reads[User]
  implicit val formatUser = Json.format[User]
}

class UsersTable(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def firstName = column[String]("first_name")

  def lastName = column[String]("last_name")

  def * = (id, firstName, lastName) <> ((User.apply _).tupled, User.unapply)

}

class UserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)
                       (implicit ec: ExecutionContext) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

  protected implicit def executeFromDb[A](action: SqlAction[A, NoStream, _ <: slick.dbio.Effect]): Future[A] = {
    db.run(action)
  }

  val usersTable = TableQuery[UsersTable]

  def findAll: Future[Seq[User]] = usersTable.result

  def create(user: User): Future[Long] = usersTable.returning(usersTable.map(_.id)) += user

  def findById(userId: Long): Future[User] = usersTable.filter(_.id === userId).result.head

  def delete(userId: Long): Future[Int] = usersTable.filter(_.id === userId).delete
}
