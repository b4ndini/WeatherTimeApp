package com.lfelipe.weathertimeapp.util


fun String.formatToPtBrDate() : String{
    return "${this[8]}${this[9]}/${this[5]}${this[6]}"

}

fun String.formatHour() : String{
    return this.substring(11,16)
}