package com.psl.schoolrun.service

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.psl.schoolrun.api.wss.DataCatchWSS
import com.psl.schoolrun.model.RunData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DataCatchService : Service() {

    private lateinit var job: Job

    private val currentDateTime = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val formattedDateTime = currentDateTime.format(formatter)

    private val hasPermission = MutableLiveData(true)

    private val accValue =  MutableLiveData(FloatArray(3))
    private val aName =  MutableLiveData("")
    private val gyrValue =  MutableLiveData(FloatArray(3))
    private val gName =  MutableLiveData("")
    private val lat =  MutableLiveData(0.0)
    private val lon =  MutableLiveData(0.0)
    private val curType = MutableLiveData(-1L)
    private val datakey = MutableLiveData(formattedDateTime)
    private val dataServer = MutableLiveData("ws://192.168.3.13:8686/ws/dc/userid")

    private var isServiceStarted = MutableLiveData(false)

    private lateinit var dataCatchWSS: DataCatchWSS

    private val context = this

    //获取传感器对象
    lateinit var sensorManager: SensorManager
    lateinit var locationManager : LocationManager
    lateinit var accelerometerSensor : Sensor
    lateinit var gyroscopeSensor : Sensor

    val accListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // 当传感器精度改变时的处理
        }

        override fun onSensorChanged(event: SensorEvent) {
            accValue.value = event.values.clone()
            aName.value = event.sensor.name
        }
    }

    val gyrListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // 当传感器精度改变时的处理
        }

        override fun onSensorChanged(event: SensorEvent) {
            gyrValue.value = event.values.clone()
            gName.value = event.sensor.name
        }
    }

    val myLocationListener = LocationListener { location -> // 处理位置变化
        Log.e("send","test")
        lat.value = location.latitude
        lon.value = location.longitude
    }


    inner class LocalBinder : Binder() {
        fun getService(): DataCatchService = this@DataCatchService
    }

    override fun onBind(p0: Intent?): IBinder {
        return LocalBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isServiceStarted.value = true
        //链接socket
        dataCatchWSS.connect(dataServer.value!!, listener)

        job = GlobalScope.launch(Dispatchers.Main) {

            sensorManager.registerListener(
                accListener,
                accelerometerSensor,
                SensorManager.SENSOR_DELAY_UI
            )
            sensorManager.registerListener(
                gyrListener,
                gyroscopeSensor,
                SensorManager.SENSOR_DELAY_UI
            )

            // 检查是否已经授予位置权限
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission.value = false
            } else {
                Log.e("send","111")
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, // 或 LocationManager.NETWORK_PROVIDER
                    10,  // 更新间隔，以毫秒为单位
                    0.01f,  // 最小距离变化，以米为单位
                    myLocationListener
                )
            }

            //发送信息
            //创建计时器，每0.1秒将传感器和位置数据通过socket发送
            while (true) {
                delay(100)
                if (lat.value != 0.0 && lon.value != 0.0) {
                    val data = RunData(
                        accValue.value!![0],
                        accValue.value!![1],
                        accValue.value!![2],
                        gyrValue.value!![0],
                        gyrValue.value!![1],
                        gyrValue.value!![2],
                        lat.value!!,
                        lon.value!!,
                        curType.value!!,
                        datakey.value!!
                    )
                    dataCatchWSS.sendMessage(data)
                }

            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        dataCatchWSS = DataCatchWSS()
        //获取传感器对象
        sensorManager= getSystemService(Context.SENSOR_SERVICE) as SensorManager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!
    }

    fun stopCatch(){
        sensorManager.unregisterListener(accListener,accelerometerSensor)
        sensorManager.unregisterListener(gyrListener,gyroscopeSensor)
        dataCatchWSS.close()
        isServiceStarted.value = false;
        job.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("111","destory")
        dataCatchWSS.close()
        isServiceStarted.value = false
    }

    fun isServiceStarted(): LiveData<Boolean> {
        return isServiceStarted
    }


    val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
        }
    }



    fun getAccValue(): LiveData<FloatArray> {
        return accValue
    }
    fun getAName(): LiveData<String> {
        return aName
    }
    fun getGyrValue(): LiveData<FloatArray> {
        return gyrValue
    }
    fun getGName(): LiveData<String> {
        return gName
    }
    fun getLat(): LiveData<Double> {
        return lat
    }
    fun getLon(): LiveData<Double> {
        return lon
    }
    fun getCurType(): LiveData<Long> {
        return curType
    }
    fun setCurType(v: Long){
        this.curType.postValue(v)
    }
    fun getDataKey(): LiveData<String> {
        return datakey
    }
    fun setDataKey(v: String){
        this.datakey.postValue(v)
    }
    fun getPermission(): LiveData<Boolean> {
        return hasPermission
    }
    fun setPermission(v: Boolean){
        this.hasPermission.postValue(v)
    }
    fun getDataServer(): MutableLiveData<String> {
        return dataServer
    }
    fun setDataServer(v: String){
        this.dataServer.postValue(v)
    }
}