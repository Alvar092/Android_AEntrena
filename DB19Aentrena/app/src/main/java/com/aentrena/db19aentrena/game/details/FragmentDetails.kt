package com.aentrena.db19aentrena.game.details

import kotlin.random.Random
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.aentrena.db19aentrena.databinding.FragmentDetailsBinding
import com.aentrena.db19aentrena.game.GameViewModel
import com.aentrena.db19aentrena.model.Hero
import com.bumptech.glide.Glide


class FragmentDetails: Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()

    private var currentHero: Hero? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.heroesLiveData.observe(viewLifecycleOwner) { state ->
            if (state is GameViewModel.HeroesState.HeroSelected) {
                val hero = state.hero
                bindHero(hero)
            }
        }

        setObservers()

    }

    fun bindHero(hero: Hero) {
        currentHero = hero

        binding.tvHeroName.text = hero.name

        binding.pbDetail.progress = hero.currentHealth

        Glide.with(this)
            .load(hero.photo)
            .into(binding.ivDetail)

        updateHealth()
    }

    private fun updateHealth() {
        currentHero?.let { hero ->
            binding.pbDetail.progress = hero.currentHealth
        }
    }

    private fun setObservers() {
        binding.bDamage.setOnClickListener {
            currentHero?.let { hero ->
                val actualDamage = hero.reciveDamage()

                updateHealth()

                Toast.makeText(requireContext(), "Daño recibido: $actualDamage", Toast.LENGTH_SHORT)
                    .show()

                if (hero.currentHealth == 0) {
                    Toast.makeText(
                        requireContext(),
                        "${hero.name} ha muerto! :(",
                        Toast.LENGTH_SHORT
                    ).show()

                    handleHeroDeath()

                }
            }
        }

        binding.bHeal.setOnClickListener {
            currentHero?.let { hero ->
                hero.heal()
                updateHealth()
            }
        }
    }

    private fun handleHeroDeath() {
        binding.root.postDelayed({
            viewModel.notifyHeroUpdated()
            requireActivity().supportFragmentManager.popBackStack()
        }, 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        currentHero.let {
            viewModel.saveCurrentHeroes(requireContext())
        }

        _binding = null
    }
}
