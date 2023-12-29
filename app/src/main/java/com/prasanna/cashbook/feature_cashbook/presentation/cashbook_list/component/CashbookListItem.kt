package com.prasanna.cashbook.feature_cashbook.presentation.cashbook_list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CashbookListItem(
    cashbook: Cashbook,
    modifier:Modifier = Modifier,
    onDeleteCashbook:()->Unit,
    isSelected:Boolean
){
    Column(
        modifier =
        if(isSelected){
            modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(start = 10.dp, bottom = 10.dp, end = 10.dp)
        }
        else{
            modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(16.dp)
                )
//                .border(
//                    width = 1.dp,
//                    color = MaterialTheme.colorScheme.primary,
//                    shape = RoundedCornerShape(5.dp)
//                )
                .padding(start = 10.dp, bottom = 10.dp, end = 10.dp)
        }

    ) {
        val textColor = MaterialTheme.colorScheme.onSecondaryContainer
        val iconColor = MaterialTheme.colorScheme.onSecondaryContainer
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            Text(text = cashbook.name, fontSize = 14.sp, color = textColor)
            IconButton(onClick = { onDeleteCashbook() }) {
                Icon(imageVector = Icons.Default.Delete,
                    contentDescription = "delete cashbook", tint = iconColor )
            }
        }
        FlowRow(modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceAround
        ){
            cashbook.tags.forEach{
                if(it.isNotBlank()){
                    Text(text = it,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.tertiary,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(3.dp),
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }
        }
        Row(){
            Text(text = "created on: ${cashbook.createdDate}",
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
                )
        }
    }


}