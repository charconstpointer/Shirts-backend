package repository

import domain.{Order, OrderHasShirt, Shirt}
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.model.ForeignKeyAction
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext

@Singleton
class OrderHasShirtRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  class OrderHasShirtTable(_tableTag: Tag) extends Table[OrderHasShirt](_tableTag, "order_has_shirt") {
    def * =
      (orderHasShirtId, shirtId, orderId) <> ((OrderHasShirt.apply _).tupled, OrderHasShirt.unapply)

    def ? = (Rep.Some(orderHasShirtId), Rep.Some(shirtId), Rep.Some(orderId)).shaped.<>({ r => import r._; _1.map(_ => OrderHasShirt.tupled((_1.get, _2.get, _3.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    val orderHasShirtId: Rep[Option[Int]] = column[Int]("order_has_shirt_id", O.AutoInc, O.PrimaryKey)
    val shirtId: Rep[Int] = column[Int]("shirt_id")
    val orderId: Rep[Int] = column[Int]("order_id")

  }

  lazy val ordersShirts = TableQuery[OrderHasShirtTable]

  def saveShirtOrder(orderHasShirt: Seq[OrderHasShirt]) = {
    val s: Seq[OrderHasShirt] = orderHasShirt
    for {
      order <- orderHasShirt
    } db.run(ordersShirts += OrderHasShirt(None, order.shirtId ,order.orderId))
  }
}