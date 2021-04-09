package com.wdtm.kinga_florek_czw_9_30

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    internal lateinit var exchangeRatesButton: Button
    internal lateinit var goldButton: Button
    internal lateinit var currencyConverterButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exchangeRatesButton = findViewById(R.id.exchange_rates)
        goldButton = findViewById(R.id.gold)
        currencyConverterButton = findViewById(R.id.currency_converter)
        DataHolder.prepare(applicationContext)

        exchangeRatesButton.setOnClickListener { openExchangeRatesActivity() }
        goldButton.setOnClickListener { openGoldActivity() }
        currencyConverterButton.setOnClickListener { openCurrencyConverterActivity() }
    }

    private fun openCurrencyConverterActivity() {
        val intent = Intent(this, CurrencyConverterActivity::class.java).apply {}
        startActivity(intent)
    }

    private fun openGoldActivity() {
        val intent = Intent(this, CurrencyDetailsActivity::class.java).apply {
            putExtra("currencyCode", "GOLD")
        }
        startActivity(intent)
    }

    private fun openExchangeRatesActivity() {
        val intent = Intent(this, ExchangeRatesActivity::class.java).apply {}
        startActivity(intent)
    }
}