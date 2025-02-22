package com.prasanna.cashbook.feature_cashbook.presentation.cashbook

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook
import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import com.prasanna.cashbook.feature_cashbook.domain.use_case.CashbookUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject
import com.prasanna.cashbook.feature_cashbook.presentation.util.Constants.ONE_INT

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CashbookViewModel @Inject constructor(private val cashbookUseCases:CashbookUseCases,
                                            savedStateHandle: SavedStateHandle
                                            ):ViewModel() {
    private val TAG = "CashbookViewModel"

    private val _cashbookState = mutableStateOf(CashbookState())
    val cashbookState:State<CashbookState> = _cashbookState

    private val _balance = mutableStateOf(0.toBigDecimal())

    private val _date = mutableStateOf<LocalDate>(LocalDate.now())
    val date: State<LocalDate> = _date

    private val _isCredit = mutableStateOf(false)
    var isCredit = _isCredit

    private val _quantity = mutableIntStateOf(ONE_INT)
    val quantity:State<Int> = _quantity

    private val _amount = mutableStateOf<BigDecimal>(0.toBigDecimal())
    val amount:State<BigDecimal> = _amount

    private val _tags = mutableStateOf<List<String>>(emptyList())
    val tags:State<List<String>> = _tags

    private val _remark = mutableStateOf("")
    val remark:State<String> = _remark

    private var _addTransactionPopupShown = mutableStateOf(false)
    val addTransactionPopupShown:State<Boolean> = _addTransactionPopupShown

    private val _editTransactionPopupShown = mutableStateOf<Transaction?>(null)
    val editTransactionPopupShown:State<Transaction?> = _editTransactionPopupShown

    private var getTransactionsJob: Job? = null
    private var getRepeatTransactionsJob: Job? = null
    private var getBinTransactionsJob:Job? = null

    private val _repeatTransactions = mutableStateOf<List<Transaction>>(emptyList())
    private val _transactions = mutableStateOf<List<Transaction>>(mutableListOf())
    val transactions:State<List<Transaction>> = _transactions
    private var _id:Int? = null

    private val _transactionsInBin = mutableStateOf<List<Transaction>>(mutableListOf())
    val transactionsInBin:State<List<Transaction>> = _transactionsInBin

    private val _toggleRepeatTransactionsPopup = mutableStateOf(false)
    val toggleRepeatTransactionsPopup:State<Boolean> = _toggleRepeatTransactionsPopup


    init {
        savedStateHandle.get<Int>("cashbookId")?.let{
            viewModelScope.launch(Dispatchers.IO) {

                _id = it
                val cashbook = cashbookUseCases.getCashbookById(id = it)

                Log.d(TAG, "CashbookId: $it, Cashbook: $cashbook")
                withContext(Dispatchers.Main){
                    _cashbookState.value = _cashbookState.value.copy(
                        cbName = cashbook.name,
                        cbTags = cashbook.tags,
                        createdTimeStamp = cashbook.createdTimeStamp,
                        createdDate = cashbook.createdDate,
                        createdOn = cashbook.createdDate
                    )

                    Log.d(TAG, "Fetching transactions...")
                    getTransactionsByCashbookId(it)
                    Log.d(TAG, "Fetched transactions: ${_transactions.value}")
                }
                getTransactionsInBin(it)
                getRepeatTransactions()

            }
        }
    }

    fun onEvent(event:CashbookEvent){
        when(event){
            //For a cashbook that is being edited - handle the case
            is CashbookEvent.ToggleCreditDebit -> {
                _isCredit.value = !_isCredit.value
            }
            is CashbookEvent.ToggleAddTransactionPopup ->{
                _addTransactionPopupShown.value = !_addTransactionPopupShown.value
            }
            is CashbookEvent.AddAmount -> {
                _amount.value = event.amount
            }

            is CashbookEvent.AddQuantity -> {
                _quantity.intValue = event.qty
            }

            is CashbookEvent.AddDate -> _date.value = event.date
            is CashbookEvent.AddRemark -> _remark.value = event.remark
            is CashbookEvent.AddTags -> _tags.value = event.tags
            is CashbookEvent.AddTransaction -> {
                val currentTime = System.currentTimeMillis()
                val transaction = Transaction(
                    id = event.transactionId,
                    date = _date.value,
                    amount = _amount.value,
                    quantity = _quantity.intValue,
                    isCredit = isCredit.value,
                    createdAt = currentTime,
                    updatedAt = currentTime,
                    tags = _tags.value,
                    remark = _remark.value,
                    cashbookId = _id!!,
                    cashbookName = cashbookState.value.cbName)
                viewModelScope.launch(Dispatchers.IO) {
                    cashbookUseCases.addTransaction(transaction)
                }
            }
            is CashbookEvent.EditCashbookName -> {
                //Similar to whatsapp edit group name (just use popup)
                _cashbookState.value = _cashbookState.value.copy(cbName = event.name)
            }

            is CashbookEvent.EditCashbookTags ->{
                //Similar to whatsapp edit group name (just use popup)
                _cashbookState.value = _cashbookState.value.copy(cbTags = event.tags)
            }
            is CashbookEvent.SaveCashbook -> {
                //Ideally, remove this condition, save cashbook in add expense option
                viewModelScope.launch(Dispatchers.IO) {
                    //Handle Adding new cashbook or editing the cashbook
                    Log.d(TAG, "Transactions at the time of adding: ${transactions.value}")
                    val mCashbook = Cashbook(
                        name = cashbookState.value.cbName,
                        tags = cashbookState.value.cbTags,
                        createdTimeStamp = cashbookState.value.createdTimeStamp,
                        lastEditTimeStamp = System.currentTimeMillis(),
                        id = _id,
                        createdDate = cashbookState.value.createdDate
                        )
                    Log.d(TAG, "Adding cashbook: $mCashbook")

                    cashbookUseCases.addCashbook(mCashbook)
                }
            }

            is CashbookEvent.DeleteTransaction -> {
                viewModelScope.launch(Dispatchers.IO) {
                    cashbookUseCases.addTransaction(event
                        .transaction.copy(inBin = true))
                }
            }

            is CashbookEvent.UpdateBalance ->{
                if(isCredit.value){
                    _balance.value += event.amount
                }else{
                    _balance.value -= event.amount
                }
            }

            is CashbookEvent.ToggleEditTransactionPopup -> {
                _editTransactionPopupShown.value = event.transaction
            }

            is CashbookEvent.RestoreTransaction -> {
                viewModelScope.launch(Dispatchers.IO) {
                    cashbookUseCases
                        .addTransaction(event.transaction.copy(inBin = false))
                }
            }

            is CashbookEvent.PermanentlyDeleteTransaction -> {
                viewModelScope.launch(Dispatchers.IO) {
                    cashbookUseCases.deleteTransaction(event.transaction)
                }
            }

            is CashbookEvent.PermanentlyDeleteAllTransactions -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _transactionsInBin.value.forEach {
                        cashbookUseCases.deleteTransaction(it)
                    }
                }
            }

            is CashbookEvent.ToggleDeleteTopBar-> {
//                askdn
//                if(event.transactionList.isNotEmpty()){
//
//                }else{
//
//                }
            }

            CashbookEvent.AddRepeatTransactions -> {
                _repeatTransactions.value.forEach {
                    val currentTime = System.currentTimeMillis()
                    val transaction = Transaction(
                        date = LocalDate.now(),
                        amount = it.amount, isCredit = it.isCredit,
                        createdAt = currentTime, updatedAt = currentTime,
                        tags = it.tags, remark = it.remark, cashbookId = _id!!,
                        cashbookName = cashbookState.value.cbName)
                    viewModelScope.launch(Dispatchers.IO) {
                        cashbookUseCases.addTransaction(transaction)
                    }
                }

            }

            CashbookEvent.ToggleRepeatTransactionPopup -> {
                _toggleRepeatTransactionsPopup.value = !_toggleRepeatTransactionsPopup.value
            }
        }
    }

    private fun getTransactionsByCashbookId(cashbookId:Int){
        getTransactionsJob?.cancel()

        getTransactionsJob = cashbookUseCases
            .getTransactionByCashbookId(cashbookId).onEach { transactionList ->
                _transactions.value = transactionList
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    private fun getRepeatTransactions(){
        getRepeatTransactionsJob?.cancel()

        getRepeatTransactionsJob = cashbookUseCases
            .getTransactionByCashbookId(1).onEach { transactionList ->
                _repeatTransactions.value = transactionList
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    private fun getTransactionsInBin(cashbookId:Int){
        getBinTransactionsJob?.cancel()

        getBinTransactionsJob = cashbookUseCases
            .getTransactionsInBin(cashbookId).onEach {
                _transactionsInBin.value = it
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

}