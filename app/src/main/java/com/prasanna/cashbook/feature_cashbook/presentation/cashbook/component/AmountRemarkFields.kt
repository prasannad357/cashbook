package com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookEvent
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookViewModel
import com.prasanna.cashbook.feature_cashbook.presentation.util.Constants.EIGHT_DP
import com.prasanna.cashbook.feature_cashbook.presentation.util.Constants.ONE_DP
import com.prasanna.cashbook.feature_cashbook.presentation.util.Constants.TWO_DP

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountRemarkFields(viewModel:CashbookViewModel){
    TextField(value = if(viewModel.amount.value == 0.toBigDecimal()){
        ""
    } else viewModel.amount.value.toString() ,
        label = { Text("Enter unit amount") },
        onValueChange = { amountValue ->
            if(amountValue.isNotBlank()){
                viewModel.onEvent(CashbookEvent.AddAmount(amountValue.toBigDecimal()))
            }else{
                viewModel.onEvent(CashbookEvent.AddAmount(0.toBigDecimal()))
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
    )
    Spacer(modifier = Modifier.height(ONE_DP))
    TextField(value = viewModel.remark.value ,
        label = { Text("Enter remark") },
        onValueChange = { remarkValue ->
            viewModel.onEvent(CashbookEvent.AddRemark(remarkValue))
        })
}