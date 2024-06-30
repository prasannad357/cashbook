package com.prasanna.cashbook.feature_cashbook.presentation.cashbook

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component.BinDrawer
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component.InsertRepeatTransactions
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component.TopBar
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component.TransactionItem
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component.TransactionPopup
import kotlinx.coroutines.launch
import java.math.BigDecimal

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun CashbookScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel:CashbookViewModel = hiltViewModel()
    ) {
    val TAG = "CashbookScreen"
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    BinDrawer(viewModel){
                        scope.launch {
                            if(drawerState.isOpen){
                                drawerState.close()
                            }
                        }
                    }
                }
            },
            gesturesEnabled = false
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            viewModel.onEvent(CashbookEvent.ToggleAddTransactionPopup)
                        },
                            contentColor = MaterialTheme.colorScheme.onTertiary,
                            containerColor = MaterialTheme.colorScheme.tertiary
                            ) {
                            if(viewModel.addTransactionPopupShown.value){
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Close popup")
                            }else{
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Expense")
                            }
                        }
                    }
                ) {

                    Column(modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
                        .padding(it)
                    ) {
                        TopBar(viewModel = viewModel){
                            if(drawerState.isOpen){
                                scope.launch {
                                    drawerState.close()
                                }
                            }else{
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        }
                        val sortedTransactions = viewModel.transactions.value.sortedBy { it -> it.date }
                        var tidBalanceMap = remember {
                            mutableStateMapOf<Int, BigDecimal>(
                            )
                        }
                        LaunchedEffect(key1 = sortedTransactions) {
                            var balance:BigDecimal = 0.toBigDecimal()
                            sortedTransactions.forEachIndexed { id, transaction ->
                                Log.d(TAG, "CashbookScreen: transaction id:${transaction.id}")
                                if(transaction.isCredit) {
                                    Log.d(TAG, "CashbookScreen: Adding ${transaction.amount}")
                                    balance += transaction.amount }
                                else {
                                    Log.d(TAG, "CashbookScreen: Subtracting ${transaction.amount}")
                                    balance -= transaction.amount}
                                Log.d(TAG, "tid:$id balance: $balance")

                                tidBalanceMap[id] = balance
                            }
                        }
                        LazyColumn(modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 20.dp, end = 20.dp)){

                            items(items = sortedTransactions,
                                key = {transaction -> transaction.id!!}
                                ){ transaction ->

                                val displayDate = sortedTransactions.indexOf(transaction) == 0 ||
                                        transaction.date != sortedTransactions[sortedTransactions.indexOf(transaction)-1].date

                                TransactionItem(transaction = transaction, displayDate = displayDate,
                                    balance = tidBalanceMap[sortedTransactions.indexOf(transaction)]?:0.toBigDecimal(), viewModel = viewModel,
                                    modifier = Modifier.animateItemPlacement(tween(250)))

                                if(sortedTransactions.indexOf(transaction) == sortedTransactions.size - 1){
                                    Spacer(modifier = Modifier.height(100.dp))
                                }else{
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                            }
                        }

                        if(viewModel.addTransactionPopupShown.value){
                            TransactionPopup(viewModel = viewModel, transaction = null)
                        }

                        if(viewModel.editTransactionPopupShown.value != null){
                            TransactionPopup(
                                viewModel = viewModel,
                                transaction = viewModel.editTransactionPopupShown.value
                            )
                        }

                        if(viewModel.toggleRepeatTransactionsPopup.value){
                            InsertRepeatTransactions(cashbookViewModel = viewModel)
                        }

                    }

                }
            }

        }
    }

}







