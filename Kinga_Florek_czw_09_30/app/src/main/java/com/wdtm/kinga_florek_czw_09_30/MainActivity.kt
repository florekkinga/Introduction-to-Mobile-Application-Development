package com.wdtm.kinga_florek_czw_09_30

// Kinga Florek
// Zrealizowane podpunkty:
// - całość podpuktu 1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var digits: List<Button>
    private lateinit var operations: List<Button>
    private lateinit var equalsButton: Button
    private lateinit var dotButton: Button
    private lateinit var clearButton: Button
    private lateinit var displayLabel: TextView

    private val calculatorEngine: CalculatorItf = Calculator()
    private var decimal: Boolean = false
    private var display: String = "0"
        set(value){
            displayLabel.text = value
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayLabel = findViewById(R.id.result)
        equalsButton = findViewById(R.id.equals)
        dotButton = findViewById(R.id.dot)
        clearButton = findViewById(R.id.clear)

        val digitsIDs = listOf(
            R.id._0, R.id._1, R.id._2, R.id._3, R.id._4, R.id._5,
            R.id._6, R.id._7, R.id._8, R.id._9
        )
        val operationsIDs = listOf(
            R.id.addition, R.id.subtraction, R.id.multiplication,
            R.id.division, R.id.percent, R.id.square_root
        )

        digits = digitsIDs.map { id -> findViewById<Button>(id) }
        operations = operationsIDs.map { id -> findViewById<Button>(id) }

        equalsButton.setOnClickListener { evaluateFormula() }
        clearButton.setOnClickListener { clear() }
        dotButton.setOnClickListener { dotButtonPressed() }
        digits.forEach { digit -> digit.setOnClickListener{i -> digitPressed(i as Button)} }
        operations.forEach { operation -> operation.setOnClickListener{i -> operationPressed(i as Button)} }
    }

    private fun operationPressed(button: Button) {
        decimal = false
        var operation = ""
        when(button.id){
            R.id.addition -> operation = getString(R.string.addition)
            R.id.subtraction -> operation = getString(R.string.subtraction)
            R.id.multiplication -> operation = getString(R.string.multiplication)
            R.id.division -> operation = getString(R.string.division)
            R.id.percent -> operation = getString(R.string.percent)
            R.id.square_root -> operation = getString(R.string.square_root)
        }
        display += operation
        calculatorEngine.addOperation(operation)
    }

    private fun digitPressed(button: Button) {
        val digit = digits.indexOf(button)
        if(display == getString(R.string._0)){
            display = digit.toString()
        }
        else {
            display += digit.toString()
        }
        calculatorEngine.addNumber(digit)
    }

    private fun dotButtonPressed() {
        if(!decimal) {
            calculatorEngine.setDecimal(true)
            decimal = true
            display += getString(R.string.dot)
        }
    }

    private fun clear() {
        calculatorEngine.clear()
        display = getString(R.string._0)
        decimal = false
    }

    private fun evaluateFormula() {
        try{
            calculatorEngine.evaluateFormula()
        }
        catch (e: Exception){
            val message = getString(R.string.message)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            display = getString(R.string._0)
        }
    }
}