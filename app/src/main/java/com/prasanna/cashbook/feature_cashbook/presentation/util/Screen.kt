package com.prasanna.cashbook.feature_cashbook.presentation.util

sealed class Screen(val route:String){
    object CashbookScreen:Screen("cashbook_screen")
    object CashbookListScreen:Screen("cashbooklist_screen")
    object CompareScreen:Screen("compare_screen")
}
