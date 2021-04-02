package com.wdtm.kinga_florek_czw_9_30

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley.newRequestQueue
import com.blongho.country_data.Country


object DataHolder {
    lateinit var queue: RequestQueue

    fun prepare(context: Context){
        queue = newRequestQueue(context)
        World.init(context)
    }
}