package com.psl.schoolrun.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.psl.schoolrun.api.ApiService
import com.psl.schoolrun.api.RetrofitClient
import com.psl.schoolrun.constant.SharedPreferencesConstant
import com.psl.schoolrun.model.CheckPointModel
import com.psl.schoolrun.model.CheckPointRequest
import com.psl.schoolrun.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val userSP = application.getSharedPreferences(SharedPreferencesConstant.USER_SP, 0)

    //用户token
    val token = mutableStateOf(userSP.getString(SharedPreferencesConstant.TOKEN, ""))

    //用户信息
    val userInfo = mutableStateOf(UserModel(false, "", "用户名", "备注", "1", -1L, "用户名"))

    //设置sp监听，监听对应键值对的变化
    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            SharedPreferencesConstant.TOKEN -> {
                token.value = userSP.getString(SharedPreferencesConstant.TOKEN, "")!!
            }
        }
    }

    init {
        userSP.registerOnSharedPreferenceChangeListener(listener)
    }


    fun getUserInfo(context: Context) {
        GlobalScope.launch {
            if (token.value?.isNotEmpty() == true) {
                val api = RetrofitClient.createApi(ApiService::class.java)
                val call = api.getUserInfo(token.value!!)
                if (call.code == 200) {
                    userInfo.value = call.user
                } else {
                    //登录失败，在主线程弹出错误信息
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, call.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun createCheckPoint(
        context: Context,
        value: CheckPointRequest,
        showDialog: MutableState<Boolean>
    ) {
        GlobalScope.launch {
            if (token.value?.isNotEmpty() == true) {
                val api = RetrofitClient.createApi(ApiService::class.java)
                val call = api.createCheckPoint(token.value!!, value)
                if (call.code == 200) {
                    showDialog.value = false
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, call.msg, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    //失败，在主线程弹出错误信息
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, call.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    fun editCheckPoint(
        context: Context,
        value: CheckPointModel,
        showDialog: MutableState<Boolean>,
        update: MutableState<Boolean>
    ) {
        GlobalScope.launch {
            if (token.value?.isNotEmpty() == true) {
                val api = RetrofitClient.createApi(ApiService::class.java)
                val call = api.editPoint(token.value!!, value)
                if (call.code == 200) {
                    showDialog.value = false
                    update.value = true
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, call.msg, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    //失败，在主线程弹出错误信息
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, call.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    fun deleteCheckPoint(
        context: Context,
        value: Long,
        showDialog: MutableState<Boolean>,
        update: MutableState<Boolean>
    ) {
        GlobalScope.launch {
            if (token.value?.isNotEmpty() == true) {
                val api = RetrofitClient.createApi(ApiService::class.java)
                val call = api.deletePoint(token.value!!, value)
                if (call.code == 200) {
                    showDialog.value = false
                    update.value = true
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, call.msg, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    //失败，在主线程弹出错误信息
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, call.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}