package com.example.barcodescanner.feature.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.barcodescanner.R
import com.example.barcodescanner.feature.tabs.BottomTabsActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class SaveUserInDB : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    //In xml we have this edit text to take data input and a button to submit
    private lateinit var et: EditText
    private lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_user_in_db)
        mAuth = FirebaseAuth.getInstance()
        et = findViewById(R.id.et)
        btn = findViewById(R.id.btn)
        // Get the login date


        btn.setOnClickListener {
            val data = et.text.toString()
            val intent = Intent(this, BottomTabsActivity::class.java)
            startActivity(intent)
            saveData(data)

        }
    }

    override fun onStop() {
        super.onStop()
        // User is logged out, save the logout date
        val logoutDate = getCurrentDateTime()
        updateLogoutDate(logoutDate)
    }

    override fun onStart() {
        super.onStart()
        //User is LoggedOut send user to mainActivity to Login
        if (mAuth.currentUser == null) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
    }

    //Function to save data in Realtime Database of Firebase
    private fun saveData(data: String) {
        val email = mAuth.currentUser?.email
        //Here UserInfo is a data class which tells DB that we have these columns
        val user = email?.let { UserInfo(it, data, getCurrentDateTime()) }

        //Remember here we use keyword what we used in our Database i.e "Key"
        //You can put anything at place of Key but same as your DataBase
        val dbUsers = FirebaseDatabase.getInstance().getReference("Key")
        dbUsers.child(mAuth.currentUser!!.uid)
            .setValue(user).addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Token Saved", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
    private fun updateLogoutDate(logoutDate: String) {
        val dbUsers = FirebaseDatabase.getInstance().getReference("Key")
        dbUsers.child(mAuth.currentUser!!.uid)
            .child("logoutDate")
            .setValue(logoutDate)
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Logout Date Updated", Toast.LENGTH_SHORT).show()
                }
            })
    }
}