package com.mutobu.financial_literacy_simulator.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mutobu.financial_literacy_simulator.R

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val categories = viewModel.investmentCategories

        val treasuryBondsCard =
            view.findViewById<android.view.View>(R.id.treasuryBondsCard)

        treasuryBondsCard.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_treasuryBondsFragment
            )
        }
        }

    }
