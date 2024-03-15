package com.scxy.wztp.ui.components.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.psl.schoolrun.R
import com.scxy.wztp.nav.MainNavConfig

@Composable
fun MainBottomBar(navController: NavController) {

    var selectedItem by remember { mutableStateOf(0) }
    val navBackStackEntity by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntity?.destination

    NavigationBar(
        Modifier
            .height(64.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(MaterialTheme.shapes.large)

    ) {
        navItems.forEach{item->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any{it.route == item.navigate } == true,
                onClick = {
                    selectedItem = item.id
                    navController.navigate(item.navigate){
                        navController.popBackStack()
                    }
                },
                icon = { Icon(painter = painterResource(id = item.resId), contentDescription = null)},
                label = {
                        if(currentDestination?.hierarchy?.any{it.route == item.navigate } == false){
//                            Text(text = item.name , fontSize = 10.sp)
                        }
                },
            )
        }
    }
}
data class ImageItem(
    val id: Int,
    val name: String,
    val navigate: String,
    val resId: Int
)

val navItems = listOf(
    ImageItem(0,"主页",MainNavConfig.HOME_PAGE, R.drawable.sv_home),
    ImageItem(1,"我的",MainNavConfig.MINE_PAGE, R.drawable.sv_user)
)