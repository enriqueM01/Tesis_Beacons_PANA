package com.example.prototipobeacons.activities

import android.content.Intent
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
import com.example.prototipobeacons.databinding.ActivityLoginBinding
import com.example.prototipobeacons.databinding.ActivityRequestHelpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    val gson = Gson()
    val callback = object : Callback<UserResponse> {
        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {

            when (response.code()) {
                200 -> {
                    Log.i(TAG, "access token: ${response.body()?.accessToken}")
                    val intent = Intent(this@Login,RequestHelpActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                401 -> {
                    Snackbar.make(binding.loginButton,"Credenciales inválidas", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
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

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
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

    }
}




