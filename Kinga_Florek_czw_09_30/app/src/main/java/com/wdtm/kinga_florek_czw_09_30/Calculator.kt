package com.wdtm.kinga_florek_czw_09_30

import android.provider.Settings.Global.getString
import java.lang.Exception
import kotlin.math.sqrt
import android.util.Log

class Calculator : CalculatorItf {

    private var previous: String = ""
    private var decimal: Boolean = false
    private var isPrevOperation: Boolean = false
    private var digitsAndOperations: MutableList<String> = mutableListOf("0")
    private var operations: List<String> =
        listOf("PERCENT", "ADDITION", "SUBTRACTION", "MULTIPLICATION", "DIVISION", "SQUARE_ROOT")

    override fun addNumber(num: Int) {
        var number = ""
        if (previous != "" && !isPrevOperation) {
            number = digitsAndOperations.last()
        }
        if (decimal) {
            if (previous == "") {
                number += 0
            }
            number += "."
            setDecimal(false)
        }
        number += num.toString()
        if (previous == "" || isPrevOperation) {
            digitsAndOperations.add(number)
        } else {
            digitsAndOperations[digitsAndOperations.lastIndex] = number
        }
        previous = num.toString()

        isPrevOperation = false
    }

    override fun clear(displayLabel: String) {
        digitsAndOperations.clear()
        if (displayLabel == "") {
            previous = ""
        } else {
            previous = displayLabel
            digitsAndOperations.add(previous)
        }
        setDecimal(false)
        isPrevOperation = false
    }

    override fun addOperation(op: String) {
        if (isPrevOperation && digitsAndOperations.last() != "PERCENT") {
            digitsAndOperations[digitsAndOperations.lastIndex] = op
        } else {
            digitsAndOperations.add(op)
        }
        Log.d("deb", digitsAndOperations.last())
        isPrevOperation = true
    }

    override fun evaluateFormula(): Double {
        Log.d("deb", digitsAndOperations.toString())
        if (digitsAndOperations.last() in operations.subList(1, 5)
            || digitsAndOperations.first() in operations.subList(0, 4)) {
            throw Exception()
        }
        var numbersToSum: MutableList<Double> = mutableListOf()
        var sign: Int = 1
        var multiplication: Boolean = false
        var division: Boolean = false
        var squareRoot: Boolean = false
        for (element in digitsAndOperations) {
            Log.d("deb", element)
            Log.d("deb", numbersToSum.toString())
            when (element) {
                "ADDITION" -> sign = 1
                "SUBTRACTION" -> sign = -1
                "MULTIPLICATION" -> multiplication = true
                "DIVISION" -> division = true
                "PERCENT" -> numbersToSum[numbersToSum.lastIndex] *= 0.01
                "SQUARE_ROOT" -> squareRoot = true
                !in operations -> {
                    when {
                        multiplication -> {
                            numbersToSum[numbersToSum.lastIndex] *= element.toDouble()
                            multiplication = false
                        }
                        division -> {
                            if (element.toDouble() == 0.0) {
                                throw Exception()
                            } else {
                                numbersToSum[numbersToSum.lastIndex] /= element.toDouble()
                            }
                            division = false
                        }
                        squareRoot -> {
                            Log.d("deb", "squ")
                            if (numbersToSum.isEmpty()) {
                                numbersToSum.add(sqrt(element.toDouble()))
                            } else {
                                numbersToSum[numbersToSum.lastIndex] *= sqrt(element.toDouble())
                            }
                            squareRoot = false
                        }
                        else -> numbersToSum.add(sign * element.toDouble())
                    }
                }
            }
        }
        Log.d("deb", digitsAndOperations.toString())
        return numbersToSum.sum()
    }

    override fun setDecimal(boolean: Boolean) {
        decimal = boolean
    }
}