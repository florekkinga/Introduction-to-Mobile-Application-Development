package com.wdtm.kinga_florek_czw_09_30

// Kinga Florek
// Zrealizowane podpunkty:
// - całość podpuktu 1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var digits: List<Button>
    private lateinit var operations: List<Button>
    private lateinit var equalsButton: Button
    private lateinit var dotButton: Button
    private lateinit var clearButton: Button
    private lateinit var resultLabel: TextView

    private val calculatorEngine: CalculatorItf = Calculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultLabel = findViewById(R.id.result)
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
        TODO("Not yet implemented")
    }

    private fun digitPressed(button: Button) {
        TODO("Not yet implemented")
    }

    private fun dotButtonPressed() {
        TODO("Not yet implemented")
    }

    private fun clear() {
        TODO("Not yet implemented")
    }

    private fun evaluateFormula() {
        TODO("Not yet implemented")
    }
}