package com.example.prototipobeacons.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.prototipobeacons.R
import com.example.prototipobeacons.UserRequest
import com.example.prototipobeacons.UserResponse
import com.example.prototipobeacons.api.Api
import com.example.prototipobeacons.api.RetrofitClientInstance
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    val gson = Gson()
    val callback = object : Callback<UserResponse> {
        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
            val errorMessageText = findViewById<TextView>(R.id.login_error_msg)

            when (response.code()) {
                200 -> {
                    Log.i(TAG, "access token: ${response.body()?.accessToken}")
                }
                401 -> {
                    Log.i(TAG, "Credenciales invalidas")
                    errorMessageText.text = "Credenciales inválidas"
                }
            }
        }

        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            Log.i("[FAILURE]", gson.toJson(t))
        }
    }

    companion object {
        const val TAG = "[LOGIN]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initAction()
    }

    fun initAction() {
        val btn_login = findViewById<Button>(R.id.button)
        btn_login.setOnClickListener {
            login()
        }
    }

    fun login() {
        val request = UserRequest()
        //* obtencion de textos
        val email1 = findViewById<EditText>(R.id.email_input)
        val password1 = findViewById<EditText>(R.id.password_input)
        request.email = email1.text.toString().trim()
        request.password = password1.text.toString().trim()

        val retro = RetrofitClientInstance().getRetrofitClientInstance().create(Api::class.java)
        retro.login(request).enqueue(callback)
        /*
        retro.login(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.i("[LoginResponse]", gson.toJson(response))

                val user = response.body()
                user!!.data?.token?.let { Log.e("token", it) }
                user.data?.email?.let { Log.e("email", it) }
               if (response.code() == 200) {
                    Toast.makeText(this@Login, "Autenticación Exitosa!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Login, "Autenticación Fallida!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.i("[LoginResponse]", gson.toJson(t))
                t.message?.let { Log.e("Error", it) }
                Toast.makeText(
                    this@Login,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
         */
    }
}




