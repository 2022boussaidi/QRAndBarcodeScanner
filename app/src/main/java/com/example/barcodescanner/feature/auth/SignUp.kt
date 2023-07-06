package com.example.barcodescanner.feature.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.barcodescanner.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException


class SignUp : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var username: EditText
    private lateinit var signUp: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var confirm: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


            mAuth = FirebaseAuth.getInstance()
            email = findViewById(R.id.editTextEmail)
            password = findViewById(R.id.editTextPassword)
           // username = findViewById(R.id.editTextUsername)
        confirm = findViewById(R.id.editTextConfirmPassword)
            signUp = findViewById(R.id.buttonSignUp)
        val textViewSignUp = findViewById<TextView>(R.id.textView2)

        textViewSignUp.setOnClickListener { val intent = Intent(this,Login::class.java)
            startActivity(intent)
            finish() }
            signUp.setOnClickListener {
                createUser()
            }
        }

       /* override fun onStart() {
            super.onStart()
            //User is already loggedIn
            if (mAuth.currentUser != null) {
                startSuccessfulLoginActivity()
            }
        }*/

        //Authentication using firebase Authentication
        //Logic for Password, SignUp, Login
        private fun createUser() {
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()
            val confirmText = confirm.text.toString().trim()





            if (emailText.isEmpty()) {
                email.error = "Email Required"
                email.requestFocus()
                return
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                email.error = "Proper Email Required"
                email.requestFocus()
                return
            }
            if (passwordText.isEmpty() || passwordText.length <= 6) {
                password.error = "Password Required with more than 6 characters"
                password.requestFocus()
                return
            }
            if   (confirmText.isEmpty() || passwordText != confirmText) {
               confirm.error= "Passwords do not match"
                confirm.requestFocus()
                return
            }
            //if everything is fine email is correct and password is valid
            //using instance of FirebaseAuth we will create the user
            mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startSuccessfulLoginActivity()
                    }
                        else {
                            Toast.makeText(this, "An error has occurred during signup. Please try again later.", Toast.LENGTH_SHORT).show()
                        }
                        }
                    }





        private fun startSuccessfulLoginActivity() {
            val intent = Intent(this, SaveUserInDB::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }

    }



