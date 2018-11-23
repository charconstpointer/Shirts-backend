package helpers

import play.api.libs.json.Json

case class Response(message: String, id:Long)


object Response {
  implicit val format = Json.format[Response]
  implicit val write = Json.writes[Response]
  implicit val read = Json.reads[Response]
}
