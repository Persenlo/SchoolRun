package com.psl.schoolrun

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.psl.schoolrun.api.ApiService
import com.psl.schoolrun.api.RetrofitClient
import com.psl.schoolrun.model.RunData
import com.psl.schoolrun.model.Type
import com.psl.schoolrun.ui.theme.SchoolRunTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.psl.schoolrun.service.DataCatchService

class DataCatchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SchoolRunTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(this)
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MainView(context: DataCatchActivity,modifier: Modifier = Modifier) {

    val accValue =  remember{mutableStateOf(FloatArray(3))}
    val aName =  remember{mutableStateOf("")}
    val gyrValue =  remember{mutableStateOf(FloatArray(3))}
    val gName =  remember{mutableStateOf("")}
    val lat =  remember{mutableStateOf(0.0)}
    val lon =  remember{mutableStateOf(0.0)}
    val curType = remember{mutableStateOf(-1L)}
    val datakey = remember{mutableStateOf("default")}
    val dataServer = remember{mutableStateOf("ws://192.168.3.13:8686/ws/dc/userid")}
    val hasPermission = remember{mutableStateOf(true)}
    val startData = remember { mutableStateOf(false) }

    var dataCatchService: DataCatchService? = null
    val isServiceBound = remember{ mutableStateOf(false) }

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
            val binder = service as DataCatchService.LocalBinder
            dataCatchService = binder.getService()
            isServiceBound.value = true
            dataCatchService!!.isServiceStarted().observe(context, Observer {
                startData.value = it
            })

            dataCatchService!!.getAccValue().observe(context, Observer {
                accValue.value = it
            })
            dataCatchService!!.getAName().observe(context, Observer {
                aName.value = it
            })
            dataCatchService!!.getGyrValue().observe(context, Observer {
                gyrValue.value = it
            })
            dataCatchService!!.getGName().observe(context, Observer {
                gName.value = it
            })
            dataCatchService!!.getLat().observe(context, Observer {
                lat.value = it
            })
            dataCatchService!!.getLon().observe(context, Observer {
                lon.value = it
            })
            dataCatchService!!.getCurType().observe(context, Observer {
                curType.value = it
            })
            dataCatchService!!.getDataKey().observe(context, Observer {
                datakey.value = it
            })
            dataCatchService!!.getPermission().observe(context, Observer {
                hasPermission.value = it
            })
            dataCatchService!!.getDataServer().observe(context, Observer {
                dataServer.value = it
            })

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            dataCatchService = null
            isServiceBound.value = false
        }
    }
    val intent = Intent(context, DataCatchService::class.java)
    context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)



    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val showGetPermission = remember { mutableStateOf(false) }


    val runData = mutableListOf<RunData>()
    val dataCount = remember{ mutableStateOf(0) }

    val api = RetrofitClient.createApi(ApiService::class.java)
    var types = listOf<Type>()
    val typeGet = remember{ mutableStateOf(false) }
    LaunchedEffect(Unit){
        val call = api.getTypes()
        try {
            types = call.data
            typeGet.value = call.data.isNotEmpty()
        }catch (e:Exception){
            Log.e("NET",e.toString())
        }
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            LazyRow(
                verticalAlignment= Alignment.CenterVertically,
                modifier=Modifier
            ){
                if (typeGet.value){
                    item{
                        Text(text = "选择跑步模式：")
                    }
                    items(types){
                        FilterChip(
                            selected = curType.value == it.typeId,
                            onClick = { dataCatchService!!.setCurType(it.typeId) },
                            label = { Text(text = it.typeName) },
                            modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
        }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "数据集名称")
                OutlinedTextField(
                    value = datakey.value,
                    onValueChange = { dataCatchService!!.setDataKey(it) }
                )
            }
        }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "数据服务器")
                OutlinedTextField(
                    value = dataServer.value,
                    onValueChange = { dataCatchService!!.setDataServer(it) }
                )
            }
        }
        item {
            Button(onClick = {
                if (curType.value != -1L){
                    if (startData.value) {
                        dataCatchService!!.stopCatch()
                        dataCatchService!!.stopSelf()
                        Log.e("111", (dataCatchService == null).toString()+"stop")
                    } else {
                        context.startService(intent)
                        runData.clear()
                        dataCount.value = 0

                    }
                }else{
                    return@Button
                }

            }) {
                if (!startData.value) {
                    Text(text = "开始获取数据")
                } else {
                    Text(text = "结束获取数据")
                }
            }
        }
        items(1) {
            Text(text = "加速度" + aName.value)
            Row {
                accValue.value.forEach { value ->
                    Text(text = "/"+String.format("%.2f", value))
                }
            }
            Text(text = "陀螺仪" + gName.value)
            Row {
                gyrValue.value.forEach { value ->
                    Text(text = "/"+String.format("%.2f", value))
                }
            }
            Text(text = "位置")
            Row {
                Text(text = "经度" + lat.value)
                Text(text = "纬度" + lon.value)
            }
        }
        if (startData.value == false) {

        }
    }

    if (!hasPermission.value) {
        AlertDialog(
            title = { Text(text = "授权提示") },
            text = {
                Column() {
                    Text(text = "使用Run需要获取以下权限：")
                    Text(text = "位置信息")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        locationPermissionState.launchPermissionRequest()
                        showGetPermission.value = false
                    }
                ) {
                    Text(text = "授权")
                }
            },
            onDismissRequest = { },
        )
    }



}