package com.example.composition.presentation

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GetGameSettingsUseCase

class GameFragment : Fragment() {
    private lateinit var level: Level
    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            this.add(binding.answer1)
            this.add(binding.answer2)
            this.add(binding.answer3)
            this.add(binding.answer4)
            this.add(binding.answer5)
            this.add(binding.answer6)
        }
    }
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("GameFragment binding == null")
    private val viewModel: GameFragmentViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[GameFragmentViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        viewModel.question.observe(viewLifecycleOwner) {
            binding.tvSum.text = it.sum.toString()
            binding.tvVisibleNum.text = it.visibleNumber.toString()
            for (i in 0 until tvOptions.size) {
                tvOptions[i].text = it.options[i].toString()
            }
        }
        viewModel.percentOfRightAnswers.observe(viewLifecycleOwner) {
            binding.progressBar.setProgress(it, true)
        }
        viewModel.enoughCountOfRightAnswers.observe(viewLifecycleOwner){
            binding.tvAnswersProgress.setTextColor(getColorByState(it))
        }
        viewModel.enoughPercentOfRightAnswers.observe(viewLifecycleOwner){
            val color = getColorByState(it)
            binding.progressBar.progressTintList =  ColorStateList.valueOf(color )
        }
        viewModel.formattedTime.observe(viewLifecycleOwner){
            binding.tvTimer.text = it
        }
        viewModel.minPercent.observe(viewLifecycleOwner){
            binding.progressBar.secondaryProgress = it
        }
        viewModel.gameResult.observe(viewLifecycleOwner){
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, ResultFragment.newInstance(it))
                .addToBackStack(ResultFragment.NAME)
                .commit()
        }
    }

    private fun getColorByState(state: Boolean): Int{
        return  ContextCompat.getColor(requireContext(),
            if (state){
                android.R.color.holo_green_light
            }else {
                android.R.color.holo_red_light
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun parseArgs() {
        requireArguments().getParcelable<Level>(KEY_VALUE)?.let {
            level = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_VALUE = "level"
        const val GAME_FRAGMENT = "gameFragment"
        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_VALUE, level)
                }
            }
        }
    }
}