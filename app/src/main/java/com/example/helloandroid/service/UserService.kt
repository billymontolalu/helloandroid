package com.example.helloandroid.service

import com.example.helloandroid.data.LoginData
import com.example.helloandroid.respon.LoginRespon
import com.example.helloandroid.respon.UserRespon
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @GET("users")
    fun getData() : Call<List<UserRespon>>

    @DELETE("users/{id}")
    fun delete(@Path("id") id : Int) : Call<UserRespon>
}