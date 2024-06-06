package com.prasanna.cashbook.feature_cashbook.domain.use_case

import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import com.prasanna.cashbook.feature_cashbook.domain.repository.TransactionRepository

class AddTransactions(private val repository: TransactionRepository) {
    suspend operator fun invoke(transactionList: List<Transaction>){
        repository.addTransactions(transactionList = transactionList)
    }
}