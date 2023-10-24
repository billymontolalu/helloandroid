package com.example.helloandroid.respon

import com.google.gson.annotations.SerializedName

class UserRespon {
    var id: Int = 0
    var username : String = ""
    var email : String = ""
    var provider : String = ""
    var confirmed: String = ""
    var blocked: Boolean = false
    var createdAt : String = ""
    var updatedAt: String = ""
}