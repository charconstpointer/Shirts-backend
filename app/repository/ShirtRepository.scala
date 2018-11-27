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

  def findById(id: Int) = {
    db.run(shirts.filter(_.shirtId === id).result)
  }

  def delete(id: Int): Future[Int] = {
    db.run(shirts.filter(_.shirtId === id).delete)
  }

  def update(id: Int, shirt: Shirt)(implicit exec: ExecutionContext) = {
    db.run(
      shirts.filter(_.shirtId === id).map(r => (r.shirtColor, r.shirtCount, r.shirtSize)).update((shirt.color, shirt.count, shirt.size)).map {
        case 0 => None
        case _ => Some(shirt)
      }
    )
  }

  def getAllShirts() = {
    db.run(shirts.result)
  }

}