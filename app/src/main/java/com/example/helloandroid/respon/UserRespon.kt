package com.example.helloandroid.respon

import com.google.gson.annotations.SerializedName
data class UserRespon(
    val id: Int?,
    val username : String?,
    val email : String?,
    val provider : String?,
    val confirmed: String?,
    val blocked: Boolean?,
    val creastedAt : String?,
    val updatedAt: String?,
    val prodi: Prodi?
)

data class Prodi (
    val id: Long?,

    @SerializedName("nama_prodi")
    val namaProdi: String?,

    @SerializedName("jumlah_peserta")
    val jumlahPeserta: String?,

    val createdAt: String?,
    val updatedAt: String?,
    val publishedAt: String?
)