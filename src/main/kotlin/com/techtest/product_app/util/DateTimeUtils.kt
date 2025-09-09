package com.techtest.product_app.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtils {
    
    fun parseIsoDateTime(dateTimeString: String?): LocalDateTime {
        return if (dateTimeString != null && dateTimeString.isNotBlank()) {
            try {
                LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
            } catch (e: Exception) {
                LocalDateTime.now()
            }
        } else {
            LocalDateTime.now()
        }
    }
}