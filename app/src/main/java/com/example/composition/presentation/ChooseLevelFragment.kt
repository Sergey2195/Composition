package com.example.composition.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, GameFragment.newInstance(Level.TEST))
                .addToBackStack(GameFragment.GAME_FRAGMENT)
                .commit()
        }
        binding.easyBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, GameFragment.newInstance(Level.EASY))
                .addToBackStack(GameFragment.GAME_FRAGMENT)
                .commit()
        }
        binding.medBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, GameFragment.newInstance(Level.NORMAL))
                .addToBackStack(GameFragment.GAME_FRAGMENT)
                .commit()
        }
        binding.hardBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, GameFragment.newInstance(Level.HARD))
                .addToBackStack(GameFragment.GAME_FRAGMENT)
                .commit()
        }
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