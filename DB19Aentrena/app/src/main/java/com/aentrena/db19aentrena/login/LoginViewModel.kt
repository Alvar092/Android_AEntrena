package com.aentrena.db19aentrena.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aentrena.db19aentrena.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    fun performLogin(usuario: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

}