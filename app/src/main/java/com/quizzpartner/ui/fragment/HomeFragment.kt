package com.quizzpartner.ui.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import com.quizzpartner.R
import com.quizzpartner.databinding.ActivityDashboardBinding
import com.quizzpartner.databinding.FragmentHomeBinding
import com.quizzpartner.model.QuizItem
import com.quizzpartner.ui.QuizCategoryActivity
import com.quizzpartner.ui.adapter.TaskAdapter

class HomeFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentHomeBinding
    private var listEasyQuiz: ArrayList<QuizItem> = arrayListOf()
    private var listMediumQuiz: ArrayList<QuizItem> = arrayListOf()
    private var listHardQuiz: ArrayList<QuizItem> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        sharedPreferences = requireContext().getSharedPreferences("user", MODE_PRIVATE)

        setupListQuiz()
        val easyQuizAdapter = TaskAdapter(requireContext(), listEasyQuiz)
        val mediumQuizAdapter = TaskAdapter(requireContext(), listMediumQuiz)
        val hardQuizAdapter = TaskAdapter(requireContext(), listHardQuiz)

        binding.lvEasyQuiz.adapter = easyQuizAdapter
        binding.lvMediumQuiz.adapter = mediumQuizAdapter
        binding.lvHardQuiz.adapter = hardQuizAdapter

        return binding.root
    }

    fun setupListQuiz() {
        listEasyQuiz.add(QuizItem(getString(R.string.label_multiple_choices), getString(R.string.label_ayat_penting), 200, 10))
        listEasyQuiz.add(QuizItem(getString(R.string.label_multiple_choices), getString(R.string.label_tokoh_alkitab), 200, 10))
        listEasyQuiz.add(QuizItem(getString(R.string.label_true_false), getString(R.string.label_ayat_penting), 200, 10))
        listEasyQuiz.add(QuizItem(getString(R.string.label_true_false), getString(R.string.label_tokoh_alkitab), 200, 10))
        listEasyQuiz.add(QuizItem(getString(R.string.label_fill_in_word), getString(R.string.label_ayat_penting), 200, 10))
        listEasyQuiz.add(QuizItem(getString(R.string.label_fill_in_word), getString(R.string.label_tokoh_alkitab), 200, 10))

        listMediumQuiz.add(QuizItem(getString(R.string.label_multiple_choices), getString(R.string.label_ayat_penting), 200, 20))
        listMediumQuiz.add(QuizItem(getString(R.string.label_multiple_choices), getString(R.string.label_tokoh_alkitab), 200, 20))
        listMediumQuiz.add(QuizItem(getString(R.string.label_true_false), getString(R.string.label_ayat_penting), 200, 20))
        listMediumQuiz.add(QuizItem(getString(R.string.label_true_false), getString(R.string.label_tokoh_alkitab), 200, 20))
        listMediumQuiz.add(QuizItem(getString(R.string.label_fill_in_word), getString(R.string.label_ayat_penting), 200, 20))
        listMediumQuiz.add(QuizItem(getString(R.string.label_fill_in_word), getString(R.string.label_tokoh_alkitab), 200, 20))

        listHardQuiz.add(QuizItem(getString(R.string.label_multiple_choices), getString(R.string.label_ayat_penting), 200, 40))
        listHardQuiz.add(QuizItem(getString(R.string.label_multiple_choices), getString(R.string.label_tokoh_alkitab), 200, 40))
        listHardQuiz.add(QuizItem(getString(R.string.label_true_false), getString(R.string.label_ayat_penting), 200, 40))
        listHardQuiz.add(QuizItem(getString(R.string.label_true_false), getString(R.string.label_tokoh_alkitab), 200, 40))
        listHardQuiz.add(QuizItem(getString(R.string.label_fill_in_word), getString(R.string.label_ayat_penting), 200, 40))
        listHardQuiz.add(QuizItem(getString(R.string.label_fill_in_word), getString(R.string.label_tokoh_alkitab), 200, 40))
    }
}