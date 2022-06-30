package com.example.moneyapp

data class transaction(
    var amount: String,
    var category: String,
    var notes: String,
    var type: String,
    var date: String
)
