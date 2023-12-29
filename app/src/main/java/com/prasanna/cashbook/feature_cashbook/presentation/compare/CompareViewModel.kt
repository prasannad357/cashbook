package com.prasanna.cashbook.feature_cashbook.presentation.compare

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prasanna.cashbook.feature_cashbook.domain.use_case.CashbookUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CompareViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle,
    private val cashbookUseCases: CashbookUseCases
    ):ViewModel() {

    private var _transactionJobs:HashMap<Int, Job?> = hashMapOf()
    // hashMapOf<Tag, HashMap<CashbookId, Amount>>()
//    private var _tagIdAmountMap = hashMapOf<String, HashMap<Int, BigDecimal>>()
//    val tagIdAmountMap:Map<String, HashMap<Int, BigDecimal>> = _tagIdAmountMap
    private var _tagIdAmountMap = hashMapOf<String, HashMap<Int, HashMap<String,BigDecimal>>>()
    val tagIdAmountMap:Map<String, HashMap<Int, HashMap<String,BigDecimal>>> = _tagIdAmountMap
    private var _maxAmount: MutableState<BigDecimal> = mutableStateOf(0.toBigDecimal())
    val maxAmount: State<BigDecimal> = _maxAmount
    private var _cashbookIdName:HashMap<Int, String> = hashMapOf()
    val cashbookIdName:Map<Int, String> = _cashbookIdName
    private var _colorIndex = 0
    private val colors = arrayOf(0xFFFFAEBC, 0xFFA0E7E5,0xFFB4F8C8, 0xFFFBE7C6,
        0xFFE98973,0xFFE7D4C0,0xFF88B2CC,0xFF658EA9)

    init {
        savedStateHandle.get<String>("cashbookIds")?.let { idsString ->
            val cashbookIds = idsString.split(",")
            Log.d("CompareViewModel", "CashbookIds = $cashbookIds")
            cashbookIds.forEach{id ->
                mapTagsIdAndAmt(id.toInt())
            }
        }
    }

    private fun mapTagsIdAndAmt(cashbookId:Int){
        _transactionJobs[cashbookId]?.cancel()
        _transactionJobs[cashbookId] = cashbookUseCases
            .getTransactionByCashbookId(cashbookId).onEach {transactions ->
                transactions.forEach { transaction ->
                    if(transaction.amount > _maxAmount.value){
                        _maxAmount.value = transaction.amount
                    }
                    transaction.tags.forEach {tag ->
                        val containsTag = _tagIdAmountMap.containsKey(tag)
                        if(containsTag) {
                            val containsCashbookId = _tagIdAmountMap[tag]?.containsKey(cashbookId) == true
                            if(containsCashbookId){
                                val existingAmt:BigDecimal = _tagIdAmountMap[tag]
                                    ?.get(cashbookId)!![transaction.cashbookName]!!
                                val newAmt:BigDecimal = existingAmt.add(transaction.amount)
                                _tagIdAmountMap[tag]?.set(cashbookId,
                                    hashMapOf(transaction.cashbookName to newAmt))
                            }else{
                                //doesn't contain cashbookId, so add a new one
                                _tagIdAmountMap[tag]?.set(cashbookId,
                                    hashMapOf(transaction.cashbookName to transaction.amount))
                            }
                        }else{
                            //doesn't contain tag so add a new one with value for cashbook id
                            _tagIdAmountMap[tag] = hashMapOf(cashbookId to
                                    hashMapOf(transaction.cashbookName to transaction.amount))
                        }
                    }
                }

            }.launchIn(viewModelScope)
    }

    fun getColor():Long{
        val color = colors[_colorIndex]
        if(_colorIndex < colors.size - 1){
            _colorIndex += 1
        }else{
            _colorIndex = 0
        }
        return color
    }

    fun resetColorIndex(){
        _colorIndex = 0
    }

}