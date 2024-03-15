package com.scxy.wztp.nav

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.psl.schoolrun.ui.pages.LoginPage
import com.psl.schoolrun.ui.pages.MainPage
import com.psl.schoolrun.ui.pages.MinePage
import com.psl.schoolrun.ui.pages.admin.PointManagerPage


@Composable
fun MainNav(
    navController: NavHostController,
){

    //需要为底部导航栏加padding
    val navPadding = Modifier.windowInsetsPadding(WindowInsets.navigationBars)

    NavHost(navController = navController, startDestination = MainNavConfig.HOME_PAGE, modifier = navPadding){
        composable(MainNavConfig.HOME_PAGE){
            MainPage(navController)
        }
        composable(MainNavConfig.MINE_PAGE){
            MinePage(navController)
        }
        composable(MainNavConfig.LOGIN_PAGE){
            LoginPage(navController)
        }
        composable(MainNavConfig.POINT_MANAGER_PAGE){
            PointManagerPage(navController)
        }
    }

}