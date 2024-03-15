package com.psl.schoolrun.model

data class RunData(
    val dataAccelerationX: Float,
    val dataAccelerationY: Float,
    val dataAccelerationZ: Float,
    val dataGyroscopeX: Float,
    val dataGyroscopeY: Float,
    val dataGyroscopeZ: Float,
    val dataLatitude: Double,
    val dataLongitude: Double,
    val typeId: Long,
    val dataKey: String,
)
