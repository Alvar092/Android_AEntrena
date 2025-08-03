package com.aentrena.db19aentrena.domain.hero

import com.aentrena.db19aentrena.model.Hero

interface HeroRepository {

    sealed class DownloadHeroesResponse {
        data class Success(val heroes: List<Hero>): DownloadHeroesResponse()
        data class Error(val message: String, val code: Int): DownloadHeroesResponse()
    }
    suspend fun downloadHeroes(token: String): DownloadHeroesResponse
}