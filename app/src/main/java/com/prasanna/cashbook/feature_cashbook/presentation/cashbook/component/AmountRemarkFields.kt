package com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookEvent
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountRemarkFields(viewModel:CashbookViewModel){
    TextField(value = if(viewModel.amount.value == 0.toBigDecimal()){
        ""
    } else viewModel.amount.value.toString() ,
        label = { Text("Enter amount") },
        onValueChange = { amountValue ->
            if(amountValue.isNotBlank()){
                viewModel.onEvent(CashbookEvent.AddAmount(amountValue.toBigDecimal()))
            }else{
                viewModel.onEvent(CashbookEvent.AddAmount(0.toBigDecimal()))
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
    )
    TextField(value = viewModel.remark.value ,
        label = { Text("Enter remark") },
        onValueChange = { remarkValue ->
            viewModel.onEvent(CashbookEvent.AddRemark(remarkValue))
        })
}