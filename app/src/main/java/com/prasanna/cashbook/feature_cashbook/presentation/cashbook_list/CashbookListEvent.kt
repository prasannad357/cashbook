package com.prasanna.cashbook.feature_cashbook.presentation.cashbook_list

import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook
import com.prasanna.cashbook.feature_cashbook.presentation.util.CashbookOrder

sealed class CashbookListEvent {
    data class Order(val cashbookOrder:CashbookOrder):CashbookListEvent()
    data class DeleteCashbook(val cashbook: Cashbook):CashbookListEvent()
    data class AddCashbookName(val name:String):CashbookListEvent()
    data class AddCashbookTags(val tags:List<String>):CashbookListEvent()
    data class SelectedCashbooks(val cashbookIds:List<Int>):CashbookListEvent()
    object AddCashbook:CashbookListEvent()
    object RestoreCashbook:CashbookListEvent()
    object ToggleOrderSection:CashbookListEvent()
    object ToggleAddCashbookPopup:CashbookListEvent()
    object ToggleCompareMultiSelect:CashbookListEvent()
}