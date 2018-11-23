package controllers

import Repository.{User, UserDAO}
import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext


@Singleton
class UserController @Inject()(repo:UserDAO, cc: ControllerComponents) (implicit exec: ExecutionContext) extends AbstractController(cc) {
  def addUser = Action.async(parse.json[User]) { req => {
    repo.create(req.body).map({ user =>
      Ok(Json.toJson(user))
    })
  }
  }

  def getAllUsers() = Action.async {
    repo.findAll.map({users=>Ok(Json.toJson(users))})
  }

  def deleteUser() = Action.async(parse.json[User]) { req => {
    repo.delete(req.body.id).map { id => {
      Ok(Json.toJson("Deleted" + req.body))
    }
    }
  }
  }
}
