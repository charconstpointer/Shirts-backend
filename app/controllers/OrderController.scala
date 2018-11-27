package controllers

import domain.{Order, Shirt}
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import repository.{OrderRepository, ShirtRepository}
import rest.OrderRest

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderController @Inject()(orderRepository: OrderRepository, shirtRepository: ShirtRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def getOrder = ()

  def getAllShirts = Action.async { implicit request =>
    shirtRepository.getAllShirts().map {
      shirts => Created(Json.toJson(shirts))
    }
  }

  def getOrderById(id: Int) = Action.async { implicit request =>
    orderRepository.findById(id).map(res => Ok(Json.toJson(res)))
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
    orderRepository.findAllOrders.map { res => Ok(Json.toJson(res)) }
  }
}