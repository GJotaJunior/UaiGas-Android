package com.uai.uaigas.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uai.uaigas.R
import com.uai.uaigas.api.RetrofitClient
import com.uai.uaigas.model.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GasStationList : AppCompatActivity() {

    lateinit var gasStationList: ArrayList<Endereco>
    var recycler: RecyclerView? = null
    var adapter: PostoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gas_station_list)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        RetrofitClient.instance.findAllAddress().enqueue(object :
            Callback<List<Endereco>> {
            override fun onFailure(call: Call<List<Endereco>>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Ocorreu um erro, tente novamente mais tarde!",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(
                call: Call<List<Endereco>>,
                response: Response<List<Endereco>>
            ) {
                when {
                    response.isSuccessful -> {
                        response.body()?.let {
                            gasStationList = ArrayList(it)
                            recycler = findViewById(R.id.GasStationList)
                            adapter = PostoAdapter(gasStationList, applicationContext)
                            recycler?.layoutManager = LinearLayoutManager(applicationContext)
                            recycler?.adapter = adapter
                        }
                    }
                    response.code() == 400 -> {
                        response.errorBody()?.let {
                            var resp = it.string()
                            if (resp.contains("message")) {
                                resp = JSONObject(resp).getString("message")
                            }
                            Toast.makeText(applicationContext, resp, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    else -> Toast.makeText(
                        applicationContext,
                        "Ocorreu um erro, tente novamente!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun newGasStation(view: View) {
        startActivity(Intent(this, GasStationActivity::class.java))
    }
}