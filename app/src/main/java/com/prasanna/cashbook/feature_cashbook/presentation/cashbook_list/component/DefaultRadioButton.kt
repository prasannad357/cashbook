package com.prasanna.cashbook.feature_cashbook.presentation.cashbook_list.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DefaultRadioButton(
    text:String,
    selected:Boolean,
    onSelect:()->Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )

        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodySmall)
    }
}