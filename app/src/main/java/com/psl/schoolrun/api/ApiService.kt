package com.psl.schoolrun.api

import com.psl.schoolrun.model.CheckPointModel
import com.psl.schoolrun.model.CheckPointRequest
import com.psl.schoolrun.model.LoginRequest
import com.psl.schoolrun.model.LoginResponseModel
import com.psl.schoolrun.model.PointResponseModel
import com.psl.schoolrun.model.ResponseModel
import com.psl.schoolrun.model.ResponseType
import com.psl.schoolrun.model.UserDataResponseModel
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("/catch/send")
    suspend fun sendData(@Body requestBody: RequestBody): ResponseModel

    @GET("/catch/types")
    suspend fun getTypes(): ResponseType

    @POST("/login")
    suspend fun login(@Body requestBody: LoginRequest): LoginResponseModel

    @GET("/getInfo")
    suspend fun getUserInfo(@Header("Authorization") token: String): UserDataResponseModel

    @POST("/ai/point")
    suspend fun createCheckPoint(@Header("Authorization") token: String, @Body requestBody: CheckPointRequest): ResponseModel

    @GET("/ai/point/list/all")
    suspend fun listPoint(@Header("Authorization") token: String): PointResponseModel

    @PUT("/ai/point")
    suspend fun editPoint(@Header("Authorization") token: String, @Body requestBody: CheckPointModel): ResponseModel

    @DELETE("/ai/point/{id}")
    suspend fun deletePoint(@Header("Authorization") token: String, @Path("id") id: Long): ResponseModel

}