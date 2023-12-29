package com.prasanna.cashbook.feature_cashbook.presentation.util

sealed class CashbookOrder(val orderType: OrderType){
    class Name(orderType: OrderType):CashbookOrder(orderType)
    class CreatedDate(orderType: OrderType):CashbookOrder(orderType)
    class LastEdit(orderType: OrderType):CashbookOrder(orderType)

    fun copy(orderType: OrderType):CashbookOrder{
        return when(this){
            is Name -> {
                Name(orderType)
            }

            is CreatedDate -> {
                CreatedDate(orderType)
            }

            is LastEdit -> {
                LastEdit(orderType)
            }
        }
    }
}
