package com.psl.schoolrun.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SmallMenuItem(
    menuText: String,
    icon: ImageVector,
    iconDesc: String = "",
    onClick: ()->Unit
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(88.dp)
            .width(88.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onClick()
            }
            .padding(horizontal = 8.dp, vertical = 8.dp)

    ){
        Icon(imageVector = icon, contentDescription = iconDesc)
        Text(text = menuText, color = MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 12.sp)
    }
}