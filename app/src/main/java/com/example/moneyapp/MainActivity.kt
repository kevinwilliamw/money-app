package com.example.moneyapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.content.Intent
import com.google.api.ResourceDescriptor

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val _btnAddTransaction =  findViewById<Button>(R.id.btnAddTransaction)
        val _btnHistory = findViewById<Button>(R.id.btnHistory)

        _btnAddTransaction.setOnClickListener {
            val intentAddTransaction = Intent(this, AddTransaction::class.java)
            startActivity(intentAddTransaction)
        }

        _btnHistory.setOnClickListener {
            val intentHistory = Intent(this, History::class.java)
            startActivity(intentHistory)
        }
    }

}