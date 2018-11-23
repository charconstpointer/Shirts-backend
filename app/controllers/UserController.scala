package controllers

import Repository.{User, UserDAO}
import helpers.Response
import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


@Singleton
class UserController @Inject()(repo: UserDAO, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {
  def addUser = Action.async(parse.json[User]) { req => {
    repo.create(req.body).map({ user =>
      ""
      val res = Response("Successfuly created new user with id: ", user)
      Ok(Json.toJson(res))
    })
  }
  }

  def getAllUsers() = Action.async {
    repo.findAll.map({ users => Ok(Json.toJson(users)) })
  }

  def deleteUser = Action.async(parse.json[User]) { req => {
    repo.delete(req.body.id).map { id => {
      if(id == 0) NotFound("whoopsie") else Ok(Json.toJson(Response("deleted : ", id)))
    }
    }
  }
  }
}
