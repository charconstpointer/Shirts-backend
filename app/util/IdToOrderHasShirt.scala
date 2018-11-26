package util

import domain.OrderHasShirt

object IdToOrderHasShirt {
  def convert(ids: Seq[Int], orderId: Int): Seq[OrderHasShirt] = {
    for {
      id <- ids
    } yield OrderHasShirt(None, orderId, id)
  }
}
