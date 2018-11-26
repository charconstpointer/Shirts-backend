package repository

import java.sql.Timestamp

import domain.{Order, Shirt}
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.RepOption

import scala.concurrent.ExecutionContext

@Singleton
class OrderRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  class OrderTable(_tableTag: Tag) extends Table[Order](_tableTag, "order") {
    def * = (orderId, date, clientName, clientAge) <> ((Order.apply _).tupled, Order.unapply)

    def ? = (Rep.Some(orderId), Rep.Some(date), Rep.Some(clientName), Rep.Some(clientAge)).shaped.<>({ r => import r._; _1.map(_ => (Order.apply _).tupled((_1.get, _2.get, _3.get, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))


    val orderId: Rep[Option[Int]] = column[Int]("order_id", O.AutoInc, O.PrimaryKey)
    val date: Rep[String] = column[String]("date")
    val clientName: Rep[String] = column[String]("client_name", O.Length(255, varying = true))
    val clientAge: Rep[Int] = column[Int]("client_age")
  }

  private val orders = TableQuery[OrderTable]

  def addOrder(order: Order) = db.run {
    (orders returning orders) += order
  }

  def getAllOrders = db.run {
    orders.result
  }

  def getOrderById(id: Int) = db.run {
    orders.filter(_.orderId === id).result.head
  }
}