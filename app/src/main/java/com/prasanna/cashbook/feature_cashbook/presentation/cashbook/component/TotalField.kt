package com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookViewModel
import com.prasanna.cashbook.feature_cashbook.presentation.util.Constants.EIGHT_DP

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TotalField(viewModel: CashbookViewModel = hiltViewModel()){
    val total = viewModel.amount.value.multiply(viewModel.quantity.value.toBigDecimal())
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(EIGHT_DP),
        color = MaterialTheme.colorScheme.onPrimary,
        text = "Total : $total"

    )
}