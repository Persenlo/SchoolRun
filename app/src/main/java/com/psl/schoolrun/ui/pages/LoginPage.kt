package com.psl.schoolrun.ui.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.psl.schoolrun.api.ApiService
import com.psl.schoolrun.api.RetrofitClient
import com.psl.schoolrun.constant.SharedPreferencesConstant
import com.psl.schoolrun.model.LoginRequest
import com.psl.schoolrun.utils.cookieStore.PersistentCookieStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavHostController) {

    val context = LocalContext.current

    val userAcc = remember { mutableStateOf("") };
    val userPwd = remember { mutableStateOf("") }

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
        bottomBar = {
            TextButton(onClick = { /*TODO*/ }, Modifier.fillMaxWidth()) {
                Text(text = "忘记密码", textAlign = TextAlign.Center)
            }
        }
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
                        text = "登录",
                        fontSize = 26.sp,
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    OutlinedTextField(
                        value = userAcc.value,
                        onValueChange = { userAcc.value = it },
                        placeholder = { Text(text = "请输入手机号") },
                        label = { Text(text = "手机号") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = userPwd.value,
                        onValueChange = { userPwd.value = it },
                        placeholder = { Text(text = "请输入密码") },
                        label = { Text(text = "密码") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .fillMaxWidth(0.3f)
                        ) {
                            Text(text = "注册")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                //判空
                                if (userAcc.value.isEmpty() || userPwd.value.isEmpty()) {
                                    Toast.makeText(context, "账号密码不能为空", Toast.LENGTH_SHORT)
                                        .show()
                                    return@Button
                                }
                                //发送登录请求
                                val api = RetrofitClient.createApi(ApiService::class.java)
                                GlobalScope.launch {
                                    val call = api.login(LoginRequest(userAcc.value, userPwd.value))
                                    try {
                                        if (call.code == 200) {
                                            //得到token，保存到sp储存
                                            val token = call.token
                                            withContext(Dispatchers.Main) {
                                                val sp = context.getSharedPreferences(
                                                    SharedPreferencesConstant.USER_SP,
                                                    0
                                                )
                                                sp.edit().putString(
                                                    SharedPreferencesConstant.TOKEN,
                                                    "Bearer $token"
                                                ).apply()
                                                Toast.makeText(
                                                    context,
                                                    "登录成功",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                navController.popBackStack()
                                            }
                                        } else {
                                            //登录失败，在主线程弹出错误信息
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(
                                                    context,
                                                    call.msg,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    } catch (e: Exception) {
                                        Log.e("NET", e.toString())
                                    }
                                }
                            },
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .fillMaxWidth(1f)
                        ) {
                            Text(text = "登录")
                        }
                    }
                }

            }
        }
    }
}

