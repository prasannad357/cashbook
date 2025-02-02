package com.prasanna.cashbook.feature_cashbook.presentation.cashbook_list

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook
import com.prasanna.cashbook.feature_cashbook.domain.model.InvalidCashbookException
import com.prasanna.cashbook.feature_cashbook.domain.use_case.CashbookUseCases
import com.prasanna.cashbook.feature_cashbook.presentation.util.CashbookOrder
import com.prasanna.cashbook.feature_cashbook.presentation.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CashbookListViewModel @Inject constructor(val cashbookUseCases: CashbookUseCases):ViewModel() {
    private val TAG = "CashbookListViewModel"
    private val _state = mutableStateOf(CashbookListState())
    val state: State<CashbookListState> = _state

    private val _name = mutableStateOf("")
    val name:State<String> = _name

    private val _tags = mutableStateOf(emptyList<String>())
    val tags:State<List<String>> = _tags

    //I could create a dustbin for cashbooks
    private var recentlyDeletedCashbook:Cashbook? = null
    private var getCashbooksJob:Job? = null
    init {
        getCashbooks(CashbookOrder.LastEdit(OrderType.Descending))
        var repeatTransactionsBookExists = false
        _state.value.cashbooks.forEach {
            if(it.name == "Repeat Transactions" && it.id == 1){
                repeatTransactionsBookExists = true
            }
        }
        if(!repeatTransactionsBookExists){
            viewModelScope.launch {
                try {
                    val cashbook = Cashbook(
                        id = 1,
                        name = "Repeat Transactions",
                        tags = emptyList(),
                        createdTimeStamp = System.currentTimeMillis(),
                        lastEditTimeStamp = System.currentTimeMillis(),
                        createdDate = LocalDate.now()
                    )
                    withContext(Dispatchers.IO){
                        cashbookUseCases.addCashbook(cashbook)
                    }
                }catch (ex:InvalidCashbookException){
                    Log.e(TAG, ex.message.toString())
                }
            }
        }

        viewModelScope.launch{
            val mInt1 = temp1(5000L)
            val mInt2 = temp1(1000L)
            Log.d("temp1","Addition: ${mInt1+mInt2}")
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event:CashbookListEvent){
        when(event){
            is CashbookListEvent.AddCashbookName ->{
                _name.value = event.name
            }

            is CashbookListEvent.AddCashbookTags -> {
                _tags.value = event.tags
            }

            is CashbookListEvent.AddCashbook ->{
                viewModelScope.launch {
                    try {
                        val cashbook = Cashbook(
                            name = name.value,
                            tags = tags.value,
                            createdTimeStamp = System.currentTimeMillis(),
                            lastEditTimeStamp = System.currentTimeMillis(),
                            createdDate = LocalDate.now()
                        )
                        withContext(Dispatchers.IO){
                            cashbookUseCases.addCashbook(cashbook)
                        }
                    }catch (ex:InvalidCashbookException){
                        Log.e(TAG, ex.message.toString())
                    }
                }
            }
            is CashbookListEvent.Order -> {
                if(state.value.cashbookOrder::class == event.cashbookOrder::class &&
                        state.value.cashbookOrder.orderType == event.cashbookOrder.orderType){
                    return
                }
                getCashbooks(order = event.cashbookOrder)
                _state.value.copy(cashbookOrder = event.cashbookOrder)
            }
            is CashbookListEvent.ToggleOrderSection -> {
                _state.value = _state.value.copy(
                    isOrderSelectionVisible = !_state.value.isOrderSelectionVisible
                )
            }

            is CashbookListEvent.DeleteCashbook -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO){
                        cashbookUseCases.deleteCashbook(event.cashbook)
                        recentlyDeletedCashbook = event.cashbook
                    }
                }
            }

            is CashbookListEvent.RestoreCashbook -> {
                viewModelScope.launch {
                    recentlyDeletedCashbook?.let{
                        withContext(Dispatchers.IO){
                            cashbookUseCases.addCashbook(it)
                            recentlyDeletedCashbook = null
                        }
                    }
                }
            }

            is CashbookListEvent.ToggleAddCashbookPopup -> {
                _state.value = _state.value.copy(
                    isAddCashbookPopupVisible = !_state.value.isAddCashbookPopupVisible
                )
            }

            is CashbookListEvent.ToggleCompareMultiSelect ->{
                _state.value = _state.value.copy(
                    isCompareMultiSelectActive = !_state.value.isCompareMultiSelectActive
                )
            }

            is CashbookListEvent.SelectedCashbooks ->{
                _state.value = _state.value.copy(
                    selectedCashbooks = event.cashbookIds
                )
            }

        }
    }


    private fun getCashbooks(order:CashbookOrder){
        getCashbooksJob?.cancel()
        getCashbooksJob = cashbookUseCases.getCashbooks(order).onEach {cashbooks ->
            _state.value = _state.value.copy(
                cashbooks = cashbooks,
                cashbookOrder = order
            )
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    private suspend fun temp1(time:Long):Int{
        Log.d("temp1", "Start time count for $time")
        delay(time)
        Log.d("temp1", "Print statement for $time")
        return if(time == 1000L) 10 else 50
    }
}