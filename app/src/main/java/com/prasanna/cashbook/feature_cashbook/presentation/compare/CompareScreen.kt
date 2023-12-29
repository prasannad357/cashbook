package com.prasanna.cashbook.feature_cashbook.presentation.compare

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(
    navController: NavController,
    viewModel: CompareViewModel = hiltViewModel()
){
    val bgColor = MaterialTheme.colorScheme.secondaryContainer
    val textColor = MaterialTheme.colorScheme.onSecondaryContainer
    val iconTint = MaterialTheme.colorScheme.onSecondaryContainer
    Scaffold(topBar = { Text(text = "Comparison",
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), fontSize = 20.sp)}
    ) {
      val padding = it
      LazyColumn(modifier = Modifier
          .padding(it)
          .padding(20.dp)){
          viewModel.tagIdAmountMap.forEach { (tag, idAmtMap) ->
             item{
                 viewModel.resetColorIndex()
                 Column(modifier = Modifier
                     .fillMaxWidth()
                     .background(
                         color = bgColor,
                         shape = RoundedCornerShape(16.dp)
                     )
                     .padding(10.dp)
                     .padding(bottom = 10.dp)
                 ) {
                     Text(text = tag.uppercase(),
                         overflow = TextOverflow.Ellipsis,
                         textAlign = TextAlign.Center,
                         modifier = Modifier.fillMaxWidth(),
                         color = textColor,
                         fontSize = 14.sp
                         )
                     Spacer(modifier = Modifier
                         .height(10.dp)
                         .fillMaxWidth())
                     idAmtMap.forEach { (cashbookId, nameAmountMap) ->
                         nameAmountMap.forEach { (cashbookName, amount) ->
                             Row(modifier = Modifier.fillMaxWidth(),
                                 verticalAlignment = Alignment.CenterVertically
                                 ) {
                                 val maxValue = viewModel.maxAmount.value
                                 Log.d("CompareScreen", "CashbookId:$cashbookId, " +
                                         "Amount: $amount, maxValue: $maxValue" )
                                 Text(text = "$cashbookName",
                                     overflow = TextOverflow.Ellipsis,
                                     modifier = Modifier.weight(0.5f),
                                     color = textColor,
                                     fontSize = 12.sp
                                 )
                                 val sizeOfSpacer = (amount.toFloat()/maxValue.toFloat()) *0.6
                                 Log.d("CompareScreen", "Spacer size: $sizeOfSpacer")

                                 Spacer(modifier = Modifier
                                     .weight(0.5f, false)
                                     .fillMaxWidth(sizeOfSpacer.toFloat())
//                                     .width(sizeOfSpacer.dp)
                                     .height(15.dp)
                                     .background(color = Color(viewModel.getColor())))

                                 Spacer(modifier = Modifier.width(5.dp))
                                 Text(text = "Rs. $amount",
                                     overflow = TextOverflow.Ellipsis,
                                     modifier = Modifier.weight(0.3f),
                                     textAlign = TextAlign.Center,
                                     color = textColor,
                                     fontSize = 12.sp
                                 )
                             }
                             Spacer(modifier = Modifier.height(5.dp))
                         }
                     }
                 }
                 Spacer(modifier = Modifier.height(10.dp))
             }
          }

      }

    }

}