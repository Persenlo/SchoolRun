package com.psl.schoolrun

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.psl.schoolrun.viewmodel.DataCatchViewModel
import com.psl.schoolrun.viewmodel.MainViewModel
import com.psl.schoolrun.viewmodel.manager.MainViewModelManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}