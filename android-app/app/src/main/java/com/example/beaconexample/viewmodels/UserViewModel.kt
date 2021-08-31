package com.example.beaconexample.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beaconexample.data.User
import com.example.beaconexample.repositories.UserRepository
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    val userData = MutableLiveData<User>()

    fun getUser() {
        viewModelScope.launch {
            val repository = UserRepository()
            userData.postValue(repository.getUser())
        }
    }
}