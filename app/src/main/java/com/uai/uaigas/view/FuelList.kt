package com.uai.uaigas.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uai.uaigas.R
import com.uai.uaigas.api.RetrofitClient
import com.uai.uaigas.model.Combustivel
import com.uai.uaigas.model.CombustivelAdapter
import com.uai.uaigas.model.TipoCombustivel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FuelList : AppCompatActivity() {

    lateinit var fuelList: ArrayList<Combustivel>;
    var recycler: RecyclerView? = null
    var adapter: CombustivelAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fuel_type_list)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        RetrofitClient.instance.findAllFuel().enqueue(object : Callback<List<Combustivel>> {
            override fun onFailure(call: Call<List<Combustivel>>, t: Throwable) {
                Toast.makeText(applicationContext, "Ocorreu um erro, tente novamente mais tarde!", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<List<Combustivel>>,
                response: Response<List<Combustivel>>
            ) {
                when {
                    response.isSuccessful -> {
                        response.body()?.let {
                            fuelList = ArrayList(it)
                            recycler = findViewById(R.id.FuelTypeList)
                            adapter = CombustivelAdapter(fuelList, applicationContext)
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

    fun newFuelType(view: View) {
        startActivity(Intent(applicationContext, EditFuelActivity::class.java))
    }
}