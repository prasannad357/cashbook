package com.prasanna.cashbook.commons

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLocalDateToString(localDate: LocalDate): String {
        Log.d("TAG", "converting Local Date to String: $localDate")
        return localDate.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(dataString: String): LocalDate {
        Log.d("TAG", "converting String to Local Date: ${LocalDate.parse(dataString)}")
        return LocalDate.parse(dataString)
    }
}