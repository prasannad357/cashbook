package com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookEvent
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditDebitChip(viewModel:CashbookViewModel, transaction:Transaction? = null){
    Row(modifier = Modifier
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row {
            FilterChip(selected = viewModel.isCredit.value,
                onClick = { viewModel.onEvent(CashbookEvent.ToggleCreditDebit) },
                label = {
                    if(!viewModel.isCredit.value){
                        Text("Credit",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }else{
                        Text("Credit",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                })
            Spacer(modifier = Modifier.width(10.dp))
            FilterChip(selected = !viewModel.isCredit.value,
                onClick = { viewModel.onEvent(CashbookEvent.ToggleCreditDebit) },
                label = {
                    if(viewModel.isCredit.value){
                        Text("Debit",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }else{
                        Text("Debit",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                },
            )
        }

        transaction?.let {
            IconButton(onClick = {
                viewModel.onEvent(CashbookEvent.DeleteTransaction(it))
                viewModel.onEvent(CashbookEvent.ToggleEditTransactionPopup(null))
            }) {
                Icon(imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete transaction",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}