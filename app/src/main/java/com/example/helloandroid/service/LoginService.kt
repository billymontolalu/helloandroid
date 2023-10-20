package com.example.helloandroid.service

import com.example.helloandroid.data.LoginData
import com.example.helloandroid.respon.LoginRespon
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("auth/local")
    fun getData(@Body body: LoginData) : Call<LoginRespon>
}