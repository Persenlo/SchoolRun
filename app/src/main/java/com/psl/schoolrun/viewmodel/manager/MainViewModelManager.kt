package com.psl.schoolrun.viewmodel.manager

import android.app.Application
import com.psl.schoolrun.viewmodel.MainViewModel

object MainViewModelManager {

    private lateinit var application: Application

    fun saveApplication(application: Application) {
        MainViewModelManager.application = application
    }

    val mainViewModel: MainViewModel by lazy {
        MainViewModel(application)
    }


}