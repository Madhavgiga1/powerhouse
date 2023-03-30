package com.example.powerhouseevaluation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.powerhouseevaluation.R
import com.example.powerhouseevaluation.databinding.FragmentDisplayBonusBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DisplayBonusFragment : Fragment() {
    private val args by navArgs<DisplayBonusFragmentArgs>()
    private var _binding:FragmentDisplayBonusBinding?=null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentDisplayBonusBinding.inflate(layoutInflater,container,false)
        binding.forecastforcity=args.forecastforcity
         return binding.root
    }


}