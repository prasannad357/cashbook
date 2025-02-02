package com.prasanna.cashbook.feature_cashbook.presentation.cashbook

import android.os.Build
import androidx.annotation.RequiresApi
import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import java.math.BigDecimal
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class CashbookState(
    val cbName:String = "",
    val cbTags:List<String> = emptyList(),
    val createdTimeStamp:Long = 0L,
    val createdDate: LocalDate = LocalDate.now(),
    val createdOn:LocalDate = LocalDate.now(),
    )
