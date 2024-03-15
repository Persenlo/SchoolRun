package com.psl.schoolrun.ui.components.main

import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RunDetail(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val isStartPressed = remember { mutableStateOf(false) }
    val isStopPressed = remember { mutableStateOf(false) }
    val startRun = remember { mutableStateOf(false) }
    val pauseRun = remember { mutableStateOf(false) }

    val startRunGradEnd = remember { mutableStateOf(0f) }
    val stopRunGradEnd = remember{ mutableStateOf(0f) }

    val startRunAlpha = remember { Animatable(1f) }
    val stopRunAlpha = remember { Animatable(0f) }
    val scale by animateFloatAsState(
        targetValue = if (isStartPressed.value) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ), label = ""
    )
    val gradientColors =
        listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)

    LaunchedEffect(isStartPressed.value) {
        while (isStartPressed.value) {
            delay(10)
            startRunGradEnd.value += 10f
        }
        if (!isStartPressed.value) {
            startRunGradEnd.value = 0f
        }
    }
    LaunchedEffect(isStopPressed.value) {
        while (isStopPressed.value) {
            delay(10)
            stopRunGradEnd.value += 10f
        }
        if (!isStopPressed.value) {
            stopRunGradEnd.value = 0f
        }
    }
    LaunchedEffect(startRun.value) {
        if (startRun.value) {
            startRunAlpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 300)
            )
            stopRunAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 300)
            )
        } else {
            startRunAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 300)
            )
            stopRunAlpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 300)
            )
        }
    }


    AnimatedVisibility(
        visible = startRunAlpha.value > 0,
        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 0))
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .alpha(startRunAlpha.value)
                .pointerInteropFilter { event ->
                    when {
                        event.action == MotionEvent.ACTION_DOWN -> {
                            isStartPressed.value = true
                            true
                        }

                        event.action == MotionEvent.ACTION_UP -> {
                            isStartPressed.value = false
                            if (startRunGradEnd.value > 1000f) {
                                startRun.value = true
                            }
                            onClick()
                            true
                        }

                        else -> false
                    }
                }
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .background(
                    brush = Brush.horizontalGradient(
                        gradientColors,
                        startX = 0f,
                        endX = startRunGradEnd.value,
                    ),
                    shape = CircleShape,
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AnimatedVisibility(startRunGradEnd.value < 1000f) {
                    Row() {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(text = "长按2s开始运动", color = MaterialTheme.colorScheme.onPrimary)
                    }
                } 
                AnimatedVisibility(visible = startRunGradEnd.value >= 1000f){
                    Row() {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(text = "松开以开始运动", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }

    AnimatedVisibility(
        visible = startRunAlpha.value <= 0,
        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 0))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .alpha(stopRunAlpha.value)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        ),
                        startX = 0f,
                        endX = stopRunGradEnd.value,
                    ),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            if(startRun.value){
                AnimatedVisibility(pauseRun.value && !isStopPressed.value){
                    Text(text = "跑步暂停中,剩余时间：", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
                AnimatedVisibility(startRun.value && isStopPressed.value && stopRunGradEnd.value < 1000f){
                    Text(text = "松开暂停跑步，继续长按1s结束跑步", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
                AnimatedVisibility(startRun.value && isStopPressed.value && stopRunGradEnd.value > 1000f){
                    Text(text = "松开结束跑步", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
                AnimatedVisibility(startRun.value && !isStopPressed.value && !pauseRun.value){
                    Text(text = "正在跑步,长按暂停键结束跑步", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "步频", fontSize = 12.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onPrimary)
                    Text(text = "0", fontSize = 12.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onPrimary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "距离", fontSize = 12.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onPrimary)
                    Text(text = "0", fontSize = 12.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onPrimary)
                }
                IconButton(
                    modifier = Modifier.pointerInteropFilter { event ->
                    when {
                        event.action == MotionEvent.ACTION_DOWN -> {
                            isStopPressed.value = true
                            true
                        }

                        event.action == MotionEvent.ACTION_UP -> {
                            isStopPressed.value = false
                            if (stopRunGradEnd.value > 1000f) {
                                startRun.value = false
                            }else{
                                pauseRun.value = !pauseRun.value
                            }
                            true
                        }

                        else -> false
                    }
                },
                    onClick = {  }
                ) {
                    if(pauseRun.value){
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary)
                    }else{
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "配速", fontSize = 12.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onPrimary)
                    Text(text = "0", fontSize = 12.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onPrimary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "时长", fontSize = 12.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onPrimary)
                    Text(text = "0", fontSize = 12.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }

}