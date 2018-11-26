package service

import domain.{Order}
import javax.inject.Inject
import repository.{OrderHasShirtRepository, OrderRepository, ShirtRepository}
import rest.OrderRest
import util.IdToOrderHasShirt
import scala.concurrent.duration._

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


class ManageOrderService @Inject()(orderHasShirtRepository: OrderHasShirtRepository, orderRepository: OrderRepository, shirtRepository: ShirtRepository) {

  def insertOrder(orderRest: OrderRest) = {
    var rv: Option[Int] = null
    orderRepository.addOrder(
      Order(None,
        orderRest.date,
        orderRest.name,
        orderRest.age)
    ).onComplete {
      case Success(value) => {
        rv = value.orderId
        orderHasShirtRepository.saveShirtOrder(IdToOrderHasShirt.convert(orderRest.shirts, value.orderId.get))
      }
      case Failure(exception) => {
        rv = Some(-1)
        exception.printStackTrace
      }
    }
  }
}
