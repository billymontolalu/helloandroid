package com.example.helloandroid.frontend

import android.content.ClipData.Item
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.helloandroid.data.LoginData
import com.example.helloandroid.data.RegisterData
import com.example.helloandroid.respon.LoginRespon
import com.example.helloandroid.respon.UserRespon
import com.example.helloandroid.service.LoginService
import com.example.helloandroid.service.RegisterService
import com.example.helloandroid.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homepage(navController: NavController, context: Context = LocalContext.current){
    //var listUser: List<UserRespon> = remember
    val listUser = remember { mutableStateListOf<UserRespon>()}
    //var listUser: List<UserRespon> by remember { mutableStateOf(List<UserRespon>()) }
    var baseUrl = "http://10.0.2.2:1337/api/"
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserService::class.java)
    val call = retrofit.getData()
    call.enqueue(object : Callback<List<UserRespon>> {
        override fun onResponse(
            call: Call<List<UserRespon>>,
            response: Response<List<UserRespon>>
        ) {
            if (response.code() == 200) {
                //kosongkan list User terlebih dahulu
                listUser.clear()
                response.body()?.forEach{ userRespon ->
                    listUser.add(userRespon)
                }
            } else if (response.code() == 400) {
                print("error login")
                var toast = Toast.makeText(
                    context,
                    "Username atau password salah",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onFailure(call: Call<List<UserRespon>>, t: Throwable) {
            print(t.message)
        }

    })
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("createuserpage")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "Homepage") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },) {
            innerPadding ->
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            ) {
            LazyColumn{
                listUser.forEach { user ->
                    item {
                        Row (modifier = Modifier.padding(10.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = user.username)
                            ElevatedButton(onClick = {
                                val retrofit = Retrofit.Builder()
                                    .baseUrl(baseUrl)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()
                                    .create(UserService::class.java)
                                val call = retrofit.delete(user.id)
                                call.enqueue(object : Callback<UserRespon>{
                                    override fun onResponse(call: Call<UserRespon>, response: Response<UserRespon>) {
                                        print(response.code())
                                        if(response.code() == 200){
                                        }else if(response.code() == 400){
                                            print("error login")
                                            var toast = Toast.makeText(context, "Username atau password salah", Toast.LENGTH_SHORT).show()

                                        }
                                    }

                                    override fun onFailure(call: Call<UserRespon>, t: Throwable) {
                                        print(t.message)
                                    }

                                })
                            }) {
                                Text("Delete")
                            }
                            ElevatedButton(onClick = {
                                navController.navigate("edituserpage/" + user.id + "/" + user.username)
                            }) {
                                Text("Edit")
                            }
                        }
                    }
                }
            }

        }
    }
}