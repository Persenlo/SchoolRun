package com.psl.schoolrun.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel

class DataCatchViewModel: ViewModel() {

    val hasPermission = mutableStateOf(true)

    val accValue =  mutableStateOf(FloatArray(3))
    val aName =  mutableStateOf("")
    val gyrValue =  mutableStateOf(FloatArray(3))
    val gName =  mutableStateOf("")
    val lat =  mutableStateOf(0.0)
    val lon =  mutableStateOf(0.0)
    val curType = mutableStateOf(-1L)
    val datakey = mutableStateOf("default")
}