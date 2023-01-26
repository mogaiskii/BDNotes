package com.example.awesomeapps

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class NoteDateFormatter(
    private val noteDate: Date
) {
    val dateFormatter =  DateTimeFormatter.ofPattern("dd LLL yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private fun dateToLocalDate(): LocalDateTime {
        return noteDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    val formattedDate: String
        get() {
            return dateToLocalDate().format(dateFormatter)
        }

    val formattedTime: String
        get() {
            return dateToLocalDate().format(timeFormatter)
        }

    val formattedDateTime: String
        get() {
            return "$formattedDate $formattedTime"
        }

    fun formatDateTimeWithText(text: String): String {
        return formattedDateTime + "\n\n" + text
    }
}