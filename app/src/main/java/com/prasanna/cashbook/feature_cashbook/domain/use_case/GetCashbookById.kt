package com.prasanna.cashbook.feature_cashbook.domain.use_case

import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook
import com.prasanna.cashbook.feature_cashbook.domain.repository.CashbookRepository

class GetCashbookById(private val repository: CashbookRepository) {
    suspend operator fun invoke(id:Int):Cashbook{
        return repository.getCashbookById(id)
    }
}