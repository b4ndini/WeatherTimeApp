package com.lfelipe.weathertimeapp.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


fun String.formatToPtBrDate() : String{
    return "${this[8]}${this[9]}/${this[5]}${this[6]}"

}

fun String.formatHour() : String{
    return this.substring(11,16)
}

//@RequiresApi(Build.VERSION_CODES.O)
fun String.getDayOfWeekInText(locale: Locale?, dateFormat: DateTimeFormatter? = null): String? {
/*//    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return if (format == null) {
        val dateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val day: DayOfWeek = dateTime.dayOfWeek
        day.getDisplayName(TextStyle.SHORT, locale)
    } else {*/
    val date = LocalDate.parse(this, dateFormat)
    val day: DayOfWeek = date.dayOfWeek
    return day.getDisplayName(TextStyle.SHORT, locale)
}

