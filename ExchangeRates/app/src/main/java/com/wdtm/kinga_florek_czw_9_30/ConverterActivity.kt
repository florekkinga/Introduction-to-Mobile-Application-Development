package com.wdtm.kinga_florek_czw_9_30

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray

class ConverterActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private lateinit var convertButton: Button
    private lateinit var currencyPicker: NumberPicker
    private lateinit var secondCurrencyEditTextView: TextView
    private lateinit var directionButton: ToggleButton
    private lateinit var zlotyAmountEditTextView: TextView

    private var currencies: Array<CurrencyDetails> = arrayOf<CurrencyDetails>()
    private lateinit var currentCurrency: CurrencyDetails
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)

        resultTextView = findViewById(R.id.resultText)
        convertButton = findViewById(R.id.convertButton)
        currencyPicker = findViewById(R.id.numberPicker)
        secondCurrencyEditTextView = findViewById(R.id.currencyEditText)
        zlotyAmountEditTextView = findViewById(R.id.plnEditText)
        directionButton = findViewById(R.id.directionButton)
        progressBar = findViewById(R.id.progressBar)

        resultTextView.text = ""
        zlotyAmountEditTextView.text = "0.0"
        secondCurrencyEditTextView.text = "0.0"

        convertButton.isActivated = false

        convertButton.setOnClickListener { calculate() }

        makeRequest()

    }

    private fun calculate() {
        if(directionButton.isChecked){
            //Other -> PLN
            val secondCurrencyAmount = secondCurrencyEditTextView.text.toString().toDouble()
            val result = currentCurrency.rate * secondCurrencyAmount
            resultTextView.text = getString(R.string.resultTextString, secondCurrencyAmount, currentCurrency.currencyCode, result, "PLN")
        } else {
            //PLN -> Other
            val rate = 1/currentCurrency.rate
            val zlotyAmount = zlotyAmountEditTextView.text.toString().toDouble()
            val result = rate*zlotyAmount
            resultTextView.text = getString(R.string.resultTextString, zlotyAmount, "PLN", result, currentCurrency.currencyCode)
        }
    }

    private fun makeRequest() {
        val queue = DataHolder.queue
        val url = "http://api.nbp.pl/api/exchangerates/tables/A/last/2?format=json"

        val currenciesListRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                println("SUCCESS!")
                progressBar.visibility = View.GONE
                loadData(response)
            },
            {
                progressBar.visibility = View.VISIBLE
                Toast.makeText(this, "Brak internetu. Proszę włącz internet i zrestartuj aplikację", Toast.LENGTH_LONG).show();
                println("ERROR!!!!")
            })
        val urlB = "http://api.nbp.pl/api/exchangerates/tables/B/last/2?format=json"
        val currenciesListRequestB = JsonArrayRequest(Request.Method.GET, urlB, null,
            { response ->
                println("SUCCESS!")
                progressBar.visibility = View.GONE
                loadData(response, false)
                setPicker()
            },
            {
                progressBar.visibility = View.VISIBLE
                Toast.makeText(this, "Brak internetu. Proszę włącz internet i zrestartuj aplikację", Toast.LENGTH_LONG).show();
                println("ERROR!!!!")
            })

        queue.add(currenciesListRequest)
        queue.add(currenciesListRequestB)
    }

    private fun setPicker() {
        convertButton.isActivated = true
        currentCurrency = currencies.first()

        currencyPicker.minValue = 0
        currencyPicker.maxValue = currencies.size-1
        currencyPicker.displayedValues = currencies.map { it.currencyCode }.toTypedArray()

        val valueListener = NumberPicker.OnValueChangeListener{_,_, newVal ->
            currentCurrency = currencies[newVal]
        }

        currencyPicker.setOnValueChangedListener(valueListener)
    }

    private fun loadData(response: JSONArray?, a :Boolean = true) {
        response?.let {
            val rates = response.getJSONObject(1).getJSONArray("rates")
            val previousRates = response.getJSONObject(0).getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpData = arrayOfNulls<CurrencyDetails>(ratesCount)

            for (i in 0 until ratesCount) {
                val currencyCode = rates.getJSONObject(i).getString("code")
                val currentRate = rates.getJSONObject(i).getDouble("mid")
                val previousRate = previousRates.getJSONObject(i).getDouble("mid")
                val flag = DataHolder.getFlagForCurrency(currencyCode)
                val isGrowing = DataHolder.getArrowForGrowing(previousRate < currentRate)
                val currencyObject = CurrencyDetails(currencyCode, currentRate, flag, isGrowing, a)

                tmpData[i] = currencyObject
            }

            this.currencies += tmpData as Array<CurrencyDetails>
            this.currencies.distinctBy { it.currencyCode }
        }
    }
}