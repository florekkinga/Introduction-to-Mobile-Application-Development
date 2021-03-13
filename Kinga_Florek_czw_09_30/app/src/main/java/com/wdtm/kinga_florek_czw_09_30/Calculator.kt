package com.wdtm.kinga_florek_czw_09_30

import java.lang.Exception

class Calculator : CalculatorItf {

    enum class Operations {
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION,
        PERCENT,
        SQUARE_ROOT
    }

    private var previous: String = ""
    private var decimal: Boolean = false
    private var digitsAndOperations: MutableList<String> = mutableListOf()

    override fun addNumber(num: Int) {
        var number = ""
        if(previous == ""){
            number += "0"
        }
        else{
            number = digitsAndOperations.last()
        }
        if (decimal) {
            number += "."
            setDecimal(false)
        } else {
            number += num.toString()
            if (previous == "") {
                digitsAndOperations.add(number)
            } else {
                digitsAndOperations[digitsAndOperations.lastIndex] = number
            }
            previous = num.toString()
        }
    }

    override fun clear() {
        digitsAndOperations.clear()
        previous = ""
        setDecimal(false)
    }

    override fun addOperation(op: String) {

    }

    override fun evaluateFormula(): Double {
        throw Exception()
    }

    override fun setDecimal(boolean: Boolean){
        decimal = boolean
    }
}