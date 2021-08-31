package com.example.beaconexample.viewmodels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beaconexample.constants.LoginFormCodes
import com.example.beaconexample.data.User
import com.example.beaconexample.objects.LoginFormState
import com.example.beaconexample.repositories.UserRepository
import com.example.beaconexample.utils.LocalStorage
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val email = MutableLiveData("")
    private val password = MutableLiveData("")
    val formState = MutableLiveData<LoginFormState>()

    fun detectLoginDataChange(email: String, password: String) {
        if (!isEmailValid(email)) {
            return
        }

        if (!isPasswordValid(password)) {
            return
        }

        this.email.value = email
        this.password.value = password

        formState.value = LoginFormState(
            code = LoginFormCodes.OK,
            message = "Credenciales válidas"
        )
    }

    private fun isEmailValid(email: String): Boolean {
        if (email.isBlank()) {
            formState.value = LoginFormState(
                code = LoginFormCodes.INVALID_EMAIL,
                message = "Correo inválido"
            )
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            formState.value = LoginFormState(
                code = LoginFormCodes.INVALID_EMAIL,
                message = "Formato de correo inválido"
            )
            return false
        }

        formState.value = LoginFormState(
            code = LoginFormCodes.OK,
            message = ""
        )
        return true
    }

    private fun isPasswordValid(password: String): Boolean {
        if (password.isBlank()) {
            formState.value = LoginFormState(
                code = LoginFormCodes.INVALID_PASSWORD,
                message = "Clave inválida"
            )
            return false
        }
        if (password.length < 6) {
            formState.value = LoginFormState(
                code = LoginFormCodes.INVALID_PASSWORD,
                message = "Mínimo 6 caracteres"
            )
            return false
        }

        formState.value = LoginFormState(
            code = LoginFormCodes.OK,
            message = ""
        )
        return true
    }

    fun login(): LiveData<User?> {
        val email = this.email.value!!
        val password = this.password.value!!
        val result = MutableLiveData<User?>()

        if (!isEmailValid(email)) {
            return result
        }

        if (!isPasswordValid(password)) {
            return result
        }

        viewModelScope.launch {
            val repository = UserRepository()
            val user = repository.login(email, password)
            if (user !== null) {
                LocalStorage.user = user
            }
            result.postValue(user)
        }

        return result
    }
}