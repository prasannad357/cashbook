package com.prasanna.cashbook.feature_cashbook.presentation.cashbook_list.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prasanna.cashbook.feature_cashbook.presentation.util.CashbookOrder
import com.prasanna.cashbook.feature_cashbook.presentation.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    cashbookOrder: CashbookOrder = CashbookOrder.LastEdit(OrderType.Descending),
    onOrderChange:(CashbookOrder) -> Unit
){
    Column(modifier = Modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            DefaultRadioButton(text = "Name",
                selected = cashbookOrder is CashbookOrder.Name ,
                onSelect = { onOrderChange(CashbookOrder.Name(cashbookOrder.orderType)) })
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(text = "Created",
                selected = cashbookOrder is CashbookOrder.CreatedDate,
                onSelect = { onOrderChange(CashbookOrder.CreatedDate(cashbookOrder.orderType)) })
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(text = "Recent",
                selected = cashbookOrder is CashbookOrder.LastEdit,
                onSelect = { onOrderChange(CashbookOrder.LastEdit(cashbookOrder.orderType)) })
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            DefaultRadioButton(text = "Ascending",
                selected = cashbookOrder.orderType is OrderType.Ascending,
                onSelect = { onOrderChange(cashbookOrder.copy(OrderType.Ascending)) })

            DefaultRadioButton(text = "Descending",
                selected = cashbookOrder.orderType is OrderType.Descending,
                onSelect = { onOrderChange(cashbookOrder.copy(OrderType.Descending)) })
        }
    }
}