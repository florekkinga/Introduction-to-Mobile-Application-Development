package com.wdtm.kinga_florek_czw_9_30

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley.newRequestQueue
import com.mynameismidori.currencypicker.ExtendedCurrency

//import com.blongho.country_data.Country


object DataHolder {
    lateinit var queue: RequestQueue
    lateinit var currencies: Array<ExtendedCurrency>

    fun prepare(context: Context) {
        queue = newRequestQueue(context)
        currencies = ExtendedCurrency.CURRENCIES
    }

    fun getFlagForCurrency(currencyCode: String): Int {
        return currencies.find { it.code == currencyCode }?.flag ?: R.drawable.world_flag
    }

    fun getArrowForGrowing(isGrowing: Boolean): Int {
        return if(isGrowing){
            R.drawable.up
        } else{
            R.drawable.down
        }
    }
}