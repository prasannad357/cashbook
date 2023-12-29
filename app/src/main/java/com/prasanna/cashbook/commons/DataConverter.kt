package com.prasanna.cashbook.commons

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import java.lang.reflect.Type
import java.math.BigDecimal
import java.time.LocalDate

class DataConverter {
    private val TAG = "DataConverter"
    @TypeConverter
    fun fromListToString(list: List<*>): String {
        val type = object: TypeToken<List<*>>() {}.type
        return Gson().toJson(list, type)
    }

    @TypeConverter
    fun toStringList(dataString: String?): List<String> {
        if(dataString.isNullOrEmpty()) {
            return mutableListOf()
        }
        val type: Type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(dataString, type)
    }

    @TypeConverter
    fun toTransactionList(dataString: String?): List<Transaction> {
        if(dataString.isNullOrEmpty()) {
            Log.d(TAG, "fun toTransactionList: Returning empty list")
            return mutableListOf()
        }
        val type: Type = object : TypeToken<List<Transaction>>() {}.type
        val transactionList:List<Transaction> = Gson().fromJson(dataString, type)
        Log.d(TAG, "fun toTransactionList: $transactionList")
        return transactionList
    }

    @TypeConverter
    fun fromBigDecimal(bd:BigDecimal):String{
        return bd.toString()
    }

    @TypeConverter
    fun toBigDecimal(bdString:String):BigDecimal{
        return bdString.toBigDecimal()
    }
}