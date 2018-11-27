package repository

import java.sql.Timestamp

import domain.{Order, OrderHasShirt, Shirt, Tables}
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import rest.OrderRest
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.RepOption

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends Tables with HasDatabaseConfigProvider[JdbcProfile] {


  import dbConfig._
  import profile.api._

  def findById(id: Int)(implicit ec: ExecutionContext) = {
    db.run(getOrderQuery(Option(id)).result) map {
      tuples =>
        val grouped = tuples.groupBy(_._1)
        grouped.map {
          case (s, t) =>
            val shirts = t.map(_._2).distinct.map { shi => Shirt(None, shi.size, shi.color, 1) }
            OrderRest(
              s.clientName,
              s.clientAge,
              s.date,
              shirts.toList
            )
        }.headOption
    }
  }

  private def getOrderQuery(id: Option[Int] = None) = {

    val ordersQuery = id match {
      case None => ordersShirts
      case Some(id) => ordersShirts.filter(_.orderId === id)
    }

    val query = for {
      s <- shirts
      os <- ordersQuery if s.shirtId === os.shirtId
      o <- orders if os.orderId === o.orderId
    } yield (o, s, os)
    query
  }


  def create(orderRest: OrderRest)(implicit ec: ExecutionContext): Future[Int] = {
    val order: Order = Order(None, orderRest.date, orderRest.name, orderRest.age)
    db.run((orders returning orders.map(_.orderId)) += order) flatMap {
      orderId =>
        val orderShirt = orderRest.shirts.map(shirt => OrderHasShirt(None, shirt.shirtId.get, orderId.get))
        db.run(ordersShirts ++= orderShirt).map(_ => orderId.get)
    }
  }

  def getAllOrders = db.run {
    orders.result
  }

  def getOrderById(id: Int) = db.run {
    //    println(getOrderQuery(Option(id)))
    orders.filter(_.orderId === id).result.head
  }
}