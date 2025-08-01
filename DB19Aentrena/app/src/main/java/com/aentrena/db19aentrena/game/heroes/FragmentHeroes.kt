package com.aentrena.db19aentrena.game.heroes

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aentrena.db19aentrena.databinding.FragmentHeroesBinding
import com.aentrena.db19aentrena.game.GameViewModel


class FragmentHeroes(
    private val onClick: () -> Unit
): Fragment(){

    private val viewModel: GameViewModel by activityViewModels()

    private  lateinit var binding: FragmentHeroesBinding

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
}