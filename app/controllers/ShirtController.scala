package controllers

import domain.Shirt
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import repository.{OrderRepository, ShirtRepository}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ShirtController @Inject()(shirtRepository: ShirtRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {
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

  def getShirt(id: Int) = Action.async {
    shirtRepository.findById(id).map { shirt => Ok(Json.toJson(shirt)) }
  }

  def getAllShirts = Action.async { implicit request =>
    shirtRepository.getAllShirts().map {
      shirts => Created(Json.toJson(shirts))
    }
  }

  def deleteShirt(id: Int) = Action.async {
    shirtRepository.delete(id).map { res => Ok(Json.obj("deleted" -> res)) }
  }

  def updateShirt(id: Int) = Action.async(parse.json) { implicit request =>
    request.body.validate[Shirt].map {
      shirt =>
        shirtRepository.update(id, shirt).map {
          id => Ok(Json.obj("updated" -> id))
        }
    } recoverTotal { t => {
      Future.successful(BadRequest(Json.obj("error" -> "Wrong JSON format")))
    }
    }
  }
}