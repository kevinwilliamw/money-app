package com.example.moneyapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

lateinit var db : FirebaseFirestore

private var _amount = mutableListOf<String>()
private var _category  = mutableListOf<String>()
private var _notes = mutableListOf<String>()
private var _type = mutableListOf<String>()
private var _date = mutableListOf<String>()

private var arTransaction = arrayListOf<transaction>()

class AddTransaction : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        //hide action bar
        (this as AppCompatActivity).supportActionBar!!.hide()

        //category spinner
        val spinnerCategory: Spinner = findViewById(R.id.spinnerCategory)
        ArrayAdapter.createFromResource(
            this,
            R.array.category,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter
        }

        //transaction type spinner
        val spinnerTransactionType: Spinner = findViewById(R.id.spinnerTransactionType)
        ArrayAdapter.createFromResource(
            this,
            R.array.transactionType,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTransactionType.adapter = adapter
        }

        db = FirebaseFirestore.getInstance()

        val _btnSave = findViewById<Button>(R.id.btnSave)
        val _editAmount = findViewById<EditText>(R.id.editAmount)
        val _editNotes = findViewById<EditText>(R.id.editNotes)
        val _datePicker = findViewById<DatePicker>(R.id.datepicker)

        //get date from picker
        var selectedDate = _datePicker.dayOfMonth.toString() + "-" + (_datePicker.month + 1).toString() + "-" + _datePicker.year.toString()

        //change selected date
        _datePicker.setOnDateChangedListener { datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
            selectedDate = "" + _datePicker.dayOfMonth.toString() + "-" + (_datePicker.month + 1).toString() + "-" + _datePicker.year.toString()
        }

        _btnSave.setOnClickListener {
            val selectedCategory = spinnerCategory.selectedItem.toString()
            val selectedTransactionType = spinnerTransactionType.selectedItem.toString()
            //Toast.makeText(this, "Save - CLICKED", Toast.LENGTH_SHORT).show()

            if(selectedCategory != "Select category" && selectedTransactionType != "Select transaction type"
                && !TextUtils.isEmpty(_editNotes.text) && !TextUtils.isEmpty(_editAmount.text)) {

                _amount.add(_editAmount.text.toString())
                _category.add(selectedCategory)
                _notes.add(_editNotes.text.toString())
                _type.add(selectedTransactionType)
                _date.add(selectedDate)

                for (pos in _amount.indices){
                    val data = transaction(_amount[pos],_category[pos],_notes[pos],_type[pos],_date[pos])
                    db.collection("dataTransaction").document().set(data)
                        .addOnSuccessListener {
                            Log.d(TAG, "Transaction successfully written!")
                            Toast.makeText(this, "Data successfully saved", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                    arTransaction.add(data)
                }

                resetAllFields(spinnerCategory, spinnerTransactionType, _editAmount, _editNotes, _datePicker)
                //get date from datepicker
                selectedDate = "" + _datePicker.dayOfMonth.toString() + "-" + (_datePicker.month + 1).toString() + "-" + _datePicker.year.toString()

            }
            else {
                Toast.makeText(this, "Please fill all data", Toast.LENGTH_SHORT).show()
            }
        }

        var lastType = "" //additional variable to prevent continuous spinner array update
        spinnerTransactionType.addOnLayoutChangeListener { view, i, i2, i3, i4, i5, i6, i7, i8 ->
            if(spinnerTransactionType.selectedItem.toString() == "Income" && lastType != "Income") {
                ArrayAdapter.createFromResource(
                    this,
                    R.array.categoryFilterIncome,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCategory.adapter = adapter
                }

                lastType = "Income"
            }
            else if (spinnerTransactionType.selectedItem.toString() == "Expense" && lastType != "Expense") {
                ArrayAdapter.createFromResource(
                    this,
                    R.array.categoryFilterExpense,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCategory.adapter = adapter
                }

                lastType = "Expense"
            }
        }
    }
}

class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        parent.getItemAtPosition(pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}

fun resetAllFields(
    spinnerCategory: Spinner,
    spinnerTransactionType: Spinner,
    _editAmount: EditText,
    _editNotes: EditText,
    _datePicker: DatePicker
) {
    //reset all fields
    spinnerCategory.setSelection(0)
    spinnerTransactionType.setSelection(0)
    _editAmount.text.clear()
    _editNotes.text.clear()
    val today = Calendar.getInstance()
    _datePicker.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
        today.get(Calendar.DAY_OF_MONTH))
}

