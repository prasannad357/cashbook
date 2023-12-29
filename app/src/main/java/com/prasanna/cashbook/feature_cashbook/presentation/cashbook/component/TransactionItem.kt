package com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookEvent
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookViewModel
import java.math.BigDecimal
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionItem(
    transaction: Transaction,
    displayDate:Boolean = false,
    balance:BigDecimal = 0.toBigDecimal(),
    viewModel: CashbookViewModel,
    modifier: Modifier
) {
    val bgColor = MaterialTheme.colorScheme.secondaryContainer
    val textColor = MaterialTheme.colorScheme.onSecondaryContainer
    val iconTint = MaterialTheme.colorScheme.onSecondaryContainer
    if(!displayDate){
        DisplayTransaction(transaction = transaction, balance = balance,
            viewModel = viewModel, modifier = modifier)
    }else{
        DisplayDate(transaction = transaction, modifier= modifier)
        DisplayTransaction(transaction = transaction, balance = balance,
            viewModel = viewModel, modifier = modifier)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun DisplayTransaction(transaction:Transaction, balance:BigDecimal, viewModel: CashbookViewModel,
                       modifier: Modifier
){

    val bgColor = MaterialTheme.colorScheme.secondaryContainer
    val textColor = MaterialTheme.colorScheme.onSecondaryContainer
    val iconTint = MaterialTheme.colorScheme.onSecondaryContainer
    val cornerRadius = 16.dp

    Column(modifier = modifier
        .combinedClickable(
            onLongClick = {
//                          viewModel.onEvent(CashbookEvent.ToggleDeleteTopBar)
            },
            onClick = {
                viewModel.onEvent(CashbookEvent.ToggleEditTransactionPopup(transaction))
            }
        )
        .background(color = bgColor, shape = RoundedCornerShape(cornerRadius))
        .padding(10.dp)
    ) {
        //Old green Color(0xFFA7D397), old red Color(0xFFE95793)
        val color = if(transaction.isCredit) MaterialTheme.colorScheme.tertiary
        else MaterialTheme.colorScheme.error

//        if(viewModel.editTransactionPopupShown.value == transaction.id){
//            ShowPopup(viewModel = viewModel, transaction = transaction)
//        }

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
            ) {
            Text(text = transaction.remark, modifier = Modifier.weight(2f),
                color = textColor)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = transaction.amount.toString(), color = color,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Right, fontWeight = FontWeight.Bold
                )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = balance.toString(), modifier = Modifier.weight(1f),
                textAlign = TextAlign.Right
                )
        }
        if(transaction.tags.isNotEmpty()){
            FlowRow(modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.SpaceAround
            ){
                transaction.tags.forEach{
                    if(it.isNotBlank()){
                        Text(text = it,
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(3.dp),
                            color = MaterialTheme.colorScheme.onTertiary,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayDate(transaction:Transaction, modifier: Modifier){
    Column(modifier = modifier) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp),horizontalArrangement = Arrangement.Start) {
            Text(text = transaction.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString(), fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}