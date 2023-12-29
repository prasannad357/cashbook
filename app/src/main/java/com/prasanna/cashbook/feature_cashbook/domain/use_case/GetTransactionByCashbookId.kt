package com.prasanna.cashbook.feature_cashbook.domain.use_case

import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import com.prasanna.cashbook.feature_cashbook.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionByCashbookId(private val repository: TransactionRepository) {

    operator fun invoke(cashbookId:Int): Flow<List<Transaction>> {
        return repository.getTransactionByCashbookId(cashbookId)
    }
}