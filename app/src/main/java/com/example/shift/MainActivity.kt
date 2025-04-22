package com.example.shift

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var buttonDate: Button
    private lateinit var textViewDate: TextView
    private lateinit var buttonSave: Button
    private lateinit var recyclerViewShifts: RecyclerView
    private lateinit var adapter: ShiftAdapter
    private val shiftsList = mutableListOf<String>()
    private lateinit var selectedDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextName = findViewById(R.id.editTextName)
        editTextPhone = findViewById(R.id.editTextPhone)
        buttonDate = findViewById(R.id.buttonDate)
        textViewDate = findViewById(R.id.textViewDate)
        buttonSave = findViewById(R.id.buttonSave)
        recyclerViewShifts = findViewById(R.id.recyclerViewShifts)

        adapter = ShiftAdapter(shiftsList)
        recyclerViewShifts.layoutManager = LinearLayoutManager(this)
        recyclerViewShifts.adapter = adapter

        buttonDate.setOnClickListener { showDatePicker() }
        buttonSave.setOnClickListener { saveShift() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            textViewDate.text = "Tanggal: $selectedDate"
        }, year, month, day).show()
    }

    private fun saveShift() {
        val name = editTextName.text.toString().trim()
        val phone = editTextPhone.text.toString().trim()

        if (name.isNotEmpty() && phone.isNotEmpty() && ::selectedDate.isInitialized) {
            val shiftInfo = """
                |Nama: $name
                |Telepon: $phone
                |Tanggal: $selectedDate
            """.trimMargin()

            shiftsList.add(shiftInfo)
            adapter.notifyItemInserted(shiftsList.size - 1)
            recyclerViewShifts.smoothScrollToPosition(shiftsList.size - 1)
            Toast.makeText(this, "Shift disimpan!", Toast.LENGTH_SHORT).show()
            clearInputs()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Silakan lengkapi semua field!")
                .setPositiveButton("OK", null)
                .show()
        }
    }

    private fun clearInputs() {
        editTextName.text.clear()
        editTextPhone.text.clear()
        textViewDate.text = "Tanggal: "
    }
}

class ShiftAdapter(private val shifts: List<String>) : RecyclerView.Adapter<ShiftAdapter.ShiftViewHolder>() {

    class ShiftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewShift: TextView = view.findViewById(R.id.textViewShift)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShiftViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shift, parent, false)
        return ShiftViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShiftViewHolder, position: Int) {
        holder.textViewShift.text = shifts[position]
    }

    override fun getItemCount(): Int {
        return shifts.size
    }
}