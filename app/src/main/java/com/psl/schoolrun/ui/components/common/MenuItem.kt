package com.psl.schoolrun.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuItem(
    menuText: String,
    rightContent: @Composable() (RowScope.() -> Unit),
    onClick: ()->Unit
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 6.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .clickable {
                onClick()
            }
            .padding(horizontal = 24.dp, vertical = 12.dp)

    ){
        Text(text = menuText, color = MaterialTheme.colorScheme.onPrimaryContainer)
        Row {
            rightContent()
        }
    }
}