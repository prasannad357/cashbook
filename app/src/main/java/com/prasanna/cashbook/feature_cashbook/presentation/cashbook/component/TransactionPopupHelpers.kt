package com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.annotation.RequiresApi
import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookEvent
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun initiateViewModel(viewModel: CashbookViewModel, transaction: Transaction? = null){
    if(transaction == null){
        if(viewModel.isCredit.value){
            viewModel.onEvent(CashbookEvent.ToggleCreditDebit)
        }
        viewModel.onEvent(CashbookEvent.AddDate(LocalDate.now()))
        viewModel.onEvent(CashbookEvent.AddAmount(0.toBigDecimal()))
        viewModel.onEvent(CashbookEvent.AddRemark(""))
        viewModel.onEvent(CashbookEvent.AddTags(emptyList()))
    }else{
        viewModel.onEvent(CashbookEvent.AddTags(transaction.tags))
        if(transaction.isCredit != viewModel.isCredit.value){
            viewModel.onEvent(CashbookEvent.ToggleCreditDebit)
        }
        viewModel.onEvent(CashbookEvent.AddAmount(transaction.amount))
        viewModel.onEvent(CashbookEvent.AddDate(transaction.date))
        viewModel.onEvent(CashbookEvent.AddRemark(transaction.remark))
    }
}
fun addSuffix(day:Int): Spanned {
    if(day == 11 || day == 12 || day == 13){
        return Html.fromHtml("${day}<sup>th<sup>", Html.FROM_HTML_MODE_LEGACY)
    }
    return when(day%10){
        1 -> Html.fromHtml("${day}<sup>st<sup>", Html.FROM_HTML_MODE_LEGACY)
        2 -> Html.fromHtml("${day}<sup>nd<sup>", Html.FROM_HTML_MODE_LEGACY)
        3 -> Html.fromHtml("${day}<sup>rd<sup>", Html.FROM_HTML_MODE_LEGACY)
        else -> Html.fromHtml("${day}<sup>th<sup>", Html.FROM_HTML_MODE_LEGACY)
    }
}