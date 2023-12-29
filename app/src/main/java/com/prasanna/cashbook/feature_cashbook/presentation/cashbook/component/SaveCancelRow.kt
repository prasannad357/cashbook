package com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookEvent
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SaveCancelRow(viewModel: CashbookViewModel, transaction:Transaction? = null){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
    {
        if(viewModel.editTransactionPopupShown.value != null){
            Button(onClick = {
                viewModel.onEvent(CashbookEvent.ToggleEditTransactionPopup(null))
            }) {
                Text("Cancel")
            }

            Button(onClick = {
                viewModel.onEvent(CashbookEvent.ToggleEditTransactionPopup(null))
                viewModel.onEvent(CashbookEvent.AddTransaction(transaction?.id))
            }) {
                Text("Save")
            }
        }
        if(viewModel.addTransactionPopupShown.value){
            Button(onClick = {
                viewModel.onEvent(CashbookEvent.ToggleAddTransactionPopup)
            }) {
                Text("Cancel")
            }

            Button(onClick = {
                viewModel.onEvent(CashbookEvent.ToggleAddTransactionPopup)
                viewModel.onEvent(CashbookEvent.AddTransaction())
            }) {
                Text("Add")
            }
        }
    }
}