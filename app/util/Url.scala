package util

import domain.Order
import play.api.libs.json.Json

case class Url(url: String, id: Int)

object Order {
  implicit val orderFormat = Json.format[Order]
}