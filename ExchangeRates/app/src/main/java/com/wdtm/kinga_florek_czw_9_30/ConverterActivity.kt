package com.wdtm.kinga_florek_czw_9_30

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.ToggleButton
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray

class ConverterActivity : AppCompatActivity() {
    internal lateinit var resultTextView: TextView
    internal lateinit var convertButton: Button
    internal lateinit var currencyPicker: NumberPicker
    internal lateinit var secondCurrencyEditTextView: TextView
    internal lateinit var directionButton: ToggleButton
    internal lateinit var zlotyAmountEditTextView: TextView

    internal lateinit var currencies: Array<CurrencyDetails>
    internal lateinit var currentCurrency: CurrencyDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)

        resultTextView = findViewById(R.id.resultText)
        convertButton = findViewById(R.id.convertButton)
        currencyPicker = findViewById(R.id.numberPicker)
        secondCurrencyEditTextView = findViewById(R.id.currencyEditText)
        zlotyAmountEditTextView = findViewById(R.id.plnEditText)
        directionButton = findViewById(R.id.directionButton)

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

    fun makeRequest() {
        val queue = DataHolder.queue
        val url = "http://api.nbp.pl/api/exchangerates/tables/A/last/2?format=json"

        val currenciesListRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                println("SUCCESS!")
                loadData(response)
                setPicker()
            },
            {
                println("ERROR!!!!")
            })

        queue.add(currenciesListRequest)
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

    private fun loadData(response: JSONArray?) {
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
                val currencyObject = CurrencyDetails(currencyCode, currentRate, flag, isGrowing)

                tmpData[i] = currencyObject
            }

            this.currencies = tmpData as Array<CurrencyDetails>
        }
    }
}