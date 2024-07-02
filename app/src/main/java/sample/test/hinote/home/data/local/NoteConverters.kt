package sample.test.hinote.home.data.local

import androidx.room.TypeConverter
import java.util.Date

class NoteConverters {
    @TypeConverter
    fun fromLongToDate(long: Long? = null): Date? {
        return long?.let { Date(it) }
    }

    @TypeConverter
    fun fromDateToLong(date: Date? = null): Long? {
        return date?.time
    }
}