package com.prasanna.cashbook.feature_cashbook.domain.repository

import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun addTransaction(transaction: Transaction)
    suspend fun addTransactions(transactionList: List<Transaction>)

    suspend fun deleteTransaction(transaction: Transaction)

    fun getTransactionByCashbookId(cashbookId:Int): Flow<List<Transaction>>

    fun getTransactionsInBin(cashbookId: Int):Flow<List<Transaction>>
}