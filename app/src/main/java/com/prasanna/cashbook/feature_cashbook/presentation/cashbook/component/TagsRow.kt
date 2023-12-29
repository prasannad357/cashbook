package com.prasanna.cashbook.feature_cashbook.presentation.cashbook.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookEvent
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TagsRow(viewModel: CashbookViewModel){
    val tagName = remember{ mutableStateOf("") }
    val tags = remember {
        mutableStateListOf<String>()
    }

    LaunchedEffect(key1 = Unit){
        tags.addAll(viewModel.tags.value)
    }

    Row(horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ){
        TextField(value = tagName.value ,
            label = { Text("Enter tags") },
            onValueChange = { mTag ->
                tagName.value = mTag
            },
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = {
            if(tagName.value.isNotBlank()){
                tags.add(tagName.value)
                viewModel.onEvent(CashbookEvent.AddTags(tags))
                tagName.value = ""
            }
        }) {
            Icon(imageVector = Icons.Rounded.Add,
                contentDescription = "Add Tag",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    FlowRow(modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.SpaceAround
    ){
        tags.forEach{
            if(it.isNotBlank()){
                Text(text = it,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(2.dp)
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