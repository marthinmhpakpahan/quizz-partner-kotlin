package com.quizzpartner.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.quizzpartner.data.UserData
import com.quizzpartner.databinding.ActivityLoginBinding
import com.quizzpartner.util.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        isLoggedIn()

        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password)
            } else {
                Toast.makeText(this@LoginActivity, "Usernamd dan password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLoggedIn() {
        val userId = SessionManager.getSession(this@LoginActivity, "users", "id")
        if (userId.toString().isNotEmpty()) {
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
            finish()
            return
        }
    }

    private fun saveUserSession(userData : UserData) {
        val userSharedPreferences = sharedPreferences.edit()
        userSharedPreferences.putString("id", userData.id)
        userSharedPreferences.putString("fullname", userData.fullName)
        userSharedPreferences.putString("email", userData.email)
        userSharedPreferences.putString("username", userData.username)
        userSharedPreferences.apply()
    }

    private fun loginUser(username: String, password: String) {
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val userData = userSnapshot.getValue(UserData::class.java)
                        if (userData != null && userData.password.equals(password)) {
                            saveUserSession(userData)
                            Toast.makeText(this@LoginActivity, "Successfully login!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                            finish()
                            return
                        } else {
                            Toast.makeText(this@LoginActivity, "Invalid password!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid username!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database error : ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}