package com.aentrena.db19aentrena.domain.hero

import android.util.Log
import com.aentrena.db19aentrena.domain.HeroDto
import com.aentrena.db19aentrena.domain.login.BASE_URL
import com.aentrena.db19aentrena.model.Hero
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class HeroRepositoryImpl(
    private val client: OkHttpClient = OkHttpClient(),
    private val baseUrl: String = BASE_URL
): HeroRepository {
    override suspend fun downloadHeroes(token: String): HeroRepository.DownloadHeroesResponse {
        val url = "${baseUrl}heros/all"
        val credentials = "Bearer $token"

        val body = FormBody.Builder().add("name", "").build()
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", credentials)
            .post(body)
            .build()

        // Opcional: para depuración temporal, loguear lo que se envía
        Log.d("HeroRepository", "Request to $url with header Authorization=$credentials")

        val response = client.newCall(request).execute()
        Log.d("HeroRepository", "Response code: ${response.code}")
        Log.d("HeroRepository", "Response successful: ${response.isSuccessful}")

        return if (response.isSuccessful) {
            val responseBody = response.body?.string() ?: ""
            Log.d("HeroRepository", "Response body: $responseBody")

            val json = Gson().fromJson(responseBody, Array<HeroDto>::class.java)
            Log.d("HeroRepository", "Parsed ${json.size} heroes from JSON")

            val heroes = json.toList().map { Hero(it.photo, it.id, it.name) }
            Log.d("HeroRepository", "Mapped heroes: ${heroes.map { it.name }}")

            HeroRepository.DownloadHeroesResponse.Success(heroes)
        } else {
            HeroRepository.DownloadHeroesResponse.Error(response.message, response.code)
        }
    }
}