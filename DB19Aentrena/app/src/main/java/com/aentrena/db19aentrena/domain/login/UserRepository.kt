package com.aentrena.db19aentrena.domain.login

import com.aentrena.db19aentrena.User

interface UserRepository {

    sealed class LoginResponse {
        data class Success(val token: String): LoginResponse()
        data class Error(val message: String): LoginResponse()
    }
    suspend fun performLoginRequest(user: User): LoginResponse
}