package com.quizzpartner.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import com.quizzpartner.R
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.quizzpartner.databinding.ActivityDashboardBinding
import com.quizzpartner.ui.fragment.HistoryFragment
import com.quizzpartner.ui.fragment.HomeFragment

class DashboardActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_home -> replaceFragment(HomeFragment())
                R.id.navigation_history -> replaceFragment(HistoryFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit()
    }
}