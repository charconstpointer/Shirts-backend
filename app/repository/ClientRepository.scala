package repository

import domain.Client
import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import javax.inject.Singleton

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ClientRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

    private class ClientTable(tag: Tag) extends Table[Client](tag, "client") {
      def * = (userId, name) <> ((Client.apply _).tupled, Client.unapply)

      def ? = (Rep.Some(userId), Rep.Some(name)).shaped.<>({ r => import r._; _1.map(_ => (Client.apply _).tupled((_1.get, _2.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

      val userId: Rep[Int] = column[Int]("client_id", O.PrimaryKey, O.AutoInc)
      val name: Rep[String] = column[String]("name", O.Length(255, varying = true))
    }

  private val people = TableQuery[ClientTable]

  def create(client: Client) = db.run {
    people += client
  }

  def findById(clientId: Int) = db.run {
    people.filter(_.userId === clientId).result.head
  }

  def list(): Future[Seq[Client]] = db.run {
    people.result
  }
}