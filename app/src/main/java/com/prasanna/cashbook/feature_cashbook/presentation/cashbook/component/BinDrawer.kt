package com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookEvent
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BinDrawer(viewModel:CashbookViewModel, onCloseDrawer:()->Unit){
    //TODO: Get the list of transactions that are trashed
    //TODO: Show only remark, Cr/Dr amount, restore button
    LazyColumn(modifier = Modifier
        .padding(20.dp)
        .background(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(16.dp)
        )
        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        .fillMaxWidth(0.9f)
        .fillMaxHeight()
    ) {
        item {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "Bin",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(8f)
                )

                IconButton(onClick = onCloseDrawer) {
                    Icon(imageVector = Icons.Default.Close, tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        contentDescription = "Close Drawer",
                        modifier = Modifier.weight(2f)
                        )
                }
            }
        }

        items(items = viewModel.transactionsInBin.value,
            key = {transaction -> transaction.id!!}
            ){ transaction ->
            Log.d("BinDrawer", "Transaction: $transaction")
            val color = if(transaction.isCredit) MaterialTheme.colorScheme.tertiary
            else MaterialTheme.colorScheme.error
            val textColor = if(transaction.isCredit) MaterialTheme.colorScheme.onTertiary
            else MaterialTheme.colorScheme.onError
            Row(modifier = Modifier
                .animateItemPlacement(tween(250))
                .fillMaxWidth()
                .background(color = color, shape = RoundedCornerShape(16.dp))
                .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = transaction.remark, color = textColor
                    , modifier = Modifier.weight(0.5f)
                )
                Text(text = transaction.amount.toString(), color = textColor,
                    modifier = Modifier.weight(0.3f))
                IconButton(onClick = {viewModel
                    .onEvent(CashbookEvent.RestoreTransaction(transaction)) }) {
                    Icon(imageVector = Icons.Default.Restore,
                        contentDescription = "Restore the transaction",
                        tint = textColor,
                        modifier = Modifier.weight(0.1f)
                    )
                }

                IconButton(onClick = {viewModel
                    .onEvent(CashbookEvent.PermanentlyDeleteTransaction(transaction)) }) {
                    Icon(imageVector = Icons.Default.Delete,
                        contentDescription = "Permanently delete the transaction",
                        tint = textColor,
                        modifier = Modifier.weight(0.1f)

                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

    }
}