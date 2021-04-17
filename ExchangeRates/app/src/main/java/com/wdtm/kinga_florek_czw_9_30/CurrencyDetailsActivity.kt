package com.wdtm.kinga_florek_czw_9_30

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONArray
import org.json.JSONObject


class CurrencyDetailsActivity : AppCompatActivity() {
    internal lateinit var currencyName: TextView
    internal lateinit var todayRate: TextView
    internal lateinit var yesterdayRate: TextView
    internal lateinit var lineChartMonth: LineChart
    internal lateinit var lineChartWeek: LineChart
    internal lateinit var currencyCode: String
    internal lateinit var historicRates: Array<Pair<String, Double>>
    internal var isGold: Boolean = false
    internal lateinit var table: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_details)

        currencyName = findViewById(R.id.details_currency)
        todayRate = findViewById(R.id.todayRateText)
        yesterdayRate = findViewById(R.id.previousRateText)
        lineChartMonth = findViewById(R.id.lineChart)
        lineChartWeek = findViewById(R.id.lineChartWeek)
        table = if (intent.getBooleanExtra("table", true)) {
            "A"
        } else {
            "B"
        }
        currencyCode = intent.getStringExtra("currencyCode") ?: "USD"
        if (currencyCode == "GOLD") {
            isGold = true
            title = "Złoto"
        }

        getHistoricRates()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //Take me back to the main activity
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getHistoricRates() {
        val queue = DataHolder.queue
        val url = if (isGold) {
            "http://api.nbp.pl/api/cenyzlota/last/30?format=json"
        } else {
            "http://api.nbp.pl/api/exchangerates/rates/%s/%s/last/30?format=json".format(
                table,
                currencyCode
            )
        }
        val historicRatesRequest = if (isGold) {
            JsonArrayRequest(Request.Method.GET, url, null, { response ->
                println("Success!")
                loadGoldData(response)
                showData()
            },
                {
                    Toast.makeText(this, "Brak internetu. Proszę włącz internet i zrestartuj aplikację", Toast.LENGTH_LONG).show();
                    println("ERROR!!!!")})
        } else {
            JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    println("Success!")
                    loadData(response)
                    showData()
                },
                {
                    Toast.makeText(this, "Brak internetu. Proszę włącz internet i zrestartuj aplikację", Toast.LENGTH_LONG).show();
                    println("ERROR!!!!")
                    println("ERROR!!!") })
        }
        queue.add(historicRatesRequest)
    }

    private fun loadGoldData(response: JSONArray?) {
        response?.let {
            val ratesCount = response.length()
            val tmpData = arrayOfNulls<Pair<String, Double>>(ratesCount)
            for (i in 0 until ratesCount) {
                val date = response.getJSONObject(i).getString("data")
                val currentRate = response.getJSONObject(i).getDouble("cena")
                tmpData[i] = Pair(date, currentRate)
            }
            historicRates = tmpData as Array<Pair<String, Double>>
        }
    }

    private fun loadData(response: JSONObject?) {
        response?.let {
            val rates = response.getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpData = arrayOfNulls<Pair<String, Double>>(ratesCount)
            for (i in 0 until ratesCount) {
                val date = rates.getJSONObject(i).getString("effectiveDate")
                val currentRate = rates.getJSONObject(i).getDouble("mid")
                tmpData[i] = Pair(date, currentRate)
            }
            historicRates = tmpData as Array<Pair<String, Double>>
        }
    }

    private fun showData() {
        currencyName.text = currencyCode
        todayRate.text = getString(R.string.todayRateText, historicRates.last().second)
        yesterdayRate.text =
            getString(R.string.previousRateText, historicRates[historicRates.size - 2].second)

        val entriesMonth = ArrayList<Entry>()
        for ((index, element) in historicRates.withIndex()) {
            entriesMonth.add(Entry(index.toFloat(), element.second.toFloat()))
        }

        val entriesWeek = ArrayList<Entry>()
        for ((index, element) in historicRates.takeLast(7).withIndex()) {
            entriesWeek.add(Entry(index.toFloat(), element.second.toFloat()))
        }

//        for (i in 7 downTo 1) {
//            val element = historicRates[historicRates.size-i-1].second.toFloat()
//            entriesWeek.add(Entry(i.toFloat(), element))
//        }
        drawChart(entriesMonth, "30", lineChartMonth)
        drawChart(entriesWeek, "7", lineChartWeek)
    }

    private fun drawChart(entries: ArrayList<Entry>, days: String, lineChart: LineChart) {
        val entriesDataSet = LineDataSet(entries, "Kurs")
        entriesDataSet.lineWidth = 5f
        entriesDataSet.circleRadius = 8f
        entriesDataSet.color = R.color.black
        entriesDataSet.setCircleColor(R.color.black)
        entriesDataSet.setDrawValues(true)

        val description = Description()
        val lineData = LineData(entriesDataSet)
        description.text = "Kurs %s z ostatnich %s dni".format(currencyCode, days)
        lineChart.description = description
        lineChart.data = lineData
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.xAxis.setCenterAxisLabels(true)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(historicRates.map { it.first }
            .toTypedArray())
        lineChart.animateX(3000, Easing.EaseOutBack)
    }
}