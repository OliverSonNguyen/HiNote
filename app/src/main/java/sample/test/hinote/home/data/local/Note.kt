package sample.test.hinote.home.data.local

import java.util.Calendar
import java.util.Date

data class Note(
    val title: String?,
    val content: String?,
    val createdDate: Date = Calendar.getInstance().time,
    val updatedDate: Date = Calendar.getInstance().time
)