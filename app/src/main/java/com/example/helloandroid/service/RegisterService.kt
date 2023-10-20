package com.example.helloandroid.service

import com.example.helloandroid.respon.LoginRespon
import com.example.helloandroid.data.RegisterData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("auth/local/register")
    fun saveData(@Body body: RegisterData) : Call<LoginRespon>
}