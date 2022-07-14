package com.example.composition.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.composition.R
import com.example.composition.databinding.FragmentResultBinding
import com.example.composition.domain.entity.GameResult

class ResultFragment : Fragment() {

    lateinit var result: GameResult
    private var _binding: FragmentResultBinding? = null
    private val binding: FragmentResultBinding
        get() = _binding ?: throw RuntimeException("ResultFragment binding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.retryBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvResult.text = if (result.winner){
            "Победа!"
        }else{
            "Поражение"
        }
        binding.textViewInfo.text = "Необходимое количество правильных ответов ${result.gameSettings.minCountOfRightAnswers}"
        binding.textViewInfo2.text = "Ваш счет ${result.countOfRightAnswers}"
        binding.textViewInfo3.text = "Необходимый процент правильных ответов ${result.gameSettings.minPercentOfRightAnswers}"
        binding.textViewInfo4.text = "Ваш процент ${(result.countOfRightAnswers * 100 / result.countOfQuestions.toDouble()).toInt()}%"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun parseArgs(){
       requireArguments().getParcelable<GameResult>(KEY_RESULT)?.let {
           result = it
       }
    }

    private fun retryGame(){
        requireActivity().supportFragmentManager.popBackStack(ChooseLevelFragment.NAME, 0)
    }

    companion object{
        const val KEY_RESULT = "key result"
        const val NAME = "result fragment"

        fun newInstance(gameResult: GameResult): ResultFragment{
            return ResultFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_RESULT, gameResult)
                }
            }
        }
    }
}