package com.aentrena.db19aentrena.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aentrena.db19aentrena.domain.hero.HeroRepository
import com.aentrena.db19aentrena.model.Hero
import com.aentrena.db19aentrena.presentation.LoginViewModel
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
        data class HeroSelected(val hero: Hero): HeroesState()
        data class HeroesUpdated(val heroes: List<Hero>): HeroesState()
        data class Error(val message: String): HeroesState()

    }

    private val _heroesState = MutableStateFlow<HeroesState>(HeroesState.Idle)
    val heroesState: StateFlow<HeroesState> = _heroesState

    fun downloadHeroes(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = heroRepository.downloadHeroes(token)
            when (response) {
                is HeroRepository.DownloadHeroesResponse.Success -> {
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
}