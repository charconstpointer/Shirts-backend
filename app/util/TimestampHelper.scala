package util

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.{ Singleton}

object TimestampHelper {
  def getCurrentdateTimeStamp: Timestamp ={
    val today:java.util.Date = Calendar.getInstance.getTime
    val timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val now:String = timeFormat.format(today)
    val re = java.sql.Timestamp.valueOf(now)
    re
  }
}
