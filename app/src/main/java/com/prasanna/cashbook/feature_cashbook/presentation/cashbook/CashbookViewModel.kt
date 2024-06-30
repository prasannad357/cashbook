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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CashbookViewModel @Inject constructor(private val cashbookUseCases:CashbookUseCases,
                                            savedStateHandle: SavedStateHandle
                                            ):ViewModel() {
    private val TAG = "CashbookViewModel"

    private val _state = cashbookUseCases.getCashbooks

    private val _balance = mutableStateOf(0.toBigDecimal())
    var balance = _balance

    private val _date = mutableStateOf<LocalDate>(LocalDate.now())
    val date: State<LocalDate> = _date

    private val _isCredit = mutableStateOf(false)
    var isCredit = _isCredit

    private val _amount = mutableStateOf<BigDecimal>(0.toBigDecimal())
    val amount:State<BigDecimal> = _amount

    private val _tags = mutableStateOf<List<String>>(emptyList())
    val tags:State<List<String>> = _tags

    private val _remark = mutableStateOf("")
    val remark:State<String> = _remark


    private val _cashbookName = mutableStateOf<String>("")
    val cashbookName:State<String> = _cashbookName

    private val _cashbookTags = mutableStateOf<List<String>>(listOf(""))
    val cashbookTags:State<List<String>> = _cashbookTags

    private val _createdOn = mutableStateOf<LocalDate?>(null)
    val createdOn:State<LocalDate?> = _createdOn

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
    private var _cashbookCreatedTimeStamp:Long? = null
    private var _createdDate:LocalDate? = null

    private val _transactionsInBin = mutableStateOf<List<Transaction>>(mutableListOf())
    val transactionsInBin:State<List<Transaction>> = _transactionsInBin

    private val _toggleDeleteTopBar = mutableStateOf<List<Transaction>?>(null)
    val toggleDeleteTopBar:State<List<Transaction>?> = _toggleDeleteTopBar

    private val _toggleRepeatTransactionsPopup = mutableStateOf(false)
    val toggleRepeatTransactionsPopup:State<Boolean> = _toggleRepeatTransactionsPopup


    init {
        savedStateHandle.get<Int>("cashbookId")?.let{
            viewModelScope.launch {
                _id = it
                val cashbook = cashbookUseCases.getCashbookById(id = it)

                Log.d(TAG, "CashbookId: $it, Cashbook: $cashbook")
                _cashbookName.value = cashbook.name
                _cashbookTags.value = cashbook.tags
                _cashbookCreatedTimeStamp = cashbook.createdTimeStamp
                Log.d(TAG, "Fetching transactions...")
                getTransactionsByCashbookId(it)
                Log.d(TAG, "Fetched transactions: ${_transactions.value}")
                _createdDate = cashbook.createdDate
                _createdOn.value = cashbook.createdDate
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
            is CashbookEvent.AddDate -> _date.value = event.date
            is CashbookEvent.AddRemark -> _remark.value = event.remark
            is CashbookEvent.AddTags -> _tags.value = event.tags
            is CashbookEvent.AddTransaction -> {
                val currentTime = System.currentTimeMillis()
                val transaction = Transaction(
                    id = event.transactionId,
                    date = _date.value,
                    amount = _amount.value, isCredit = isCredit.value,
                    createdAt = currentTime, updatedAt = currentTime,
                    tags = _tags.value, remark = _remark.value, cashbookId = _id!!,
                    cashbookName = _cashbookName.value)
                viewModelScope.launch {
                    cashbookUseCases.addTransaction(transaction)
                }
            }
            is CashbookEvent.EditCashbookName -> {
                //Similar to whatsapp edit group name (just use popup)
                _cashbookName.value = event.name
            }

            is CashbookEvent.EditCashbookTags ->{
                //Similar to whatsapp edit group name (just use popup)
                _cashbookTags.value = event.tags
            }
            is CashbookEvent.SaveCashbook -> {
                //Ideally, remove this condition, save cashbook in add expense option
                viewModelScope.launch {
                    //Handle Adding new cashbook or editing the cashbook
                    Log.d(TAG, "Transactions at the time of adding: ${transactions.value}")
                    val mCashbook = Cashbook(
                        name = cashbookName.value,
                        tags = cashbookTags.value,
                        createdTimeStamp = _cashbookCreatedTimeStamp!!,
                        lastEditTimeStamp = System.currentTimeMillis(),
                        id = _id,
                        createdDate = _createdDate!!
                        )
                    Log.d(TAG, "Adding cashbook: $mCashbook")
                    cashbookUseCases.addCashbook(mCashbook)
                }
            }

            is CashbookEvent.DeleteTransaction -> {
                viewModelScope.launch {
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
                viewModelScope.launch {
                    cashbookUseCases
                        .addTransaction(event.transaction.copy(inBin = false))
                }
            }

            is CashbookEvent.PermanentlyDeleteTransaction -> {
                viewModelScope.launch {
                    cashbookUseCases.deleteTransaction(event.transaction)
                }
            }

            is CashbookEvent.PermanentlyDeleteAllTransactions -> {
                viewModelScope.launch {
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
                        cashbookName = _cashbookName.value)
                    viewModelScope.launch {
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
            }.launchIn(viewModelScope)
    }

    private fun getRepeatTransactions(){
        getRepeatTransactionsJob?.cancel()

        getRepeatTransactionsJob = cashbookUseCases
            .getTransactionByCashbookId(1).onEach { transactionList ->
                _repeatTransactions.value = transactionList
            }.launchIn(viewModelScope)
    }

    private fun getTransactionsInBin(cashbookId:Int){
        getBinTransactionsJob?.cancel()

        getBinTransactionsJob = cashbookUseCases
            .getTransactionsInBin(cashbookId).onEach {
                _transactionsInBin.value = it
            }.launchIn(viewModelScope)
    }

}