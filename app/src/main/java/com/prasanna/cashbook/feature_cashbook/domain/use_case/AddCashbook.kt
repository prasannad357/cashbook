package com.prasanna.cashbook.feature_cashbook.domain.use_case

import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook
import com.prasanna.cashbook.feature_cashbook.domain.model.InvalidCashbookException
import com.prasanna.cashbook.feature_cashbook.domain.repository.CashbookRepository

class AddCashbook(private val repository: CashbookRepository) {

    @Throws(InvalidCashbookException::class)
    suspend operator fun invoke(cashbook: Cashbook){
        if(cashbook.name.isEmpty()){
            throw InvalidCashbookException("Cashbook name is empty")
        }
        repository.addCashbook(cashbook)

    }
}