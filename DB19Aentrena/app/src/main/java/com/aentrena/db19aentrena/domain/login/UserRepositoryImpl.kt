package com.aentrena.db19aentrena.domain.login

import com.aentrena.db19aentrena.User
import okhttp3.OkHttpClient
import okhttp3.Credentials
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Request



const val BASE_URL = "https://dragonball.keepcoding.education/api/"

class UserRepositoryImpl : UserRepository {
    override suspend fun performLoginRequest(user: User): UserRepository.LoginResponse {
        val client = OkHttpClient()
        val url = "${BASE_URL}auth/login"

        val credentials = Credentials.basic(user.name, user.password)

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", credentials)
            .post("".toRequestBody())
            .build()

        val response = client.newCall(request).execute()
        return if (response.isSuccessful) {
            val token = response.body.string()
            println("Token: $token")
            UserRepository.LoginResponse.Success(token)
        } else {
            UserRepository.LoginResponse.Error(response.message)
        }
    }
}