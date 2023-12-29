package com.prasanna.cashbook.feature_cashbook.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Cashbook(
    val name:String,
    val tags: List<String>,
    val createdDate:LocalDate,
    val createdTimeStamp:Long,
    val lastEditTimeStamp:Long,
    @PrimaryKey(autoGenerate = true) val id:Int? = null
)

class InvalidCashbookException(message:String):Exception(message)