package com.prasanna.cashbook.feature_cashbook.presentation.cashbook

import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import java.math.BigDecimal
import java.time.LocalDate


sealed class CashbookEvent {
    data class AddDate(val date: LocalDate):CashbookEvent()
    data class AddAmount(val amount:BigDecimal):CashbookEvent()
    data class AddQuantity(val qty: Int):CashbookEvent()
    data class AddTags(val tags:List<String>):CashbookEvent() // Expense tags
    data class AddRemark(val remark:String):CashbookEvent()
    data class EditCashbookName(val name:String):CashbookEvent()
    data class EditCashbookTags(val tags:List<String>):CashbookEvent()
    data class UpdateBalance(val amount:BigDecimal, val isCredit:Boolean):CashbookEvent()
    data class DeleteTransaction(val transaction:Transaction):CashbookEvent()
    data class AddTransaction(val transactionId:Int? = null):CashbookEvent()
    data class ToggleEditTransactionPopup(val transaction:Transaction?):CashbookEvent()
    data class RestoreTransaction(val transaction: Transaction):CashbookEvent()
    data class PermanentlyDeleteTransaction(val transaction: Transaction):CashbookEvent()
    data class ToggleDeleteTopBar(val transactionList:List<Transaction>):CashbookEvent()

    object AddRepeatTransactions:CashbookEvent()
    object PermanentlyDeleteAllTransactions:CashbookEvent()
    object SaveCashbook:CashbookEvent()
    object ToggleCreditDebit:CashbookEvent()
    object ToggleAddTransactionPopup:CashbookEvent()
    object ToggleRepeatTransactionPopup:CashbookEvent()
}