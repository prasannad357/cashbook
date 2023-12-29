package com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookEvent
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRow(viewModel:CashbookViewModel){
    var showDatePicker by remember { mutableStateOf(false) }
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { showDatePicker = true }
        .background(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
        )
        .padding(15.dp)) {
        val month = viewModel.date.value.month.toString().lowercase().replaceFirstChar { char -> char.uppercase() }
        val day = addSuffix(viewModel.date.value.dayOfMonth)

        Text(text = "Date: ", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = "$day $month ${viewModel.date.value.year}",
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    if(showDatePicker){
        DatePicker(onDateSelected = {
            viewModel.onEvent(CashbookEvent.AddDate(it))
        })
        showDatePicker = false
    }
    Spacer(modifier = Modifier.height(1.dp))
}