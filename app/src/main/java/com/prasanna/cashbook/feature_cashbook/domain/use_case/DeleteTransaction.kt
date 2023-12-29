package com.prasanna.cashbook.feature_cashbook.domain.use_case

import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import com.prasanna.cashbook.feature_cashbook.domain.repository.TransactionRepository

class DeleteTransaction(private val repository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction){
        repository.deleteTransaction(transaction = transaction)
    }
}