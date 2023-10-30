package com.example.helloandroid

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.helloandroid.data.LoginData
import com.example.helloandroid.frontend.CreateUserPage
import com.example.helloandroid.frontend.EditUserPage
import com.example.helloandroid.frontend.Homepage
import com.example.helloandroid.respon.LoginRespon
import com.example.helloandroid.service.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //val preferencesManager = remember { PreferencesManager(context = LocalContext.current) }
            val sharedPreferences: SharedPreferences = LocalContext.current.getSharedPreferences("auth", Context.MODE_PRIVATE)
            val navController = rememberNavController()

            var startDestination: String
            var jwt = sharedPreferences.getString("jwt", "")
            if(jwt.equals("")){
                startDestination = "greeting"
            }else{
                startDestination = "pagetwo"
            }

            NavHost(navController, startDestination = startDestination) {
                composable(route = "greeting") {
                    Greeting(navController)
                }
                composable(route = "pagetwo") {
                    Homepage(navController)
                }
                composable(route = "createuserpage") {
                    CreateUserPage(navController)
                }
                composable(
                    route = "edituserpage/{userid}/{username}",
                    ) {backStackEntry ->

                    EditUserPage(navController, backStackEntry.arguments?.getString("userid"), backStackEntry.arguments?.getString("username"))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(navController: NavController, context: Context = LocalContext.current) {

    val preferencesManager = remember { PreferencesManager(context = context) }
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var baseUrl = "http://10.0.2.2:1337/api/"
    var jwt by remember { mutableStateOf("") }

    jwt = preferencesManager.getData("jwt")
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Login") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            BottomAppBar {
                Text(
                    text = "QRis",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    ){
        innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(value = username, onValueChange = { newText ->
                username = newText
            }, label = { Text("Username") })
            OutlinedTextField(value = password, onValueChange = { newText ->
                password = newText
            }, label = { Text("Password") })
            ElevatedButton(onClick = {
                //navController.navigate("pagetwo")
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(LoginService::class.java)
                val call = retrofit.getData(LoginData(username.text, password.text))
                call.enqueue(object : Callback<LoginRespon>{
                    override fun onResponse(call: Call<LoginRespon>, response: Response<LoginRespon>) {
                        print(response.code())
                        if(response.code() == 200){
                            jwt = response.body()?.jwt!!
                            preferencesManager.saveData("jwt", jwt)
                            navController.navigate("pagetwo")
                        }else if(response.code() == 400){
                            print("error login")
                            var toast = Toast.makeText(context, "Username atau password salah", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<LoginRespon>, t: Throwable) {
                        print(t.message)
                    }

                })
            }) {
                Text(text = "Submit")
            }
            Text(text = jwt)
        }
    }

}
