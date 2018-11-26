package repository

import domain.Shirt
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.Result
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class ShirtRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  class ShirtTable(_tableTag: Tag) extends Table[Shirt](_tableTag, "shirt") {
    def * = (shirtId, shirtSize, shirtColor, shirtCount) <> ((Shirt.apply _).tupled, Shirt.unapply)

    def ? = (Rep.Some(shirtId), Rep.Some(shirtSize), Rep.Some(shirtColor), Rep.Some(shirtCount)).shaped.<>({ r => import r._; _1.map(_ => (Shirt.apply _).tupled((_1.get, _2.get, _3.get, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    val shirtId: Rep[Option[Int]] = column[Int]("shirt_id", O.PrimaryKey, O.AutoInc)
    val shirtSize: Rep[String] = column[String]("size", O.Length(255, varying = true))
    val shirtColor: Rep[String] = column[String]("color", O.Length(255, varying = true))
    val shirtCount: Rep[Int] = column[Int]("shirt_count")
  }

  private val shirts = TableQuery[ShirtTable]

  def create(shirt: Shirt) = db.run {
    (shirts returning shirts) += shirt
  }

  def getAllShirts() = db.run{
    shirts.result
  }

}