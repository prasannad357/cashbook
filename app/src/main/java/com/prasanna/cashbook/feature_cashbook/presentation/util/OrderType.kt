package com.prasanna.cashbook.feature_cashbook.presentation.util

sealed class OrderType{
    object Ascending:OrderType()
    object Descending:OrderType()
}
