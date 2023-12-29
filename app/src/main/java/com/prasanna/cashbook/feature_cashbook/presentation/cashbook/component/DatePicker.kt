package com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component

import android.app.DatePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(onDateSelected:(date: LocalDate)->Unit){
    val todayDate = LocalDate.now()
    val dialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            val month = if(selectedMonth+1 < 10) "0${selectedMonth+1}" else "${selectedMonth+1}"
            val day = if(selectedDayOfMonth < 10) "0$selectedDayOfMonth" else "$selectedDayOfMonth"
            val localDate = LocalDate.parse("$selectedYear-$month-$day")
            onDateSelected(localDate)
        }, todayDate.year, todayDate.monthValue - 1, todayDate.dayOfMonth
    )
//    dialog.setOnCancelListener {  }
    dialog.show()
}