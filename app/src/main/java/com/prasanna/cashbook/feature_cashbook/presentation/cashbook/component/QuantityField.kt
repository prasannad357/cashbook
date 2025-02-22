package com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookEvent
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookViewModel
import com.prasanna.cashbook.feature_cashbook.presentation.util.Constants.EMPTY
import com.prasanna.cashbook.feature_cashbook.presentation.util.Constants.ZERO_INT

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuantityField(viewModel: CashbookViewModel = hiltViewModel()){
    Log.d("QuantityField", "cashbookViewModel instance: $viewModel")
    val quantity = viewModel.quantity.value
    TextField(value = if(quantity != ZERO_INT){
        quantity.toString()
    }else EMPTY,
        label = { Text("Enter quantity") },
        onValueChange = { qty ->
            if(qty.isNotBlank()){
                viewModel.onEvent(CashbookEvent.AddQuantity(qty.toInt()))
            }else{
                viewModel.onEvent(CashbookEvent.AddQuantity(ZERO_INT))
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
}