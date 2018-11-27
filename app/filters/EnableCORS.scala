//package filters
//
//import play.api.mvc._
//import play.mvc.Http.HeaderNames
//
//import scala.concurrent.Future
//
//object EnableCORS extends Filter {
//  implicit def ec = play.api.libs.concurrent.Akka.system(play.api.Play.current).dispatcher
//
//  def apply(f: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {
//    val result = f(rh)
//    result.map(_.withHeaders(HeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN -> "*"))
//  }
//}