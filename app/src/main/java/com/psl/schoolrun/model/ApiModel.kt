package com.psl.schoolrun.model

import java.util.Objects

data class ResponseModel(
    val data: Objects,
    val code: Int,
    val msg: String
)

data class LoginResponseModel(
    val token: String,
    val code: Int,
    val msg: String
)

data class UserDataResponseModel(
    val roles: Array<String>,
    val permissions: Array<String>,
    val code: Int,
    val msg: String,
    val user: UserModel
)
data class UserModel(
    val admin: Boolean,
    val avatar: String,
    val nickName: String,
    val remark: String,
    val sex: String,
    val userId: Long,
    val userName: String,
)

data class ResponseType(
    val data: List<Type>,
    val code: Int,
    val msg: String
)

data class Type(
    val typeId: Long,
    val typeName: String,
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class PointResponseModel(
    val total: Int,
    val rows: List<CheckPointModel>,
    val code: Int,
    val msg: String
)
data class CheckPointRequest(
    var pointLat: Double?,
    var pointLon: Double?,
    var pointActive: String?,
    var pointKey: String?,
    var pointName: String?,
    var pointWeight: Float?,
)
data class CheckPointModel(
    var pointId: Long?,
    var pointLat: Double?,
    var pointLon: Double?,
    var pointActive: String?,
    var pointKey: String?,
    var pointName: String?,
    var pointWeight: Float?,
)



