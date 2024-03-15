package com.psl.schoolrun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.psl.schoolrun.api.RetrofitClient
import com.psl.schoolrun.ui.theme.SchoolRunTheme
import com.psl.schoolrun.utils.GDMapUtils
import com.psl.schoolrun.utils.SDKUtils
import com.psl.schoolrun.viewmodel.manager.MainViewModelManager
import com.scxy.wztp.nav.MainNav

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //初始化
        SDKUtils.init(this.application)
        GDMapUtils.updateMapViewPrivacy(this.applicationContext)

        //初始化ViewModel
        MainViewModelManager.saveApplication(this.application)

        //初始化retrofit
        RetrofitClient.init(this.application)
        RetrofitClient.setBaseUrl("http://v6.persenlo.top:8686")

        setContent {
            //初始化viewModel
//            val mainViewModel: MainViewModel = viewModel()
//            val dataCatchViewModel: DataCatchViewModel = viewModel()
//            SCViewModelFactory.initMainViewModel(mainViewModel)
//            SCViewModelFactory.initDCViewModel(dataCatchViewModel)
            //初始化界面
            SchoolRunTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView()
                }
            }
        }
    }
}

@Composable
private fun MainView(){

    val mainNavController = rememberNavController()

    MainNav(navController = mainNavController)

}


