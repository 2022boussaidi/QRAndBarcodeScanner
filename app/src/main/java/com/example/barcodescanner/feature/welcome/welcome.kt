package com.example.barcodescanner.feature.welcome


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.barcodescanner.R
import com.example.barcodescanner.feature.auth.SignUp

//import com.example.barcodescanner.feature.auth.s
//import com.example.barcodescanner.feature.tabs.BottomTabsActivity


class Welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome)
        val buttonNext = findViewById<Button>(R.id.button)
        buttonNext.setOnClickListener{
            val i = Intent ( this,SignUp::class.java)
            startActivity(i)
        }


    }
}