package com.prasanna.cashbook.feature_cashbook.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook
import kotlinx.coroutines.flow.Flow

@Dao
interface CashbookDao {

    @Query("SELECT * FROM cashbook")
    fun getCashbooks(): Flow<List<Cashbook>>

    @Query("SELECT * FROM cashbook WHERE id=:id")
    fun getCashbookById(id:Int):Cashbook

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCashbook(cashbook: Cashbook)

    @Delete
    fun deleteCashbook(cashbook: Cashbook)
}