package com.psl.schoolrun.ui.pages

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.psl.schoolrun.DataCatchActivity
import com.psl.schoolrun.api.RetrofitClient
import com.psl.schoolrun.ui.components.common.MenuItem
import com.psl.schoolrun.ui.components.common.SmallMenuItem
import com.psl.schoolrun.ui.components.user.UserCard
import com.scxy.wztp.nav.MainNavConfig
import com.scxy.wztp.ui.components.main.MainBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinePage(navController: NavHostController) {

    val context: Context = LocalContext.current

    Scaffold(
        bottomBar = { MainBottomBar(navController) }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(vertical = 32.dp, horizontal = 16.dp)
        ){
            item {
                Text(text = "我的", fontSize = 26.sp, modifier = Modifier.padding(vertical = 16.dp, horizontal = 6.dp))
                UserCard(navController)

                Column(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.medium)
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        SmallMenuItem(menuText = "排行榜", icon = Icons.Default.DateRange) {

                        }
                        SmallMenuItem(menuText = "设置", icon = Icons.Default.Settings) {

                        }
                        SmallMenuItem(menuText = "关于", icon = Icons.Default.Info) {

                        }

                    }
                }

                Column(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.medium)
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        SmallMenuItem(menuText = "打卡点管理", icon = Icons.Default.List) {
                            navController.navigate(MainNavConfig.POINT_MANAGER_PAGE)
                        }
                        SmallMenuItem(menuText = "数据收集", icon = Icons.Default.List) {
                            val intent = Intent(context,DataCatchActivity::class.java)
                            context.startActivity(intent)
                        }
                        SmallMenuItem(menuText = "数据收集", icon = Icons.Default.List) {
                            val intent = Intent(context,DataCatchActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}