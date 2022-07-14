package com.example.composition.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.composition.R
import com.example.composition.databinding.FragmentChooseLevelBinding
import com.example.composition.domain.entity.Level

class ChooseLevelFragment : Fragment() {
    private var _binding: FragmentChooseLevelBinding? = null
    private val binding: FragmentChooseLevelBinding
        get() = _binding ?: throw RuntimeException("ChooseLevelFragment binding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.testBtn.setOnClickListener{
            setClickListeners(Level.TEST)
        }
        binding.easyBtn.setOnClickListener {
            setClickListeners(Level.EASY)
        }
        binding.medBtn.setOnClickListener {
            setClickListeners(Level.NORMAL)
        }
        binding.hardBtn.setOnClickListener {
            setClickListeners(Level.HARD)
        }
    }

    private fun setClickListeners(level: Level){
        val args = Bundle().apply {
            putParcelable(GameFragment.KEY_VALUE, level)
        }
        findNavController().navigate(R.id.action_chooseLevelFragment_to_gameFragment, args)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object{
        const val NAME = "ChooseLevelFragment"
        fun newInstance(): ChooseLevelFragment{
            return ChooseLevelFragment()
        }
    }

}