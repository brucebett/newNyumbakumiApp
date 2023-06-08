package com.example.nyumbakumiapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class HomeActivity : AppCompatActivity() {
    lateinit var cardAddHouses:CardView
    lateinit var cardSearchHouses:CardView
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        cardAddHouses = findViewById(R.id.mcardAddhouses)
        cardSearchHouses = findViewById(R.id.mcardSearchhuoses)

          cardAddHouses.setOnClickListener {
              startActivity(Intent(applicationContext, AddHousesActivity::class.java))
          }
        cardSearchHouses.setOnClickListener {
            startActivity(Intent(this,ViewHousesActivity::class.java))
        }
    }
}