package domain

trait Tables {
  protected val driver: slick.driver.JdbcProfile

  import driver.api._

  import slick.model.ForeignKeyAction
  import slick.jdbc.{GetResult => GR}

  class OrderTable(_tableTag: Tag) extends Table[Order](_tableTag, "order") {
    def * = (orderId, date, clientName, clientAge) <> ((Order.apply _).tupled, Order.unapply)

    def ? = (Rep.Some(orderId), Rep.Some(date), Rep.Some(clientName), Rep.Some(clientAge)).shaped.<>({ r => import r._; _1.map(_ => (Order.apply _).tupled((_1.get, _2.get, _3.get, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    val orderId: Rep[Option[Int]] = column[Int]("order_id", O.AutoInc, O.PrimaryKey)
    val date: Rep[String] = column[String]("date")
    val clientName: Rep[String] = column[String]("client_name", O.Length(255, varying = true))
    val clientAge: Rep[Int] = column[Int]("client_age")
  }

  val orders = TableQuery[OrderTable]

  class ShirtTable(_tableTag: Tag) extends Table[Shirt](_tableTag, "shirt") {
    def * = (shirtId, shirtSize, shirtColor, shirtCount) <> ((Shirt.apply _).tupled, Shirt.unapply)

    def ? = (Rep.Some(shirtId), Rep.Some(shirtSize), Rep.Some(shirtColor), Rep.Some(shirtCount)).shaped.<>({ r => import r._; _1.map(_ => (Shirt.apply _).tupled((_1.get, _2.get, _3.get, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    val shirtId: Rep[Option[Int]] = column[Int]("shirt_id", O.PrimaryKey, O.AutoInc)
    val shirtSize: Rep[String] = column[String]("size", O.Length(255, varying = true))
    val shirtColor: Rep[String] = column[String]("color", O.Length(255, varying = true))
    val shirtCount: Rep[Int] = column[Int]("count")
  }

  val shirts = TableQuery[ShirtTable]

  class OrderHasShirtTable(_tableTag: Tag) extends Table[OrderHasShirt](_tableTag, "order_has_shirt") {
    def * =
      (orderHasShirtId, shirtId, orderId) <> ((OrderHasShirt.apply _).tupled, OrderHasShirt.unapply)

    def ? = (Rep.Some(orderHasShirtId), Rep.Some(shirtId), Rep.Some(orderId)).shaped.<>({ r => import r._; _1.map(_ => OrderHasShirt.tupled((_1.get, _2.get, _3.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    val orderHasShirtId: Rep[Option[Int]] = column[Int]("order_has_shirt_id", O.AutoInc, O.PrimaryKey)
    val shirtId: Rep[Int] = column[Int]("shirt_id")
    val orderId: Rep[Int] = column[Int]("order_id")
    lazy val orderFk = foreignKey("order_has_shirt_order", orderId, orders)(r => r.orderId.get, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    /** Foreign key referencing Shirt (database name order_has_shirt_shirt) */
    lazy val shirtFk = foreignKey("order_has_shirt_shirt", shirtId, shirts)(r => r.shirtId.get, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  val ordersShirts = TableQuery[OrderHasShirtTable]
}
