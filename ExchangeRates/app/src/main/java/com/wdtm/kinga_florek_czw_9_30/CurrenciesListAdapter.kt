package com.wdtm.kinga_florek_czw_9_30

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CurrenciesListAdapter(var dataSet: Array<CurrencyDetails>) : RecyclerView.Adapter<CurrenciesListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val currencyCodeTextView: TextView
        val currentRateTextView: TextView
        val flagView: ImageView

        init{
            currencyCodeTextView = view.findViewById(R.id.currencyCodeTextView)
            currentRateTextView = view.findViewById(R.id.rateTextView)
            flagView = view.findViewById(R.id.flagView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.currency_rate, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int){
        val currency = dataSet[position]
        viewHolder.currencyCodeTextView.text = currency.currencyCode
        viewHolder.currentRateTextView.text = currency.rate.toString()
        viewHolder.flagView.setImageResource(currency.flag)
        viewHolder.itemView.setOnClickListener { goToDetails(position) }
    }

    private fun goToDetails(position: Int) {

    }

    override fun getItemCount() = dataSet.size
}