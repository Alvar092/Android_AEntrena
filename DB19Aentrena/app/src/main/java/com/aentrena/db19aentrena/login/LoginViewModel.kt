package com.aentrena.db19aentrena.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aentrena.db19aentrena.User
import com.aentrena.db19aentrena.domain.login.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    sealed class LoginState {
        data object Idle: LoginState()
        data class Success(val token: String): LoginState()
        data class Error(val message: String): LoginState()
        data object Loading: LoginState()
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun performLogin(usuario: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loginState.update {
                LoginState.Loading
            }

            val response = userRepository.performLoginRequest(User(usuario, password))
            when (response) {
                is UserRepository.LoginResponse.Success -> {
                    val token = response.token
                    _loginState.update { LoginState.Success(token) }
                    Log.d("LoginViewModel", "Login existoso para usuario $usuario, token: $token")

                }
                is UserRepository.LoginResponse.Error -> {
                    LoginState.Error(response.message)
                }
            }

        }
    }

}