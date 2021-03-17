package com.wdtm.kinga_florek_czw_09_30

// Kinga Florek
// Zrealizowane punkty:
// - całość punktu 1
// - całość punktu 2 -> własna implementacja
// - wydaje mi się, że całość puktu 3
// - całość punktu 4
// - całość punktu 5
// - całość punktu 6 -> w przypadku zbyt długich liczb zastosowałam metodę ellipsize start,
//   która na początku liczby lub działania wstawia kropki, np. ...2+5*9.0+2-5+4
// - 7 punkt nie został zrealizowany, ponieważ zrobiłam punkt 9
// - działa obsługa pierwiastków i procentów, jednak pierwiastki działają w taki sposób, że wpisując
//   np √16 otrzymamy wynik 4, natomiast jeżeli chcemy użyć pierwiastka w środku działania wymagane
//   jest aby podać przed nim cyfrę, np. 16 + 1√4 lub 3 - 5√20, natomiast procenty działają w taki
//   sposób, że jeżeli wpiszemy 5% to zamieni nam to na 0.05 ale możemy po symbolu procenta użyć
//   symbolu mnożenia, czyli działa np. operacja 5% * 50
// - całość punktu 9


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
    private var isPrevOperation: Boolean = false
    private var prevOperation: String = ""
    private var decimal: Boolean = false
    private var display: String = ""
        set(value) {
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
        val operationsMap = mapOf(
            R.id.addition to "ADDITION",
            R.id.subtraction to "SUBTRACTION",
            R.id.multiplication to "MULTIPLICATION",
            R.id.division to "DIVISION",
            R.id.percent to "PERCENT",
            R.id.square_root to "SQUARE_ROOT"
        )

        digits = digitsIDs.map { id -> findViewById<Button>(id) }
        operations = operationsMap.keys.map { id -> findViewById<Button>(id) }

        equalsButton.setOnClickListener { evaluateFormula() }
        clearButton.setOnClickListener { clear() }
        dotButton.setOnClickListener { dotButtonPressed() }
        digits.forEach { digit -> digit.setOnClickListener { i -> digitPressed(i as Button) } }
        operations.forEach { operation ->
            operation.setOnClickListener { i ->
                operationPressed(
                    i as Button,
                    operationsMap
                )
            }
        }
    }

    private fun operationPressed(button: Button, operationsMap: Map<Int, String>) {
        decimal = false
        var operation = ""
        when (button.id) {
            R.id.addition -> operation = getString(R.string.addition)
            R.id.subtraction -> operation = getString(R.string.subtraction)
            R.id.multiplication -> operation = getString(R.string.multiplication)
            R.id.division -> operation = getString(R.string.division)
            R.id.percent -> operation = getString(R.string.percent)
            R.id.square_root -> operation = getString(R.string.square_root)
        }
        if (isPrevOperation && (prevOperation != operationsMap[R.id.percent])) {
            display = display.dropLast(1)
            display += operation
        } else {
            display += operation
        }
        isPrevOperation = true
        prevOperation = operationsMap[button.id].toString()
        calculatorEngine.addOperation(prevOperation)
    }

    private fun digitPressed(button: Button) {
        val digit = digits.indexOf(button)
        if (display == "") {
            display = digit.toString()
        } else {
            display += digit.toString()
        }
        calculatorEngine.addNumber(digit)
        if((isPrevOperation || display == "0")&& digit == 0){
            dotButtonPressed()
        }
        isPrevOperation = false
    }

    private fun dotButtonPressed() {
        if (!decimal) {
            calculatorEngine.setDecimal(true)
            decimal = true
            display += getString(R.string.dot)
        }
        isPrevOperation = false
    }

    private fun clear() {
        calculatorEngine.clear()
        display = ""
        decimal = false
        isPrevOperation = false
    }

    private fun evaluateFormula() {
        display = try {
              calculatorEngine.evaluateFormula().toString()
//            String.format("%.3f", calculatorEngine.evaluateFormula())
        } catch (e: Exception) {
            val message = getString(R.string.message)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            ""
        }
        calculatorEngine.clear(display)
        decimal = true
    }
}