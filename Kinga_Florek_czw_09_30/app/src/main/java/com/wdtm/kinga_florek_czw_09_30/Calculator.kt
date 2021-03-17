package com.wdtm.kinga_florek_czw_09_30

import java.lang.Exception
import kotlin.math.sqrt

class Calculator : CalculatorItf {

    private var decimal: Boolean = false
    private var isPrevOperation: Boolean = false
    private var numbersAndOperations: MutableList<String> = mutableListOf("0")
    private var operations: List<String> =
        listOf("PERCENT", "ADDITION", "SUBTRACTION", "MULTIPLICATION", "DIVISION", "SQUARE_ROOT")

    override fun addNumber(num: Int) {
        var number = ""
        if (decimal) {
            number = addDot(number)
        }
        number += num.toString()
        if (numbersAndOperations.isEmpty() || isPrevOperation) {
            numbersAndOperations.add(number)
        } else {
            numbersAndOperations[numbersAndOperations.lastIndex] += number
        }

        isPrevOperation = false
    }

    override fun clear(displayLabel: String) {
        numbersAndOperations.clear()
        if (displayLabel != "") {
            numbersAndOperations.add(displayLabel)
        }
        setDecimal(false)
        isPrevOperation = false
    }

    override fun addOperation(op: String) {
        if (isPrevOperation && numbersAndOperations.last() != "PERCENT") {
            numbersAndOperations[numbersAndOperations.lastIndex] = op
        } else {
            numbersAndOperations.add(op)
        }
        isPrevOperation = true
    }

    override fun evaluateFormula(): Double {
        if (numbersAndOperations.last() in operations.subList(1, 5)
            || numbersAndOperations.first() in operations.subList(0, 4)
        ) {
            throw Exception()
        }
        var numbersToSum: MutableList<Double> = mutableListOf()
        var sign: Int = 1
        var multiplication: Boolean = false
        var division: Boolean = false
        var squareRoot: Boolean = false
        for (element in numbersAndOperations) {
            when (element) {
                "ADDITION" -> sign = 1
                "SUBTRACTION" -> sign = -1
                "MULTIPLICATION" -> multiplication = true
                "DIVISION" -> division = true
                "SQUARE_ROOT" -> squareRoot = true
                "PERCENT" -> numbersToSum[numbersToSum.lastIndex] *= 0.01
                !in operations -> {
                    when {
                        multiplication -> {
                            numbersToSum = multiplication(numbersToSum, element)
                            multiplication = false
                        }
                        division -> {
                            numbersToSum = division(numbersToSum, element)
                            division = false
                        }
                        squareRoot -> {
                            numbersToSum = squareRoot(numbersToSum, element)
                            squareRoot = false
                        }
                        else -> numbersToSum.add(sign * element.toDouble())
                    }
                }
            }
        }
        return numbersToSum.sum()
    }

    override fun setDecimal(boolean: Boolean) {
        decimal = boolean
    }

    private fun division(numbersToSum: MutableList<Double>, element: String) : MutableList<Double> {
        if (element.toDouble() == 0.0) {
            throw Exception()
        } else {
            numbersToSum[numbersToSum.lastIndex] /= element.toDouble()
        }
        return numbersToSum
    }

    private fun multiplication(numbersToSum: MutableList<Double>, element: String) : MutableList<Double> {
        numbersToSum[numbersToSum.lastIndex] *= element.toDouble()
        return numbersToSum
    }

    private fun squareRoot(numbersToSum: MutableList<Double>, element: String) : MutableList<Double> {
        if (numbersToSum.isEmpty()) {
            numbersToSum.add(sqrt(element.toDouble()))
        } else {
            numbersToSum[numbersToSum.lastIndex] *= sqrt(element.toDouble())
        }
        return numbersToSum
    }

    private fun addDot(num: String) : String {
        var number = num
        if (numbersAndOperations.isEmpty()) {
            number += 0
        }
        number += "."
        setDecimal(false)
        return number
    }
}