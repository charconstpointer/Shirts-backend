package domain

import java.sql.Timestamp

import play.api.libs.json.{JsString, Json, Reads, Writes}

case class Order(orderId: Option[Int] = None, date: String, clientName: String, clientAge: Int)

object Order {
  implicit val orderFormat = Json.writes[Order]
  implicit val orderRead = Json.reads[Order]
//  implicit val tsreads: Reads[Timestamp] = Reads.of[Long] map (new Timestamp(_))
//  implicit val tswrites: Writes[Timestamp] = Writes { (ts: Timestamp) => JsString(ts.toString) }
}