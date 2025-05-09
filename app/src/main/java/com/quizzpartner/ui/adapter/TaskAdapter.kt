package com.quizzpartner.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.quizzpartner.R
import com.quizzpartner.model.QuizItem
import com.quizzpartner.ui.QuizFillInWordActivity
import com.quizzpartner.ui.QuizMultipleChoiceActivity
import com.quizzpartner.ui.QuizRandomActivity
import com.quizzpartner.ui.QuizTrueFalseActivity

class TaskAdapter(private val context: Context, private val dataSource: ArrayList<QuizItem>): BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowItem = inflater.inflate(R.layout.task_item, parent, false)
        val cvContainer = rowItem.findViewById(R.id.cvContainer) as MaterialCardView
        val imgCover = rowItem.findViewById(R.id.imgCover) as ImageView
        val tvCategory =  rowItem.findViewById(R.id.tvCategory) as TextView
        val tvTopic = rowItem.findViewById(R.id.tvTopic) as TextView
        val tvTimeNeeded = rowItem.findViewById(R.id.tvTimeNeeded) as TextView
        val tvTotalQuestion = rowItem.findViewById(R.id.tvTotalQueztion) as TextView

        val quiz = getItem(position) as QuizItem
        tvCategory.text = quiz.category
        tvTopic.text = quiz.topic
        tvTimeNeeded.text = quiz.time.toString() + " detik"
        tvTotalQuestion.text = quiz.totalQuestion.toString() + " soal"

        if(quiz.topic.equals(context.getString(R.string.label_tokoh_alkitab))) {
            imgCover.setImageResource(R.drawable.tokoh_alkitab)
        } else {
            imgCover.setImageResource(R.drawable.ayat_alkitab)
        }

        if(quiz.totalQuestion == 40) {
            cvContainer.setStrokeColor(context.getColor(R.color.accentRed))
        } else if(quiz.totalQuestion == 20) {
            cvContainer.setStrokeColor(context.getColor(R.color.accentOrange))
        } else {
            cvContainer.setStrokeColor(context.getColor(R.color.accentGreen))
        }
        
        rowItem.setOnClickListener {
            var intent = Intent(context, QuizMultipleChoiceActivity::class.java)
            if (quiz.category.equals(context.getString(R.string.label_true_false))) {
                intent = Intent(context, QuizTrueFalseActivity::class.java)
            } else if (quiz.category.equals(context.getString(R.string.label_fill_in_word))) {
                intent = Intent(context, QuizFillInWordActivity::class.java)
            } else if (quiz.category.equals(context.getString(R.string.label_random))) {
                intent = Intent(context, QuizRandomActivity::class.java)
            }
            intent.putExtra("totalQuestion", quiz.totalQuestion)
            intent.putExtra("quizCategory", quiz.category)
            intent.putExtra("quizTopic", quiz.topic)
            context.startActivity(intent)
        }

        return rowItem
    }
}