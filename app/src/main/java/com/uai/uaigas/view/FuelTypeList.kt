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
import com.uai.uaigas.model.TipoCombustivel
import com.uai.uaigas.model.TipoCombustivelAdapter
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FuelTypeList : AppCompatActivity() {

    lateinit var fuelTypeList: ArrayList<TipoCombustivel>;
    var recycler: RecyclerView? = null
    var adapter: TipoCombustivelAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fuel_type_list)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        RetrofitClient.instance.findAllFuelType().enqueue(object : Callback<List<TipoCombustivel>> {
            override fun onFailure(call: Call<List<TipoCombustivel>>, t: Throwable) {
                Toast.makeText(applicationContext, "Ocorreu um erro, tente novamente mais tarde!", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<List<TipoCombustivel>>,
                response: Response<List<TipoCombustivel>>
            ) {
                when {
                    response.isSuccessful -> {
                        response.body()?.let {
                            fuelTypeList = ArrayList(it)
                            recycler = findViewById(R.id.FuelTypeList)
                            adapter = TipoCombustivelAdapter(fuelTypeList, applicationContext)
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
        startActivity(Intent(applicationContext, EditFuelTypeActivity::class.java))
    }
}