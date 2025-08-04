package com.aentrena.db19aentrena.game

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aentrena.db19aentrena.domain.hero.HeroRepository
import com.aentrena.db19aentrena.model.Hero
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val heroRepository: HeroRepository
): ViewModel() {

    sealed class HeroesState {
        data object Idle : HeroesState()
        data object Loading : HeroesState()
        data class HeroesDownloaded(val heroes: List<Hero>) : HeroesState()
        data class HeroSelected(val hero: Hero, val heroList: List<Hero>) : HeroesState()
        data class HeroesUpdated(val heroes: List<Hero>) : HeroesState()
        data class Error(val message: String) : HeroesState()

    }

    private val _heroesState = MutableStateFlow<HeroesState>(HeroesState.Idle)
    val heroesState: StateFlow<HeroesState> = _heroesState
    private var cachedHeroes: List<Hero> = emptyList()
    val heroesLiveData = _heroesState.asLiveData()

    fun downloadHeroes(token: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("GameViewModel", "=== downloadHeroes STARTED ===")
                Log.d("GameViewModel", "Token: ${token.take(20)}...")
                _heroesState.update { HeroesState.Loading }

                // Primero carga desde SharedPreferences
                val savedHeroes = loadHeroesFromPreferences(context)
                Log.d("GameViewModel", "Saved heroes from prefs: ${savedHeroes.size}")

                if (savedHeroes.isNotEmpty()) {
                    cachedHeroes = savedHeroes
                    _heroesState.update { HeroesState.HeroesDownloaded(savedHeroes) }
                    Log.d("GameViewModel", "Updated state with saved heroes")
                }

                Log.d("GameViewModel", "Calling repository...")
                when (val response = heroRepository.downloadHeroes(token)) {
                    is HeroRepository.DownloadHeroesResponse.Success -> {
                        val heroes = response.heroes
                        Log.d("GameViewModel", "API SUCCESS: ${heroes.size} heroes received")
                        heroes.forEach { hero ->
                            Log.d("GameViewModel", "Hero: ${hero.name}, ID: ${hero.id}")
                        }

                        cachedHeroes = heroes
                        saveHeroesToPreferences(context, heroes)
                        _heroesState.update { HeroesState.HeroesDownloaded(heroes) }
                        Log.d("GameViewModel", "=== downloadHeroes COMPLETED ===")
                    }
                    is HeroRepository.DownloadHeroesResponse.Error -> {
                        Log.e("GameViewModel", "API ERROR: ${response.code} - ${response.message}")
                        throw Exception("Error ${response.code}: ${response.message}")
                    }
                }

            } catch (e: Exception) {
                Log.e("GameViewModel", "EXCEPTION in downloadHeroes: ${e.message}", e)
                val savedHeroes = loadHeroesFromPreferences(context)
                if (savedHeroes.isNotEmpty()) {
                    Log.d("GameViewModel", "Using fallback heroes: ${savedHeroes.size}")
                    cachedHeroes = savedHeroes
                    _heroesState.update { HeroesState.HeroesDownloaded(savedHeroes) }
                } else {
                    Log.e("GameViewModel", "No fallback data available")
                    _heroesState.update { HeroesState.Error(e.message ?: "Unknown error") }
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

        fun resetAllHeroesHealth(context: Context) {
            Log.d("GameViewModel", "resetAllHeroesHealth called")

            val currentHeroes = when (val state = _heroesState.value) {
                is HeroesState.HeroSelected -> state.heroList
                is HeroesState.HeroesDownloaded -> state.heroes
                is HeroesState.HeroesUpdated -> state.heroes
                else -> {
                    Log.d("GameViewModel", "Using cachedHeroes: ${cachedHeroes.size} heroes")
                    cachedHeroes
                }
            }

            currentHeroes.forEach { hero ->
                hero.resetHealth()
            }

            saveHeroesToPreferences(context, currentHeroes)

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

        fun saveHeroesToPreferences(context: Context, heroes: List<Hero>) {
            val gson = Gson()
            val heroesJson = gson.toJson(heroes)

            context.getSharedPreferences("LoginActivity", Context.MODE_PRIVATE)
                .edit()
                .putString("heroes_list", heroesJson)
                .apply()
        }


        fun loadHeroesFromPreferences(context: Context): List<Hero> {
            val gson = Gson()
            val heroesJson = context.getSharedPreferences("LoginActivity", Context.MODE_PRIVATE)
                .getString("heroes_list", "")

            return if (!heroesJson.isNullOrEmpty()) {
                val type = object : TypeToken<List<Hero>>() {}.type
                gson.fromJson(heroesJson, type) ?: emptyList()
            } else {
                emptyList()
            }
        }

    fun saveCurrentHeroes(context: Context) {
        saveHeroesToPreferences(context, cachedHeroes)
    }
}
