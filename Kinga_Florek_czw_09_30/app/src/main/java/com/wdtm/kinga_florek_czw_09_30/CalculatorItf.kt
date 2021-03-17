package com.wdtm.kinga_florek_czw_09_30

interface CalculatorItf {
    fun addNumber(num: Int)
    fun addOperation(op: String)
    fun evaluateFormula(): Double
    fun setDecimal(boolean: Boolean)
    fun clear(displayLabel: String = "")
}