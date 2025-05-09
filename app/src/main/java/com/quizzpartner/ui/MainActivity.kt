package com.quizzpartner.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.quizzpartner.R
import com.quizzpartner.data.UserData
import com.quizzpartner.databinding.ActivityLoginBinding
import com.quizzpartner.databinding.ActivityMainBinding
import com.quizzpartner.util.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        isLoggedIn()

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }

//        binding.btnLogin.setOnClickListener {
//            val intent = Intent(this@MainActivity, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//        binding.btnRegister.setOnClickListener {
//            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//        binding.btnLoginAsGuest.setOnClickListener {
//            loginUser("guest", "guest")
//        }
    }

    private fun isLoggedIn() {
        val userId = SessionManager.getSession(this@MainActivity, "user", "id")
        if (userId.toString().isNotEmpty()) {
            startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
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
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val userData = userSnapshot.getValue(UserData::class.java)
                        if (userData != null && userData.password.equals(password)) {
                            saveUserSession(userData)
                            Toast.makeText(this@MainActivity, "Successfully login!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
                            finish()
                            return
                        } else {
                            Toast.makeText(this@MainActivity, "Invalid password!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Invalid username!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Database error : ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}