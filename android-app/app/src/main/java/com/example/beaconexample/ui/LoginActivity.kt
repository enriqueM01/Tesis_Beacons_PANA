package com.example.beaconexample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import com.example.beaconexample.R
import com.example.beaconexample.constants.LoginFormCodes
import com.example.beaconexample.databinding.ActivityLoginBinding
import com.example.beaconexample.utils.afterTextChanged
import com.example.beaconexample.utils.goToActivity
import com.example.beaconexample.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private var passwordShown = false
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailInput = binding.emailInput
        passwordInput = binding.passwordInput
        loginButton = binding.loginButton

        loginViewModel.formState.observe(this, { state ->
            when (state.code) {
                LoginFormCodes.INVALID_EMAIL -> {
                    binding.loginEmailErrorMsg.text = state.message
                    loginButton.isEnabled = false
                }
                LoginFormCodes.INVALID_PASSWORD -> {
                    binding.loginPasswordErrorMsg.text = state.message
                    loginButton.isEnabled = false
                }
                else -> {
                    binding.loginEmailErrorMsg.text = ""
                    binding.loginPasswordErrorMsg.text = ""
                    loginButton.isEnabled = true
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()

        emailInput.afterTextChanged {
            loginViewModel.detectLoginDataChange(
                emailInput.text.toString(),
                passwordInput.text.toString(),
            )
        }

        passwordInput.afterTextChanged {
            loginViewModel.detectLoginDataChange(
                emailInput.text.toString(),
                passwordInput.text.toString(),
            )
        }

        binding.togglePasswordIcon.setOnClickListener {
            if (!passwordShown) {
                binding.togglePasswordIcon.setImageResource(R.drawable.hide_password_icon)
                binding.passwordInput.transformationMethod = null
            } else {
                binding.togglePasswordIcon.setImageResource(R.drawable.show_password_icon)
                binding.passwordInput.transformationMethod = PasswordTransformationMethod()
            }
            passwordShown = !passwordShown
        }

        loginButton.setOnClickListener { view ->
            (view as Button).text = resources.getText(R.string.login_btn_loading_text)
            view.isEnabled = false
            loginViewModel.login().observe(this, { user ->
                if (user != null) {
                    goToActivity(MainActivity::class.java)
                } else {
                    view.isEnabled = true
                    view.text = resources.getText(R.string.login_btn_text)
                    Snackbar.make(view, "Credenciales inválidas", Snackbar.LENGTH_LONG).show()
                }
            })
        }
    }
}