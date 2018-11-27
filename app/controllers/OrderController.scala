package controllers

import domain.{Order, OrderHasShirt, Shirt}
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import repository.{OrderHasShirtRepository, OrderRepository, ShirtRepository}
import rest.OrderRest
import service.ManageOrderService

import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class OrderController @Inject()(orderHasShirtRepository: OrderHasShirtRepository, orderRepository: OrderRepository, manageOrderService: ManageOrderService, shirtRepository: ShirtRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def getOrder = ()

  def getAllShirts = Action.async { implicit request =>
    shirtRepository.getAllShirts().map {
      shirts => Created(Json.toJson(shirts))
    }
  }

  def getOrderById(id: Int) = Action.async { implicit request =>
    println("here")
    orderRepository.getOrderById(id).map {
      order => Ok(Json.toJson(order))
    }
  }

  def postOrder = Action.async(parse.json) { implicit request =>
    request.body.validate[OrderRest].map {
      orderRests =>
        orderRepository.create(orderRests) map {
          orderId => Created(Json.obj("id" -> orderId))
        }
    } recoverTotal { t =>
      Future.successful(BadRequest(Json.obj("error" -> "Wrong JSON format")))
    }
  }

  def postShirt() = Action.async(parse.json) { implicit request =>
    request.body.validate[Shirt].map {
      shirt =>
        shirtRepository.create(shirt) map {
          personId => Created(Json.obj("id" -> personId.get))
        }
    } recoverTotal { t =>
      Future.successful(BadRequest(Json.obj("error" -> "Wrong JSON format")))
    }
  }

  def getExample() = Action {
    Ok(Json.toJson(Order(Some(1), "date", "name", 13)))
  }

  def getAllOrders() = Action.async {
    orderRepository.getAllOrders.map { res => Ok(Json.toJson(res)) }
  }

  //  def getOrderById(orderId: Int) = Action.async {
  //    orderRepository.getOrderById(orderId).map { res => Ok(Json.toJson(res)) }
  //  }
  def test = Action.async  { implicit request =>
    orderRepository.findById(60).map(res => Ok(Json.toJson(res)))
  }
}