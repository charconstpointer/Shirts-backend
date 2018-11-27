package repository

import domain.{Shirt, Tables}
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ShirtRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends Tables with HasDatabaseConfigProvider[JdbcProfile] {

  import dbConfig._
  import profile.api._

  def create(shirt: Shirt): Future[Option[Int]] = {
    db.run((shirts returning shirts.map(_.shirtId)) += shirt)
  }

  def delete(id: Int): Future[Int] = {
    db.run(shirts.filter(_.shirtId === id).delete)
  }

  def getAllShirts() = {
    db.run(shirts.result)
  }

}