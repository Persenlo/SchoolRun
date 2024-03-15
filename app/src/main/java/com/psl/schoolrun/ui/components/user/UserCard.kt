package com.psl.schoolrun.ui.components.user

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.psl.schoolrun.api.ApiService
import com.psl.schoolrun.api.RetrofitClient
import com.psl.schoolrun.viewmodel.manager.MainViewModelManager
import com.scxy.wztp.nav.MainNavConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun UserCard(navController: NavHostController) {

    val mainVM = MainViewModelManager.mainViewModel
    val context = LocalContext.current

    val token = mainVM.token

    LaunchedEffect(token.value){
        mainVM.getUserInfo(context)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding()
                .fillMaxWidth()
                .clickable {
                    navController.navigate(MainNavConfig.LOGIN_PAGE)
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(model = RetrofitClient.BASE_URL+mainVM.userInfo.value.avatar, contentDescription = "用户头像", modifier = Modifier
                    .width(56.dp)
                    .height(56.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    if(token.value!!.isNotEmpty()){
                        Text(text = mainVM.userInfo.value.userName)
                        Text(text = "进入个人中心", fontSize = 14.sp)
                    }
                }
            }
            if(token.value!!.isEmpty()){
                Text(text = "登录>")
            }
        }
        //分割线
        Spacer(modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .height(1.dp)
            .alpha(0.15f)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.primary))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "本学期目标", fontSize = 14.sp)
                Text(text = "40", textAlign = TextAlign.Center)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "本学期达标", fontSize = 14.sp)
                Text(text = "0", textAlign = TextAlign.Center)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "本学期跑步", fontSize = 14.sp)
                Text(text = "0", textAlign = TextAlign.Center)
            }
        }
    }
}