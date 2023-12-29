package com.prasanna.cashbook.feature_cashbook.domain.repository

import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook
import kotlinx.coroutines.flow.Flow

interface CashbookRepository {

    fun getCashbooks(): Flow<List<Cashbook>>

    suspend fun getCashbookById(id:Int):Cashbook

    suspend fun addCashbook(cashbook: Cashbook)

    suspend fun deleteCashbook(cashbook: Cashbook)
}