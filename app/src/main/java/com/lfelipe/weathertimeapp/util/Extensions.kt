package com.lfelipe.weathertimeapp.util


fun String.formatToPtBrDate() : String{
    return "${this[8]}${this[9]}/${this[5]}${this[6]}"

}