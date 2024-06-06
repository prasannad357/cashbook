package com.prasanna.cashbook.feature_cashbook.data.repository

import android.util.Log
import com.prasanna.cashbook.feature_cashbook.data.data_source.TransactionDao
import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import com.prasanna.cashbook.feature_cashbook.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class TransactionRepositoryImpl (private val dao:TransactionDao):TransactionRepository {
    private val TAG = "TransactionRepositoryImpl"
    override suspend fun addTransaction(transaction: Transaction) {
        return dao.addTransaction(transaction)
    }

    override suspend fun addTransactions(transactionList: List<Transaction>) {
        return dao.addTransactions(transactionList)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        return dao.deleteTransaction(transaction = transaction)
    }

    override fun getTransactionByCashbookId(cashbookId: Int): Flow<List<Transaction>> {
        Log.d(TAG, "fetching transactions for cashbookId: $cashbookId")
        return dao.getTransactionsByCashbookId(cashbookId)
    }

    override fun getTransactionsInBin(cashbookId: Int):Flow<List<Transaction>>{
        return dao.getTransactionsInBin(cashbookId)
    }
}