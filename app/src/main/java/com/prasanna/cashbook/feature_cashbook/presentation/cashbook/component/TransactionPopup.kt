package com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookEvent
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TransactionPopup(viewModel: CashbookViewModel, transaction: Transaction? = null ){
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = {
            if(viewModel.addTransactionPopupShown.value){
                viewModel.onEvent(CashbookEvent.ToggleAddTransactionPopup)
            }
            if(viewModel.editTransactionPopupShown.value != null){
                viewModel.onEvent(CashbookEvent.ToggleEditTransactionPopup(null))
            }
        },
        properties = PopupProperties(focusable = true),
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxWidth(0.8f)
                .padding(10.dp)
        ) {
            LaunchedEffect(key1 = Unit){
                initiateViewModel(viewModel, transaction)
            }
            CreditDebitChip(viewModel = viewModel, transaction = transaction)
            DateRow(viewModel = viewModel)
            AmountRemarkFields(viewModel = viewModel)
            TagsRow(viewModel = viewModel)
            SaveCancelRow(viewModel = viewModel, transaction = transaction)
        }

    }

}
