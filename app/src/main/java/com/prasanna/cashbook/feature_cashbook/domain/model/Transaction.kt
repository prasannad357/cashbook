package com.prasanna.cashbook.feature_cashbook.domain.model

import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

@Entity
data class Transaction(
    //LocalDate.now() to get the date in yyyy-mm-dd format,
    // Use LocalDate.now().compareTo(LocalDate.parse("2023-10-07")) to compare
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val date: LocalDate,
    val amount:BigDecimal,
    val isCredit:Boolean,
    val createdAt: Long, //System.currentTimeMillis() i.e. epoch time, can be easily compared
    val updatedAt:Long, //System.currentTimeMillis() i.e. epoch time, can be easily compared
    val cashbookId:Int,
    val cashbookName:String,
    val tags: List<String> = emptyList(),
    val remark:String = "",
    val inBin:Boolean = false
)
