package com.prasanna.cashbook.feature_cashbook.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.prasanna.cashbook.commons.DataConverter
import com.prasanna.cashbook.commons.LocalDateConverter
import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook
import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction

@Database(
    entities = [Cashbook::class, Transaction::class],
    version = 2
)
@TypeConverters(DataConverter::class, LocalDateConverter::class)
abstract class CashbookDatabase:RoomDatabase() {
    abstract val cashbookDao:CashbookDao
    abstract val transactionDao:TransactionDao

    companion object{
        const val CASHBOOK_DB_NAME = "CashbookDb"
    }
}