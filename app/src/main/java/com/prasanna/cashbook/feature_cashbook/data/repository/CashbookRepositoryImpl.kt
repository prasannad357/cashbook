package com.prasanna.cashbook.feature_cashbook.data.repository

import com.prasanna.cashbook.feature_cashbook.data.data_source.CashbookDao
import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook
import com.prasanna.cashbook.feature_cashbook.domain.repository.CashbookRepository
import kotlinx.coroutines.flow.Flow

class CashbookRepositoryImpl(private val dao: CashbookDao):CashbookRepository {
    override fun getCashbooks(): Flow<List<Cashbook>> {
        return dao.getCashbooks()
    }

    override suspend fun getCashbookById(id: Int): Cashbook {
        return dao.getCashbookById(id)
    }

    override suspend fun addCashbook(cashbook: Cashbook) {
        return dao.addCashbook(cashbook = cashbook)
    }

    override suspend fun deleteCashbook(cashbook: Cashbook) {
        dao.deleteCashbook(cashbook = cashbook)
    }
}