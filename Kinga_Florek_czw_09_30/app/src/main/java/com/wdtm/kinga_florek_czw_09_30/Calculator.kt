package com.wdtm.kinga_florek_czw_09_30

class Calculator : CalculatorItf {

    enum class Operations {
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION,
        PERCENT,
        SQUARE_ROOT
    }

    internal lateinit var digits: MutableList<Int>
    internal lateinit var operations: MutableList<Int>

    override fun addNumber(num: Double) {
        TODO("Not yet implemented")
    }

    override fun addOperation(op: String) {
        TODO("Not yet implemented")
    }

    override fun evaluateFormula(): Double {
        TODO("Not yet implemented")
    }
}