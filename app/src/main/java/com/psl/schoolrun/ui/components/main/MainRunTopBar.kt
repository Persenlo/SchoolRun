package com.psl.schoolrun.ui.components.main

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.psl.schoolrun.viewmodel.MainViewModel

@Composable
fun MainRunTopBar(mainVM: MainViewModel, showAdminOption: MutableState<Boolean>) {

    val isScoreMode = remember { mutableStateOf(true) }
    val transition = updateTransition(targetState = isScoreMode, label = "")
    val scoreTextSize = transition.animateInt(label = "") { state ->
        if (state.value) 26 else 12
    }
    val freeTextSize = transition.animateInt(label = "") { state ->
        if (!state.value) 26 else 12
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .padding(bottom = 32.dp, top = 16.dp)
            .height(32.dp)
            .fillMaxSize()
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = "计分跑",
                fontSize = scoreTextSize.value.sp,
                color = if (isScoreMode.value) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                if (!isScoreMode.value) {
                                    isScoreMode.value = true
                                }
                            }
                        )
                    }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "自由跑",
                fontSize = freeTextSize.value.sp,
                color = if (!isScoreMode.value) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                if (isScoreMode.value) {
                                    isScoreMode.value = false
                                }
                            }
                        )
                    }
            )
        }
        Row() {
            IconButton(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .size(32.dp),
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "刷新打卡点",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            if(mainVM.userInfo.value.admin){
                IconButton(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.extraLarge
                        )
                        .size(32.dp),
                    onClick = {
                        showAdminOption.value = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "管理员菜单",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}