package com.uai.uaigas.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.uai.uaigas.R
import com.uai.uaigas.api.RetrofitClient
import com.uai.uaigas.model.Combustivel
import com.uai.uaigas.model.CombustivelAdapter
import com.uai.uaigas.model.Posto
import com.uai.uaigas.model.PostoAdapter

class GasStationList : AppCompatActivity() {

    lateinit var gasStationList: ArrayList<Posto>;
    var recycler: RecyclerView? = null
    var adapter: PostoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gas_station_list)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    fun newGasStation(view: View) {

    }


}