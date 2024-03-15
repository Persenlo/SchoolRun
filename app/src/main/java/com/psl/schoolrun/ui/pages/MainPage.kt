package com.psl.schoolrun.ui.pages

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.melody.map.gd_compose.GDMap
import com.melody.map.gd_compose.position.rememberCameraPositionState
import com.psl.schoolrun.dialog.ShowOpenGPSDialog
import com.psl.schoolrun.launcher.handlerGPSLauncher
import com.psl.schoolrun.ui.components.main.MainAdminOption
import com.psl.schoolrun.ui.components.main.MainRunTopBar
import com.psl.schoolrun.ui.components.main.RunDetail
import com.psl.schoolrun.utils.requestMultiplePermission
import com.psl.schoolrun.viewmodel.LocationTrackingViewModel
import com.psl.schoolrun.viewmodel.manager.MainViewModelManager
import com.scxy.wztp.ui.components.main.MainBottomBar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MainPage(navController: NavHostController) {

    //获取屏幕高度
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    //计算合适的地图高度
    val mapViewHeight = ((screenHeightDp - 72.dp) / 5) * 3

    val showAdminOption = remember{ mutableStateOf(false) }

    val mainVM = MainViewModelManager.mainViewModel

    //地图加载箱
    val viewModel: LocationTrackingViewModel = viewModel()
    val currentState by viewModel.uiState.collectAsState()
    val cameraPosition = rememberCameraPositionState {
        // 不预加载显示默认北京的位置
        position = CameraPosition(LatLng(39.54, 116.23), 18f, 0f, 0f)
    }

    val openGpsLauncher = handlerGPSLauncher(viewModel::checkGpsStatus)
    val reqGPSPermission = requestMultiplePermission(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),
        onGrantAllPermission = viewModel::handleGrantLocationPermission,
        onNoGrantPermission = viewModel::handleNoGrantLocationPermission
    )
    LaunchedEffect(Unit) {
        snapshotFlow { reqGPSPermission.allPermissionsGranted }.collect {
            // 从app应用权限开关页面，打开权限，需要再检查一下GPS开关
            viewModel.checkGpsStatus()
        }
    }

    LaunchedEffect(currentState.locationLatLng) {
        if (null == currentState.locationLatLng) return@LaunchedEffect
        cameraPosition.move(CameraUpdateFactory.newLatLng(currentState.locationLatLng))
    }

    LaunchedEffect(currentState.isOpenGps, reqGPSPermission.allPermissionsGranted) {
        if (currentState.isOpenGps == true) {
            if (!reqGPSPermission.allPermissionsGranted) {
                reqGPSPermission.launchMultiplePermissionRequest()
            } else {
                viewModel.startMapLocation()
            }
        }
    }

    if (currentState.isShowOpenGPSDialog) {
        ShowOpenGPSDialog(
            onDismiss = viewModel::hideOpenGPSDialog,
            onPositiveClick = {
                viewModel.openGPSPermission(openGpsLauncher)
            }
        )
    }

    Scaffold(
        bottomBar = { MainBottomBar(navController) }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .fillMaxSize()
        ) {
            item {
                MainRunTopBar(mainVM = mainVM, showAdminOption = showAdminOption)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(mapViewHeight)
                        .clip(MaterialTheme.shapes.large)
                ) {
                    // 地图
                    GDMap(
                        modifier = Modifier.matchParentSize(),
                        cameraPositionState = cameraPosition,
                        properties = currentState.mapProperties,
                        uiSettings = currentState.mapUiSettings,
                        locationSource = viewModel,
                        onMapLoaded = viewModel::checkGpsStatus
                    )
                }
            }
            item{
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                RunDetail(onClick = { /*TODO*/ })
            }
        }
    }

    if (showAdminOption.value){
        MainAdminOption(currentState.locationLatLng){
            showAdminOption.value = false
        }
    }
}