package controllers

import domain.Client
import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._
import repository.{ClientRepository, CourseRepository}

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ClientController @Inject()(clientRepo: ClientRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {
  def addClient = Action.async(parse.json[Client]) { req => {
    clientRepo.create(req.body).map({ client =>
      Ok(Json.toJson(client))
    })
  }
  }

  def getClient(clientId: Int) = Action.async { req => {
    clientRepo.findById(clientId).map { client => Ok(Json.toJson(client)) }
  }
  }
  
}
