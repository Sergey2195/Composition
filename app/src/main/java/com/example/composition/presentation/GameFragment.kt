package com.example.composition.presentation

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GetGameSettingsUseCase

class GameFragment : Fragment() {
    private lateinit var level: Level

    private val args by navArgs<GameFragmentArgs>()
    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.answer1)
            add(binding.answer2)
            add(binding.answer3)
            add(binding.answer4)
            add(binding.answer5)
            add(binding.answer6)
        }
    }
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("GameFragment binding == null")
    private val viewModel: GameFragmentViewModel by lazy {
        level = args.level
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[GameFragmentViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        observeLiveData()
        setClickListeners()
        viewModel.startGame(level)
    }

    private fun setClickListeners(){
        for (item in tvOptions){
            item.setOnClickListener {
                viewModel.chooseAnswer(item.text.toString().toInt())
            }
        }
    }

    private fun observeLiveData() { 
        viewModel.gameResult.observe(viewLifecycleOwner){
            findNavController().navigate(GameFragmentDirections.actionGameFragmentToResultFragment(it))
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}