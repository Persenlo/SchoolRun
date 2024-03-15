package com.psl.schoolrun.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
internal fun ShowOpenGPSDialog(onPositiveClick: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        title = { Text(text = "授权提示") },
        text = {
            Column() {
                Text(text = "使用 畅跑校园 需要获取以下权限：")
                Text(text = "位置信息")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onPositiveClick()
                }
            ) {
                Text(text = "授权")
            }
        },
        onDismissRequest = { onDismiss() },
    )
}