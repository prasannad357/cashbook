package com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.outlined.RestoreFromTrash
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookEvent
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookViewModel
import com.prasanna.cashbook.feature_cashbook.presentation.util.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TopBar(viewModel: CashbookViewModel, onBinClick:()->Unit){
    val scope = rememberCoroutineScope()
    var cbName by remember {
        mutableStateOf("")
    }
    var tags by remember {
        mutableStateOf(emptyList<String>())
    }

    LaunchedEffect(key1 = viewModel.cashbookState.value.cbName) {
        cbName = viewModel.cashbookState.value.cbName
    }

    Column(modifier = Modifier
        .fillMaxWidth()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Row(horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .weight(5f)
                    .padding(start = 20.dp, top = 10.dp)
            ) {
                Text(text = cbName, fontSize = 20.sp)
            }

            if(cbName != "Repeat Transactions"){
                IconButton(onClick = {
                    viewModel.onEvent(CashbookEvent.ToggleRepeatTransactionPopup)
                }, modifier = Modifier.weight(2f),) {
                    Icon(imageVector = Icons.Default.PlaylistAdd, contentDescription = "Compare cashbooks",
                        modifier = Modifier.weight(0.5f))
                }
            }


            OutlinedButton(
                modifier = Modifier.weight(3f),
                onClick = {
                onBinClick()
            }) {
                Icon(
                    imageVector = Icons.Outlined.RestoreFromTrash,
                    contentDescription = "Bin",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Bin",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        LaunchedEffect(key1 = viewModel.cashbookState.value.cbTags) {
            tags = viewModel.cashbookState.value.cbTags
        }

        FlowRow(modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .padding(8.dp),
            verticalArrangement = Arrangement.SpaceAround
        ){
            tags.forEach{
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
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Remark",
                modifier = Modifier.weight(2f),
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Cr/Dr",modifier = Modifier.weight(1f),
                textAlign = TextAlign.Right,
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Qty",modifier = Modifier.weight(1f),
                textAlign = TextAlign.Right,
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Total", modifier = Modifier.weight(1f),
                textAlign = TextAlign.Right,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InsertRepeatTransactions(cashbookViewModel: CashbookViewModel){
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = {
            cashbookViewModel.onEvent(CashbookEvent.ToggleRepeatTransactionPopup)
        },
        properties = PopupProperties(focusable = true),
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxWidth(0.8f)
                .padding(10.dp)
        ){
            Spacer(modifier = Modifier.height(10.dp))
            Text("Want to add all repeating transactions?", color = MaterialTheme.colorScheme.onPrimary,
                style = TextStyle(textAlign = TextAlign.Center), fontSize = 18.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
            {
                Button(onClick = {
                    cashbookViewModel.onEvent(CashbookEvent.ToggleRepeatTransactionPopup)
                }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onPrimary)
                }

                Button(onClick = {
                    cashbookViewModel.onEvent(CashbookEvent.AddRepeatTransactions)
                    cashbookViewModel.onEvent(CashbookEvent.ToggleRepeatTransactionPopup)
                }) {
                    Text("Add", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }

    }

}