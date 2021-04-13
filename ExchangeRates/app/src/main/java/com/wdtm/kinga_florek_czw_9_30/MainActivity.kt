package com.wdtm.kinga_florek_czw_9_30

/* Kinga Florek, czwartek godzina 9:30
* Zrealizowane podpunkty:
*   1. Layout - tak, obsługa sytuacji awaryjny - nie?
*   2. Ekran startowy posiada 3 przyciski - Kursy walut/Złoto/Przelicznik
*   3. Obsługa tabeli A
*   4. Kursy walut:
*       4a. tak
*       4b. tak
*       4c. skorzystałam z innej biblioteki niż była na zajęciach, więc nie było problemu z flagami
*       4d. tak
*       4e. tak
*   5. Złoto:
*       5a. tak
*       5b. tak
*   6. Przelicznik walut:
*       Stworzyłam layout
*       6a.
*       6b.
**/

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