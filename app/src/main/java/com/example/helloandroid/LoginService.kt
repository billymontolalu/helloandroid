package com.example.helloandroid

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("auth/local")
    fun getData(@Body body:LoginData) : Call<LoginRespon>
}