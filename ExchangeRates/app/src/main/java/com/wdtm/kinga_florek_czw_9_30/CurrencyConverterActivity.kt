package com.wdtm.kinga_florek_czw_9_30

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mynameismidori.currencypicker.CurrencyPicker


class CurrencyConverterActivity : AppCompatActivity() {
    internal lateinit var changeToButton: Button
    internal lateinit var changeFromButton: Button
    internal lateinit var currencyPicker: CurrencyPicker
    internal lateinit var currencyToText: TextView
    internal lateinit var currencyFromText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)
        changeToButton = findViewById(R.id.changeToButton)
        changeFromButton = findViewById(R.id.changeFromButton)
        changeToButton.setOnClickListener { changeCurrency() }
        changeFromButton.setOnClickListener { changeCurrency() }

        currencyFromText = findViewById(R.id.currencyFromText)
        currencyToText = findViewById(R.id.currencyToText)

        currencyPicker = CurrencyPicker.newInstance("Select Currency")

        currencyPicker.setListener { name, code, symbol, flagDrawableResID ->
            chooseCurrency(code)
            currencyPicker.dismiss()
        }
    }

    private fun chooseCurrency(code: String) {
        currencyToText.text = code
        currencyFromText.text = code
    }

    private fun changeCurrency() {
        currencyPicker.show(supportFragmentManager, "CURRENCY_PICKER")
    }
}