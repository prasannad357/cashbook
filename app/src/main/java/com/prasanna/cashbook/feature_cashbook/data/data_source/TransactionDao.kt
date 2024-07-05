package com.prasanna.cashbook.feature_cashbook.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTransaction(transaction: Transaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTransactions(transactionList: List<Transaction>)

    @Delete
    fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM `transaction` WHERE cashbookId=:cashbookId AND inBin=:inBin")
    fun getTransactionsByCashbookId(cashbookId:Int, inBin:Boolean = false): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE cashbookId=:cashbookId AND inBin=:inBin")
    fun getTransactionsInBin(cashbookId:Int, inBin:Boolean = true):Flow<List<Transaction>>
}