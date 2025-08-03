package com.aentrena.db19aentrena.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aentrena.db19aentrena.domain.hero.HeroRepository
import com.aentrena.db19aentrena.model.Hero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val heroRepository: HeroRepository
): ViewModel() {

    sealed class HeroesState {
        data object Idle: HeroesState()
        data object Loading: HeroesState()
        data class HeroesDownloaded(val heroes: List<Hero>): HeroesState()
        data class HeroSelected(val hero: Hero, val heroList: List<Hero>): HeroesState()
        data class HeroesUpdated(val heroes: List<Hero>): HeroesState()
        data class Error(val message: String): HeroesState()

    }

    private val _heroesState = MutableStateFlow<HeroesState>(HeroesState.Idle)
    val heroesState: StateFlow<HeroesState> = _heroesState
    private var cachedHeroes: List<Hero> = emptyList()
    val heroesLiveData = _heroesState.asLiveData()

    fun downloadHeroes(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = heroRepository.downloadHeroes(token)
            Log.d("GameViewModel", "Response: $response")
            when (response) {
                is HeroRepository.DownloadHeroesResponse.Success -> {
                    cachedHeroes = response.heroes
                    _heroesState.update {
                        HeroesState.HeroesDownloaded(response.heroes)
                    }
                }
                is HeroRepository.DownloadHeroesResponse.Error -> {
                    _heroesState.update {
                        HeroesState.Error(response.message)
                    }
                }

            }
        }
    }

    fun selectHero(hero: Hero) {
        val currentHeroes = when (val state = _heroesState.value) {
            is HeroesState.HeroSelected -> state.heroList
            is HeroesState.HeroesDownloaded -> state.heroes
            is HeroesState.HeroesUpdated -> state.heroes
            else -> cachedHeroes
        }
        _heroesState.update {
            HeroesState.HeroSelected(hero, currentHeroes)
        }
    }

    fun resetAllHeroesHealth() {
        val currentHeroes = when (val state = _heroesState.value) {
            is HeroesState.HeroSelected -> state.heroList
            is HeroesState.HeroesDownloaded -> state.heroes
            is HeroesState.HeroesUpdated -> state.heroes
            else -> cachedHeroes
        }

        currentHeroes.forEach { hero ->
            hero.resetHealth()
        }

        _heroesState.update {
            HeroesState.HeroesUpdated(currentHeroes)
        }
    }

    fun notifyHeroUpdated() {
        val currentHeroes = when (val state = _heroesState.value) {
            is HeroesState.HeroSelected -> state.heroList
            is HeroesState.HeroesDownloaded -> state.heroes
            is HeroesState.HeroesUpdated -> state.heroes
            else -> cachedHeroes
        }

        currentHeroes.forEach { hero ->
            Log.d("GameViewModel", "Hero: ${hero.name}, Health: ${hero.currentHealth}")
        }

        _heroesState.update {
            HeroesState.HeroesUpdated(currentHeroes)
        }
    }

    fun getSelectedHero(): Hero? {
        return when (val currentState = heroesState.value) {
            is HeroesState.HeroSelected -> currentState.hero
            else -> null
        }
    }


}