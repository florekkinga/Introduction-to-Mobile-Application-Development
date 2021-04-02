package com.wdtm.kinga_florek_czw_9_30

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    internal lateinit var currenciesListRecyclerView: RecyclerView
    internal lateinit var adapter: CurrenciesListAdapter
    internal lateinit var dataSet: Array<CurrencyDetails>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currenciesListRecyclerView = findViewById(R.id.currenciesListRecyclerView)
        currenciesListRecyclerView.layoutManager = LinearLayoutManager(this)
        val tmpData = arrayOf(CurrencyDetails("EUR", 4.56), CurrencyDetails("CHF", 4.55))
        adapter = CurrenciesListAdapter(tmpData)
        currenciesListRecyclerView.adapter = adapter
        DataHolder.prepare(applicationContext)
        makeRequest()
    }

    fun makeRequest() {
        val queue = newRequestQueue(applicationContext)
        val url = "http://api.nbp.pl/api/exchangerates/tables/A?format=json"

        val currenciesListRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                println("SUCCESS!")
                loadData(response)
                adapter.dataSet = dataSet
                adapter.notifyDataSetChanged()
            },
            Response.ErrorListener {
                println("ERROR!!!!")
            })

        queue.add(currenciesListRequest)
    }

    private fun loadData(response: JSONArray?) {
        response?.let{
            val rates = response.getJSONObject(0).getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpData = arrayOfNulls<CurrencyDetails>(ratesCount)

            for (i in 0 until ratesCount){
                val currencyCode = rates.getJSONObject(i).getString("code")
                val currentRate = rates.getJSONObject(i).getDouble("mid")
                val currencyObject = CurrencyDetails(currencyCode, currentRate)

                tmpData[i] = currencyObject
            }

            this.dataSet = tmpData as Array<CurrencyDetails>
        }
    }

}