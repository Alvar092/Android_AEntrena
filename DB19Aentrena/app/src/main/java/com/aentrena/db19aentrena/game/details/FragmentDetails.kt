package com.aentrena.db19aentrena.game.details

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aentrena.db19aentrena.databinding.FragmentDetailsBinding
import com.aentrena.db19aentrena.databinding.FragmentHeroesBinding
import com.aentrena.db19aentrena.game.GameViewModel


class FragmentDetails: Fragment() {

    private  lateinit var binding: FragmentDetailsBinding
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
}