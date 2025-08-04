package com.aentrena.db19aentrena.game.heroes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aentrena.db19aentrena.databinding.FragmentHeroesBinding
import com.aentrena.db19aentrena.game.GameViewModel
import com.aentrena.db19aentrena.model.Hero
import kotlinx.coroutines.launch
import com.aentrena.db19aentrena.R
import com.google.android.material.floatingactionbutton.FloatingActionButton



class FragmentHeroes: Fragment() {

    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var heroAdapter: HeroAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_heroes, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)
        setupFloatingActionButton(view)
        setObservers()

        /* viewModel.heroesLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is GameViewModel.HeroesState.HeroesDownloaded -> {
                    heroAdapter.submitList(state.heroes)
                }
                is GameViewModel.HeroesState.HeroesUpdated -> {
                    heroAdapter.submitList(null)
                    heroAdapter.submitList(state.heroes.toList())
                }

                is GameViewModel.HeroesState.Error -> {

                }
                is GameViewModel.HeroesState.HeroSelected -> {

                }
                GameViewModel.HeroesState.Idle -> {

                }
                GameViewModel.HeroesState.Loading -> {

                }
            }
        } */

        if (viewModel.heroesState.value is GameViewModel.HeroesState.Idle) {
            loadHeroes()
        }
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.rvHeroes)

        heroAdapter = HeroAdapter(viewModel)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = heroAdapter
        }
    }

    private fun loadHeroes() {
        val token = requireActivity().getSharedPreferences("LoginActivity", android.content.Context.MODE_PRIVATE)
            .getString("token", "") ?: ""
        viewModel.downloadHeroes(token, requireContext())
    }

    private fun setupFloatingActionButton(view: View) {
        val fab = view.findViewById<FloatingActionButton>(R.id.fbResetHealth)

        fab.setOnClickListener {
            viewModel.resetAllHeroesHealth(requireContext())
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("FragmentHeroes","onResume called, state: ${viewModel.heroesState.value}")
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.heroesState.collect { state ->
                when (state) {
                    is GameViewModel.HeroesState.Idle -> {

                    }
                    is GameViewModel.HeroesState.Loading -> {

                    }
                    is GameViewModel.HeroesState.HeroesDownloaded -> {
                        heroAdapter.submitList(state.heroes)
                    }
                    is GameViewModel.HeroesState.HeroesUpdated -> {
                        heroAdapter.submitList(emptyList()) {
                            heroAdapter.submitList(state.heroes)
                        }
                    }
                    is GameViewModel.HeroesState.HeroSelected -> {
                        Log.d("FragmentHeroes", "HeroesUpdated: ${state.heroList.size} heroes")
                        heroAdapter.submitList(state.heroList)

                    } //goToHeroDetails() increaseCounter()
                    is GameViewModel.HeroesState.Error -> {
                        Toast.makeText(requireContext(),"Error:${state.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}