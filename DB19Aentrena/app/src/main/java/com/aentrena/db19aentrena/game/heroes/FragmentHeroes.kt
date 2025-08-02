package com.aentrena.db19aentrena.game.heroes

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.aentrena.db19aentrena.databinding.FragmentHeroesBinding
import com.aentrena.db19aentrena.game.GameViewModel
import com.aentrena.db19aentrena.model.Hero
import kotlinx.coroutines.launch


class FragmentHeroes(
    private val onClick: (Hero) -> Unit
): Fragment() {

    private val viewModel: GameViewModel by activityViewModels()

    private lateinit var binding: FragmentHeroesBinding

    private lateinit var adapter: AdapterHeroes

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeroesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setOnClickListener {
            onClick()
        }
    }

    fun setHeroes(heroes: List<Hero>) {
        adapter.submitList(heroes)
    }
}

    /*private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.heroesState.collect { state ->
                when(state) {
                    is GameViewModel.HeroesState.Loading
                    is GameViewModel.HeroesState.HeroesDownloaded // showHeroes() storeOnSharedPreferences()
                    is GameViewModel.HeroesState.Error
                    is GameViewModel.HeroesState.HeroSelected //goToHeroDetails() increaseCounter()
                    is GameViewModel.HeroesState.HeroesUpdated //notificar del cambio
            }
        }
    }
} */