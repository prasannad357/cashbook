package com.prasanna.cashbook.feature_cashbook.presentation.cashbook_list

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook_list.component.CashbookListItem
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook_list.component.OrderSection
import com.prasanna.cashbook.feature_cashbook.presentation.util.Screen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CashbookListScreen(
    navController: NavController,
    viewModel: CashbookListViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val state = viewModel.state.value
    val TAG = "CashbookListScreen"

    //TODO: cashbook item UI should change if it's id is in isSelected ids
    var isCashbookSelected by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(CashbookListEvent.ToggleAddCashbookPopup)
            },
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                if(state.isAddCashbookPopupVisible){
                    Icon(imageVector = Icons.Default.Close,
                        contentDescription = "Close popup")
                }else{
                    Icon(imageVector = Icons.Default.Add,
                        contentDescription = "Add Cashbook"
                        )
                }
            }
        }
    ) {
        val padding = it

        Column(modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(padding)
            .padding(start = 20.dp, end = 20.dp))
        {
            TopBar(viewModel = viewModel, navController)

            if(viewModel.state.value.isOrderSelectionVisible){
                OrderSection(onOrderChange = { cashbookOrder ->
                    viewModel.onEvent(CashbookListEvent.Order(cashbookOrder))
                }, cashbookOrder = viewModel.state.value.cashbookOrder)
            }





            LazyColumn(modifier = Modifier.background(MaterialTheme.colorScheme.surface)){
                items(items = state.cashbooks,
                    key = { cashbook ->
                        cashbook.id!!
                    }
                ){ cashbook: Cashbook ->

                    CashbookListItem(cashbook = cashbook,
                        modifier = Modifier
                            .animateItemPlacement(tween(250, easing = LinearOutSlowInEasing))
                            .clickable {
                                if (state.isCompareMultiSelectActive) {
                                    if (state.selectedCashbooks.contains(cashbook.id)) {
                                        val ids: ArrayList<Int> = ArrayList<Int>()
                                        ids.addAll(state.selectedCashbooks)
                                        ids.remove(cashbook.id)
                                        viewModel.onEvent(
                                            CashbookListEvent
                                                .SelectedCashbooks(
                                                    ids
                                                )
                                        )
                                    } else {
                                        val ids: ArrayList<Int> = ArrayList<Int>()
                                        ids.addAll(state.selectedCashbooks)
                                        cashbook.id?.let { cId -> ids.add(cId) }
                                        viewModel.onEvent(
                                            CashbookListEvent
                                                .SelectedCashbooks(
                                                    ids
                                                )
                                        )
                                    }

                                } else {
                                    Log.d(TAG, "CashbookId = ${cashbook.id}")
                                    navController.navigate(
                                        Screen.CashbookScreen.route +
                                                "?cashbookId=${cashbook.id}"
                                    )
                                }
                            },
                        onDeleteCashbook = {
                            viewModel.onEvent(CashbookListEvent.DeleteCashbook(cashbook))
                        },
                        isSelected = state.selectedCashbooks.contains(cashbook.id)
                        )
                    
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            if(state.isAddCashbookPopupVisible){
                ShowPopup(viewModel)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBar(viewModel: CashbookListViewModel, navController: NavController){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically) {
        if(viewModel.state.value.isCompareMultiSelectActive){
            MultiSelectTopBar(viewModel = viewModel, navController = navController)
        }else{
            GeneralTopBar(viewModel = viewModel)
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MultiSelectTopBar(viewModel: CashbookListViewModel,
                      navController:NavController){
    val context = LocalContext.current
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Select Cashbooks", fontSize = 20.sp, modifier = Modifier
            .weight(3f))
        if(viewModel.state.value.isCompareMultiSelectActive){
            IconButton(onClick = {
                viewModel.onEvent(CashbookListEvent.ToggleCompareMultiSelect)
                var idsString:String = ""
                val selectedCashbooks = viewModel.state.value.selectedCashbooks
                if(selectedCashbooks.isNotEmpty() && selectedCashbooks.size > 1){
                    selectedCashbooks.forEach {
                        idsString += if(selectedCashbooks.size - 1 != selectedCashbooks.indexOf(it)){
                            "$it,"
                        }else{
                            "$it"
                        }
                    }
                    navController.navigate(
                        Screen.CompareScreen.route +
                                "?cashbookIds=${idsString}")
                    viewModel.onEvent(CashbookListEvent.SelectedCashbooks(emptyList()))
                }else{
                    viewModel.onEvent(CashbookListEvent.SelectedCashbooks(emptyList()))
                    Toast.makeText(context,"Select at least 2 cashbooks", Toast.LENGTH_SHORT).show()
                }

            }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Compare cashbooks",
                    modifier = Modifier.weight(0.5f))
            }
        }

        IconButton(onClick = {
            viewModel.onEvent(CashbookListEvent.ToggleCompareMultiSelect)
            viewModel.onEvent(CashbookListEvent.SelectedCashbooks(emptyList()))
        }) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel Multiselect",
                modifier = Modifier.weight(0.5f))
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GeneralTopBar(viewModel:CashbookListViewModel){
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Cashbooks", fontSize = 20.sp,
            modifier = Modifier.weight(3f))

        IconButton(onClick = {
            viewModel.onEvent(CashbookListEvent.ToggleCompareMultiSelect)
        }) {
            Icon(imageVector = Icons.Default.BarChart, contentDescription = "Compare cashbooks",
                modifier = Modifier.weight(0.5f))
        }
        IconButton(onClick = {
            viewModel.onEvent(CashbookListEvent.ToggleOrderSection)
        }) {
            Icon(imageVector = Icons.Default.Reorder, contentDescription = "Change Order",
                modifier = Modifier.weight(0.5f))
        }

    }



}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Stable
@Composable
fun ShowPopup(viewModel:CashbookListViewModel){

    Popup(
        alignment = Alignment.Center,
        onDismissRequest = {
            viewModel.onEvent(CashbookListEvent.ToggleAddCashbookPopup)
        },
        properties = PopupProperties(focusable = true)
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .fillMaxWidth(0.8f)
                .padding(10.dp)
        ) {
            val name = remember{
                mutableStateOf("")
            }
            val tagName = remember{
                mutableStateOf("")
            }
            val tags = remember{
                mutableListOf<String>()
            }

            var recomposeRow by remember {
                mutableStateOf(0)
            }

            TextField(value = name.value ,
                label = {Text("Enter cashbook name")},
                onValueChange = { nameValue ->
                name.value = nameValue
                viewModel.onEvent(CashbookListEvent.AddCashbookName(nameValue))
            })
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
                ){
                TextField(value = tagName.value ,
                    label = {Text("Enter tags")},
                    onValueChange = { mTag ->
                        tagName.value = mTag
                    },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    if(tagName.value.isNotBlank()){
                        tags.add(tagName.value.lowercase())
                        viewModel.onEvent(CashbookListEvent.AddCashbookTags(tags))
                        recomposeRow += 1
                    }
                }) {
                    Icon(imageVector = Icons.Rounded.Add,
                        contentDescription = "Add Tag",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            if(recomposeRow > 0){
                FlowRow(modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.SpaceAround
                    ){
                    viewModel.tags.value.forEach{
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
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    viewModel.onEvent(CashbookListEvent.ToggleAddCashbookPopup)
                }) {
                    Text("Cancel")
                }
                Button(onClick = {
                    viewModel.onEvent(CashbookListEvent.ToggleAddCashbookPopup)
                    viewModel.onEvent(CashbookListEvent.AddCashbook)
                    // TODO(  onNavigateToCashbook() screen)
                }) {
                    Text("Create")
                }
            }
        }

    }
}