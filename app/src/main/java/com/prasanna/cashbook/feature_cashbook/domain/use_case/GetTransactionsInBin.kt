package com.prasanna.cashbook.feature_cashbook.domain.use_case

import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import com.prasanna.cashbook.feature_cashbook.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionsInBin(private val transactionRepository: TransactionRepository) {

    operator fun invoke(cashbookId:Int): Flow<List<Transaction>> {
        return transactionRepository.getTransactionsInBin(cashbookId)
    }
}