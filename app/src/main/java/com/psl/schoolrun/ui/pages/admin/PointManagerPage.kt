package com.psl.schoolrun.ui.pages.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.psl.schoolrun.api.ApiService
import com.psl.schoolrun.api.RetrofitClient
import com.psl.schoolrun.model.CheckPointModel
import com.psl.schoolrun.viewmodel.manager.MainViewModelManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PointManagerPage(navController: NavHostController) {

    val mainVM = MainViewModelManager.mainViewModel
    val context = LocalContext.current
    val pointList = remember{ mutableStateOf<List<CheckPointModel>>(emptyList())}
    val showEditPoint = remember{ mutableStateOf(false) }
    val showDelete = remember{ mutableStateOf(false) }
    val index = remember{ mutableIntStateOf(-1) }
    val update = remember{ mutableStateOf(false) }

    fun getData(){
        GlobalScope.launch {
            val api = RetrofitClient.createApi(ApiService::class.java)
            val call = api.listPoint(mainVM.token.value!!)
            if(call.code == 200){
                pointList.value = call.rows
            }else{
                withContext(Dispatchers.Main){
                    Toast.makeText(context,call.msg,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    LaunchedEffect(mainVM.token.value){
        getData()
    }
    LaunchedEffect(update.value){
        getData()
        update.value = false
    }


    Scaffold(
        topBar = {
            IconButton(
                onClick = { navController.popBackStack() },
                Modifier
                    .padding(top = 32.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "返回")
            }
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                ) {
                    Text(
                        text = "打卡点管理",
                        fontSize = 26.sp,
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))


            }
            itemsIndexed(pointList.value){i, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .background(
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.shapes.medium
                        )
                        .clickable {
                            index.intValue = i
                            showEditPoint.value = true
                        }
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                ) {
                    Column (
                        modifier = Modifier
                    ){
                        Text(text = item.pointName!!, color = MaterialTheme.colorScheme.onSecondary, fontWeight = FontWeight.Bold)
                        Text(text = "经度：${item.pointLon!!}", color = MaterialTheme.colorScheme.onSecondary)
                        Text(text = "纬度：${item.pointLat!!}", color = MaterialTheme.colorScheme.onSecondary)
                    }
                    IconButton(onClick = {
                        index.intValue = i
                        showDelete.value = true
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "删除", tint = MaterialTheme.colorScheme.onSecondary)
                    }
                }
            }
        }
    }

    //编辑
    if (showEditPoint.value){
        if(index.intValue == -1){
            showEditPoint.value = false
        }
        val data = remember{ mutableStateOf(pointList.value[index.intValue]) }
        AlertDialog(
            title = { Text(text = "编辑打卡点") },
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
                showEditPoint.value = false
            },
            confirmButton = {
                TextButton(onClick = {mainVM.editCheckPoint(context,data.value,showEditPoint,update) }) {
                    Text(text = "修改")
                }

            },
            dismissButton = {
                TextButton(onClick = { showEditPoint.value = false }) {
                    Text(text = "取消")
                }
            }
        )
    }
    //删除
    if (showDelete.value){
        if(index.intValue == -1){
            showDelete.value = false
        }
        val data = remember{ mutableStateOf(pointList.value[index.intValue]) }
        AlertDialog(
            title = { Text(text = "删除打卡点") },
            text = {
                Text(text = "确认删除名称为“${data.value.pointName}”的打卡点吗？")
            },
            onDismissRequest = {
                showDelete.value = false
            },
            confirmButton = {
                TextButton(onClick = {mainVM.deleteCheckPoint(context,data.value.pointId!!,showDelete,update) }) {
                    Text(text = "删除", color = MaterialTheme.colorScheme.error)
                }

            },
            dismissButton = {
                TextButton(onClick = { showDelete.value = false }) {
                    Text(text = "取消")
                }
            }
        )
    }

}