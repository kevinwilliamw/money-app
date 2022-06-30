package com.example.moneyapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*

private var data = mutableListOf<String>()

class History : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        //hide action bar
        (this as AppCompatActivity).supportActionBar!!.hide()

        db = FirebaseFirestore.getInstance()

        //spinner category filter
        val spinnerCategoryFilter: Spinner = findViewById(R.id.spinnerCategoryFilter)
        ArrayAdapter.createFromResource(
            this,
            R.array.categoryFilter,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategoryFilter.adapter = adapter
        }

        //spinner transaction type
        val spinnerTransactionTypeFilter: Spinner = findViewById(R.id.spinnerTransactionTypeFilter)
        ArrayAdapter.createFromResource(
            this,
            R.array.transactionTypeFilter,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTransactionTypeFilter.adapter = adapter
        }

        //selected item from spinner
        var selectedCategoryFilter = spinnerCategoryFilter.selectedItem.toString()
        var selectedTransactionTypeFilter = spinnerTransactionTypeFilter.selectedItem.toString()

        //list view adapter
        val lvadapter : ArrayAdapter<String> = ArrayAdapter(this, R.layout.itemhistory, data)
        val _lv1 = findViewById<ListView>(R.id.lvHistory)
        _lv1.adapter = lvadapter

        //get date
        val _datePickerFilter = findViewById<DatePicker>(R.id.datePickerFilter)
        var selectedDate = "00-00-0000"
        _datePickerFilter.setOnDateChangedListener { datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
            selectedDate = _datePickerFilter.dayOfMonth.toString() + "-" + (_datePickerFilter.month + 1).toString() + "-" + _datePickerFilter.year.toString()
        }

        val _textTotalIn = findViewById<TextView>(R.id.textTotalIn)
        val _textTotalOut = findViewById<TextView>(R.id.textTotalOut)

        //get data when opening the page
        getData(
            lvadapter,
            selectedCategoryFilter,
            selectedTransactionTypeFilter,
            selectedDate,
            _textTotalIn,
            _textTotalOut
        )

        //apply filter button
        val _btnFilterTypeAndCategory = findViewById<Button>(R.id.btnFilterTypeAndCategory)
        val _btnFilterDate = findViewById<Button>(R.id.btnFilterDate)
        val _btnFilterAll = findViewById<Button>(R.id.btnFilterAll)
        val _btnResetFilter = findViewById<Button>(R.id.btnResetFilter)

        _btnFilterTypeAndCategory.setOnClickListener {
            data.clear()
            //get category
            selectedCategoryFilter = spinnerCategoryFilter.selectedItem.toString()
            //get type
            selectedTransactionTypeFilter = spinnerTransactionTypeFilter.selectedItem.toString()
            //set date for all date
            selectedDate = "00-00-0000"
            //update list view
            getData(
                lvadapter,
                selectedCategoryFilter,
                selectedTransactionTypeFilter,
                selectedDate,
                _textTotalIn,
                _textTotalOut
            )
        }
        _btnFilterDate.setOnClickListener {
            //all category
            selectedCategoryFilter = "All categories"
            //all type
            selectedTransactionTypeFilter = "All types"
            //get datye
            selectedDate = _datePickerFilter.dayOfMonth.toString() + "-" + (_datePickerFilter.month + 1).toString() + "-" + _datePickerFilter.year.toString()
            //update list view
            getData(
                lvadapter,
                selectedCategoryFilter,
                selectedTransactionTypeFilter,
                selectedDate,
                _textTotalIn,
                _textTotalOut
            )
        }
        _btnFilterAll.setOnClickListener {
            //get category
            selectedCategoryFilter = spinnerCategoryFilter.selectedItem.toString()
            //get type
            selectedTransactionTypeFilter = spinnerTransactionTypeFilter.selectedItem.toString()
            //get date
            selectedDate = _datePickerFilter.dayOfMonth.toString() + "-" + (_datePickerFilter.month + 1).toString() + "-" + _datePickerFilter.year.toString()
            //update list view
            getData(
                lvadapter,
                selectedCategoryFilter,
                selectedTransactionTypeFilter,
                selectedDate,
                _textTotalIn,
                _textTotalOut
            )
        }
        _btnResetFilter.setOnClickListener {
            //reset category
            spinnerCategoryFilter.setSelection(0)
            selectedCategoryFilter = spinnerCategoryFilter.selectedItem.toString()
            //reset type
            spinnerTransactionTypeFilter.setSelection(0)
            selectedTransactionTypeFilter = spinnerTransactionTypeFilter.selectedItem.toString()
            //reset date
            val today = Calendar.getInstance()
            _datePickerFilter.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH))
            selectedDate = "00-00-0000"
            //update list view
            getData(
                lvadapter,
                selectedCategoryFilter,
                selectedTransactionTypeFilter,
                selectedDate,
                _textTotalIn,
                _textTotalOut
            )
        }

        //change category spinner items based on transaction type

        var lastType = "" //additional variable to prevent continuous spinner array update
        spinnerTransactionTypeFilter.addOnLayoutChangeListener { view, i, i2, i3, i4, i5, i6, i7, i8 ->
            if(spinnerTransactionTypeFilter.selectedItem.toString() == "Income" && lastType != "Income") {
                ArrayAdapter.createFromResource(
                    this,
                    R.array.categoryFilterIncome,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCategoryFilter.adapter = adapter
                }

                lastType = "Income"
            }
            else if (spinnerTransactionTypeFilter.selectedItem.toString() == "Expense" && lastType != "Expense") {
                ArrayAdapter.createFromResource(
                    this,
                    R.array.categoryFilterExpense,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCategoryFilter.adapter = adapter
                }

                lastType = "Expense"
            }
            else if(spinnerTransactionTypeFilter.selectedItem.toString() == "All types" && lastType != "None"){
                ArrayAdapter.createFromResource(
                    this,
                    R.array.categoryFilter,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCategoryFilter.adapter = adapter
                }

                lastType = "None"
            }
        }
    }

    fun getData(
        lvadapter: ArrayAdapter<String>,
        selectedCategoryFilter: String,
        selectedTransactionTypeFilter: String,
        selectedDate: String,
        _textTotalIn: TextView,
        _textTotalOut: TextView
    ) {
        var counter = 0
        db.collection("dataTransaction")
            .get()
            .addOnSuccessListener { result ->
                var moneyOutTotal = 0
                var moneyInTotal = 0

                data.clear()
                for (document in result) {
                    if((selectedTransactionTypeFilter == document.data["type"].toString() || selectedTransactionTypeFilter == "All types")
                        && (selectedCategoryFilter == document.data["category"].toString() || selectedCategoryFilter == "All categories")
                        && (selectedDate == document.data["date"].toString() || selectedDate == "00-00-0000")) {
                        addData(document, counter)
                        //count total money in & out
                        if(document.data["type"].toString() == "Expense") { //money out
                            moneyOutTotal += Integer.parseInt(document.data["amount"].toString())
                        }
                        else {//money in
                            moneyInTotal += Integer.parseInt(document.data["amount"].toString())
                        }
                        counter++
                    }
                }
                if(data.size == 0) {
                    data.add("No transaction matched with the filter")
                }
                _textTotalIn.text = "Total In = Rp " + moneyInTotal.toString()
                _textTotalOut.text = "Total Out = Rp " + moneyOutTotal.toString()
                lvadapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    fun addData (document: QueryDocumentSnapshot, counter: Int) {
        var str = ""

        str += counter+1
        str += ")\n"

        str += "Date         : "
        str += document.data["date"].toString()
        str += "\n"

        str += "Amount   : Rp "
        str += document.data["amount"].toString()
        str += "\n"

        str += "Category : "
        str += document.data["category"].toString()
        str += "\n"

        str += "Notes      : "
        str += document.data["notes"].toString()
        str += "\n"

        str += "Type        : "
        str += document.data["type"].toString()
        str += "\n"

        data.add(str)

        str = ""
    }
}
