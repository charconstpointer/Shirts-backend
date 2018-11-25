package repository

import domain.Course
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.Result
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CourseRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  // We want the JdbcProfile for this provider
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CourseTable(_tableTag: Tag) extends Table[Course](_tableTag, "course") {
    def * = (courseId, name) <> ((Course.apply _).tupled, Course.unapply)

    def ? = (Rep.Some(courseId), Rep.Some(name)).shaped.<>({ r => import r._; _1.map(_ => (Course.apply _).tupled((_1.get, _2.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    val courseId: Rep[Int] = column[Int]("course_id", O.PrimaryKey)
    val name: Rep[String] = column[String]("name", O.Length(255, varying = true))
  }

  private val courses = TableQuery[CourseTable]

  def create(course: Course) = db.run {
    courses += course
  }

  def findById(courseId: Int): Future[Course] = db.run {
    courses.filter(_.courseId === courseId).result.head
  }

  def createMany(courseList:Seq[Course]) = db.run{
    courses ++= courseList
  }

  def list(): Future[Seq[Course]] = db.run {
    courses.result
  }
}
