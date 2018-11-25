package controllers

import domain.{Client, Course}
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import repository.{ClientRepository, CourseRepository}

import scala.concurrent.ExecutionContext

@Singleton
class CourseController @Inject()(courseRepo: CourseRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {
  def addCourse = Action.async(parse.json[Course]) { req => {
    courseRepo.create(req.body).map({ course =>
      println(req.body)
      Ok(Json.toJson(course))
    })
  }
  }


  def createMany() = Action.async(parse.json[Seq[Course]]) { req => {
    courseRepo.createMany(req.body).map { res => Ok(Json.toJson(res))}
  }}

  def getCourse(courseId: Int) = Action.async { req => {
    courseRepo.findById(courseId).map { course => Ok(Json.toJson(course)) }
  }
  }
}
