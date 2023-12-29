package com.prasanna.cashbook.feature_cashbook.domain.use_case

import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook
import com.prasanna.cashbook.feature_cashbook.domain.repository.CashbookRepository
import com.prasanna.cashbook.feature_cashbook.presentation.util.CashbookOrder
import com.prasanna.cashbook.feature_cashbook.presentation.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCashbooks(private val repository: CashbookRepository) {
    operator fun invoke(
        cashbookOrder: CashbookOrder = CashbookOrder.LastEdit(OrderType.Descending))
    : Flow<List<Cashbook>> {
        return repository.getCashbooks().map {
            cashbooks ->
            when(cashbookOrder.orderType){
                is OrderType.Ascending ->{
                   when(cashbookOrder){
                       is CashbookOrder.LastEdit -> cashbooks.sortedBy { it.lastEditTimeStamp }
                       is CashbookOrder.Name -> cashbooks.sortedBy { it.name }
                       is CashbookOrder.CreatedDate -> cashbooks.sortedBy { it.createdDate }
                   }
                }

                is OrderType.Descending ->{
                    when(cashbookOrder){
                        is CashbookOrder.LastEdit -> cashbooks.sortedByDescending { it.lastEditTimeStamp }
                        is CashbookOrder.Name -> cashbooks.sortedByDescending { it.name }
                        is CashbookOrder.CreatedDate -> cashbooks.sortedByDescending { it.createdDate }
                    }
                }
            }
        }
    }
}