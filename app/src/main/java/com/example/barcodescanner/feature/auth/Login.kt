package com.example.barcodescanner.feature.auth
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.barcodescanner.R
import com.example.barcodescanner.feature.tabs.BottomTabsActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class Login : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val textViewSignUp = findViewById<TextView>(R.id.textView3)
        mAuth = FirebaseAuth.getInstance()
        email = findViewById(R.id.editTextEmail)
        password = findViewById(R.id.editTextPassword)
        login = findViewById(R.id.buttonLogin)


        textViewSignUp.setOnClickListener { val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
            finish() }
        login.setOnClickListener {
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()
            userLogin(emailText, passwordText)
        }


    }
    override fun onStop() {
        super.onStop()
        // User is logged out, save the logout date
        val logoutDate = getCurrentDateTime()
        updateLogoutDate(logoutDate)
    }
    private fun userLogin(emailText: String, passwordText: String) {
        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(this, "Please make sure to fill in your email and password", Toast.LENGTH_SHORT).show()
            return
        }
        //here we uses signInWithEmailAndPassword function of FirebaseAuth
        mAuth.signInWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, BottomTabsActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this, "An error has occurred during login. Please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
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
