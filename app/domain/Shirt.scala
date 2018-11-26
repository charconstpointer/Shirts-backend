package domain

import play.api.libs.json.Json

case class Shirt(shirtId: Option[Int]  = None, size: String, color: String, count:Int)

object Shirt {
  implicit val shirtFormat = Json.format[Shirt]
}