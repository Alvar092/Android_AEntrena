package com.aentrena.db19aentrena.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.aentrena.db19aentrena.databinding.ActivityGameBinding
import com.aentrena.db19aentrena.game.details.FragmentDetails
import com.aentrena.db19aentrena.game.heroes.FragmentHeroes
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, GameActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityGameBinding
    private val viewModel: GameViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setObservers()
        showHeroesFragment()


        supportFragmentManager.beginTransaction()
            .replace(binding.flGame.id, FragmentHeroes())
            .commit()
    }

    fun setObservers() {
        lifecycleScope.launch {
            viewModel.heroesState.collect { state ->
                when(state) {
                    is GameViewModel.HeroesState.Error -> {
                    }
                    is GameViewModel.HeroesState.HeroSelected -> {
                        var hero = viewModel.getSelectedHero()
                        showHeroDetails()
                    }
                    is GameViewModel.HeroesState.HeroesDownloaded -> {
                    }
                    is GameViewModel.HeroesState.HeroesUpdated -> {
                    }
                    GameViewModel.HeroesState.Idle -> {
                    }
                    GameViewModel.HeroesState.Loading -> {
                    }
                }
            }
        }
    }
    fun showHeroesFragment() {
        val fragmentHeroes = FragmentHeroes()

        supportFragmentManager.beginTransaction()
            .replace(binding.flGame.id, fragmentHeroes)
            .commit()
    }

    fun showHeroDetails() {
        supportFragmentManager.beginTransaction()
            .replace(binding.flGame.id, FragmentDetails())
            .addToBackStack(null)
            .commit()
    }
}