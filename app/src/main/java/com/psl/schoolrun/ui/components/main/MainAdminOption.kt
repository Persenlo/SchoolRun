package com.psl.schoolrun.ui.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.amap.api.maps.model.LatLng
import com.psl.schoolrun.model.CheckPointRequest
import com.psl.schoolrun.viewmodel.manager.MainViewModelManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAdminOption(latlng: LatLng?, onClose: () -> Unit) {

    val mainVM = MainViewModelManager.mainViewModel
    val context = LocalContext.current

    val showCheckPointCreate = remember { mutableStateOf(false) }

    AlertDialog(
        title = { Text(text = "管理员选项") },
        text = {
            LazyColumn {
                item {
                    TextButton(onClick = { showCheckPointCreate.value = true }) {
                        Text(text = "在当前位置设置打卡点")
                    }
                }
            }
        },
        onDismissRequest = { onClose() },
        confirmButton = {
            TextButton(onClick = { onClose() }) {
                Text(text = "关闭")
            }
        }
    )


    if (showCheckPointCreate.value) {
        val data = remember { mutableStateOf(CheckPointRequest(1.0, 1.0, "1", "", "", 0.0f)) }
        if(latlng != null){
            data.value.pointLat = latlng.latitude
            data.value.pointLon = latlng.longitude
        }
        AlertDialog(
            title = { Text(text = "新增打卡点") },
            text = {
                LazyColumn {
                    item {
                        OutlinedTextField(
                            value = data.value.pointName ?: "",
                            onValueChange = { data.value = data.value.copy(pointName = it) },
                            placeholder = { Text(text = "请输入名称") },
                            label = { Text(text = "名称") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = data.value.pointKey ?: "",
                            onValueChange = { data.value = data.value.copy(pointKey = it) },
                            placeholder = { Text(text = "请输入分类") },
                            label = { Text(text = "分类") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            label = { Text(text = "权重(范围1~9999)") },
                            placeholder = { Text(text = "请输入权重") },
                            value = data.value.pointWeight.toString(),
                            onValueChange = {
                                if (it.matches(Regex("^[0-9]+(\\.[0-9]+)?$")) && it != "") {
                                    if (it.toFloat() in 0f..9999f) {
                                        data.value = data.value.copy(pointWeight = it.toFloat())
                                    }
                                }
                                if (it == "") {
                                    data.value = data.value.copy(pointWeight = 0f)
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "激活打卡点")
                            Switch(
                                checked = data.value.pointActive == "1",
                                onCheckedChange = {
                                    data.value = data.value.copy(pointActive = if (it) "1" else "0")
                                }
                            )
                        }
                    }
                }
            },
            onDismissRequest = {
                showCheckPointCreate.value = false
            },
            confirmButton = {
                TextButton(onClick = {mainVM.createCheckPoint(context,data.value,showCheckPointCreate) }) {
                    Text(text = "创建")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCheckPointCreate.value = false }) {
                    Text(text = "取消")
                }
            }
        )
    }

}