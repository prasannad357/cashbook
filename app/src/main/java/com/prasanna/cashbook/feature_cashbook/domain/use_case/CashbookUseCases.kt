package com.prasanna.cashbook.feature_cashbook.domain.use_case

data class CashbookUseCases(
    val addCashbook: AddCashbook,
    val deleteCashbook: DeleteCashbook,
    val getCashbookById: GetCashbookById,
    val getCashbooks: GetCashbooks,
    val addTransaction: AddTransaction,
    val deleteTransaction: DeleteTransaction,
    val getTransactionByCashbookId: GetTransactionByCashbookId,
    val getTransactionsInBin: GetTransactionsInBin,
    val addTransactions: AddTransactions
)
