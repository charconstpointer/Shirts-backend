package domain

case class OrderHasShirt(orderHasShirtId: Option[Int] = None, orderId: Int, shirtId: Int)