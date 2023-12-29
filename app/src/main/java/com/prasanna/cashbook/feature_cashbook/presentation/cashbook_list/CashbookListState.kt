package com.prasanna.cashbook.feature_cashbook.presentation.cashbook_list

import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook
import com.prasanna.cashbook.feature_cashbook.presentation.util.CashbookOrder
import com.prasanna.cashbook.feature_cashbook.presentation.util.OrderType

data class CashbookListState(
    val cashbooks:List<Cashbook> = emptyList(),
    val cashbookOrder:CashbookOrder = CashbookOrder.LastEdit(OrderType.Descending),
    val isOrderSelectionVisible:Boolean = false,
    val isAddCashbookPopupVisible:Boolean = false,
    val isCompareMultiSelectActive:Boolean = false,
    val selectedCashbooks:List<Int> = emptyList()
)
