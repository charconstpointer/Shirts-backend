package rest

import java.sql.Timestamp

import play.api.libs.json.{JsString, Json, Reads, Writes}

case class OrderRest (name:String, age:Int, date:String, shirts:Seq[Int])


object OrderRest {
  implicit val orderFormat = Json.format[OrderRest]
  implicit val orderReads = Json.reads[OrderRest]
  implicit val orderWrites = Json.writes[OrderRest]
//
//  implicit val tsreads: Reads[Timestamp] = Reads.of[Long] map (new Timestamp(_))
//  implicit val tswrites: Writes[Timestamp] = Writes { (ts: Timestamp) => JsString(ts.toString) }
}
