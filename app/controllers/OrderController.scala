package controllers

import domain.{Order, OrderHasShirt, Shirt}
import javax.inject.{Inject, Singleton}
import org.omg.CORBA.Any
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import repository.{OrderHasShirtRepository, OrderRepository, ShirtRepository}
import rest.OrderRest
import service.ManageOrderService
import util.TimestampHelper

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}

@Singleton
class OrderController @Inject()(orderHasShirtRepository: OrderHasShirtRepository, orderRepository: OrderRepository, manageOrderService: ManageOrderService, shirtRepository: ShirtRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def getOrder = ()

  def getAllShirts = Action.async {
    shirtRepository.getAllShirts().map { res => Ok(Json.toJson(res)) }
  }

  def createShirt() = Action.async(parse.json[Shirt]) { req => {
    shirtRepository.create(req.body).map(res => Created(Json.toJson(res)))
  }
  }
//
//  def test = Action.async {
//    orderHasShirtRepository.saveShirtOrder(Seq(1, 2, 3)).map { res => Ok(Json.toJson(res)) }
//  }

  def postOrder = Action(parse.json[OrderRest]) { req => {

    Created(Json.toJson(manageOrderService.insertOrder(req.body) + "replace this"))
//    Created(Json.toJson("localhost:9000/order/" + manageOrderService.insertOrder(req.body)))
  }
  }

  def getExample() = Action {
    Ok(Json.toJson(Order(Some(1), "date", "name", 13)))
  }

  def getAllOrders() = Action.async {
    orderRepository.getAllOrders.map { res => Ok(Json.toJson(res)) }
  }

  def getOrderById(orderId: Int) = Action.async {
    orderRepository.getOrderById(orderId).map { res => Ok(Json.toJson(res)) }
  }
}