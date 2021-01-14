package com.uai.uaigas.view

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.textfield.TextInputLayout
import com.uai.uaigas.R
import com.uai.uaigas.api.RetrofitClient
import com.uai.uaigas.enums.PostoStatus
import com.uai.uaigas.model.Endereco
import com.uai.uaigas.model.Posto
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class GasStationActivity : AppCompatActivity() {
    lateinit var name: TextInputLayout
    lateinit var street: TextInputLayout
    lateinit var number: TextInputLayout
    lateinit var adjunct: TextInputLayout
    lateinit var district: TextInputLayout
    lateinit var city: TextInputLayout
    lateinit var state: TextInputLayout
    lateinit var cep: TextInputLayout
    lateinit var gasStation: Posto
    lateinit var address: Endereco
    var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gasstation)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        name = findViewById(R.id.name)
        street = findViewById(R.id.street)
        number = findViewById(R.id.number)
        adjunct = findViewById(R.id.adjunct)
        district = findViewById(R.id.district)
        city = findViewById(R.id.city)
        state = findViewById(R.id.state)
        cep = findViewById(R.id.cep)

        id = intent.getIntExtra("id", 0)

        chargeGasStationAndAddressInfo(id)
    }

    fun updateGasStationAndAddressInfo(view: View) {
        val validatorEmpty =
            name.editText?.text.toString().isNullOrEmpty() || street.editText?.text.toString()
                .isNullOrEmpty() || number.editText?.text.toString().isNullOrEmpty() ||
                    adjunct.editText?.text.toString()
                        .isNullOrEmpty() || district.editText?.text.toString()
                .isNullOrEmpty() || city.editText?.text.toString()
                .isNullOrEmpty() || state.editText?.text.toString().isNullOrEmpty() ||
                    cep.editText?.text.toString()
                        .isNullOrEmpty()

        when {
            validatorEmpty ->
                Toast.makeText(
                    applicationContext,
                    "Todos os campos precisam estar preenchidos!",
                    Toast.LENGTH_SHORT
                ).show();
            else -> {
                if (gasStation.id != null)
                    gasStation = Posto(
                        id = gasStation.id,
                        descricao = name.editText?.text.toString(),
                        status = PostoStatus.ATIVO
                    )
                else
                    gasStation = Posto(
                        id = null,
                        descricao = name.editText?.text.toString(),
                        status = PostoStatus.ATIVO
                    )
                var locale = getLatLng(street.editText?.text.toString())
                if (locale != null) {
                    if (address.id != null)
                        address = Endereco(
                            id = address.id,
                            logradouro = street.editText?.text.toString(),
                            numero = number.editText?.text.toString().toInt(),
                            complemento = adjunct.editText?.text.toString(),
                            bairro = district.editText?.text.toString(),
                            cidade = city.editText?.text.toString(),
                            estado = state.editText?.text.toString(),
                            cep = cep.editText?.text.toString(),
                            latitude = locale.latitude as Float,
                            longitude = locale.longitude as Float,
                            posto = gasStation
                        )
                    else
                        address = Endereco(
                            id = null,
                            logradouro = street.editText?.text.toString(),
                            numero = number.editText?.text.toString().toInt(),
                            complemento = adjunct.editText?.text.toString(),
                            bairro = district.editText?.text.toString(),
                            cidade = city.editText?.text.toString(),
                            estado = state.editText?.text.toString(),
                            cep = cep.editText?.text.toString(),
                            latitude = locale.latitude as Float,
                            longitude = locale.longitude as Float,
                            posto = gasStation
                        )
                }
            }
        }

        checkIntent(id)
    }

    private fun chargeGasStationAndAddressInfo(id: Int) {
        if (id != 0) {
            RetrofitClient.instance.findAddress(id)
                .enqueue(object : Callback<Endereco> {
                    override fun onFailure(call: Call<Endereco>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Ocorreu um erro durante o carregamento das informações, tente novamente mais tarde!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<Endereco>,
                        response: Response<Endereco>
                    ) {
                        when {
                            response.isSuccessful -> {
                                response.body()?.let {
                                    street.editText?.setText(it.logradouro)
                                    number.editText?.setText(it.numero.toString())
                                    adjunct.editText?.setText(it.complemento)
                                    district.editText?.setText(it.bairro)
                                    city.editText?.setText(it.cidade)
                                    state.editText?.setText(it.estado)
                                    cep.editText?.setText(it.cep)
                                    Log.d("idPosto", it.posto?.id.toString())
                                    it.posto?.id?.let { it1 ->
                                        RetrofitClient.instance.findGasStation(it1.toInt())
                                            .enqueue(object : Callback<Posto> {
                                                override fun onFailure(
                                                    call: Call<Posto>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Ocorreu um erro durante o carregamento das informações, tente novamente mais tarde!",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }

                                                override fun onResponse(
                                                    call: Call<Posto>,
                                                    response: Response<Posto>
                                                ) {
                                                    when {
                                                        response.isSuccessful -> {
                                                            response.body()?.let {
                                                                Log.d("posto", it.id.toString())
                                                                name.editText?.setText(it.descricao)
                                                            }
                                                        }
                                                        response.code() == 400 -> {
                                                            response.errorBody()?.let {
                                                                var resp = it.string()
                                                                if (resp.contains("message")) {
                                                                    resp =
                                                                        JSONObject(resp).getString("message")
                                                                }
                                                                Toast.makeText(
                                                                    applicationContext,
                                                                    resp,
                                                                    Toast.LENGTH_SHORT
                                                                )
                                                                    .show()
                                                            }
                                                        }
                                                        else -> Toast.makeText(
                                                            applicationContext,
                                                            "Ocorreu um erro durante o carregamento das informações, tente novamente!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            }
                                            )
                                    }
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
                                "Ocorreu um erro durante o carregamento das informações, tente novamente!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        }
    }

    private fun updateGasStationAndAddress() {
        gasStation.id?.let {
            RetrofitClient.instance.updateGasStation(it, gasStation)
                .enqueue(object : Callback<Posto> {
                    override fun onFailure(call: Call<Posto>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Ocorreu um erro ao atualizar o posto, tente novamente mais tarde!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<Posto>,
                        response: Response<Posto>
                    ) {
                        when {
                            response.isSuccessful -> {
                                response.body()?.let {
                                    Toast.makeText(
                                        applicationContext,
                                        "Posto atualizado com sucesso!",
                                        Toast.LENGTH_SHORT
                                    ).show()
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
                                "Ocorreu um erro ao atualizar o posto, tente novamente!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        }


        address.id?.let {
            RetrofitClient.instance.updateAddress(it, address)
                .enqueue(object : Callback<Endereco> {
                    override fun onFailure(call: Call<Endereco>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Ocorreu um erro ao atualizar o endereço, tente novamente mais tarde!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<Endereco>,
                        response: Response<Endereco>
                    ) {
                        when {
                            response.isSuccessful -> {
                                response.body()?.let {
                                    Toast.makeText(
                                        applicationContext,
                                        "Endereço atualizado com sucesso!",
                                        Toast.LENGTH_SHORT
                                    ).show()
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
                                "Ocorreu um erro ao atualizar o endereço, tente novamente!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        }
    }


    private fun insertGasStationAndAddress() {
        RetrofitClient.instance.insertGasStation(gasStation)
            .enqueue(object : Callback<Posto> {
                override fun onFailure(call: Call<Posto>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Ocorreu um erro ao cadastrar o posto, tente novamente mais tarde!",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<Posto>,
                    response: Response<Posto>
                ) {
                    when {
                        response.isSuccessful -> {
                            response.body()?.let {
                                Toast.makeText(
                                    applicationContext,
                                    "Posto cadastrado com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
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
                            "Ocorreu um erro ao cadastrar o posto, tente novamente!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })


        RetrofitClient.instance.insertAddress(address)
            .enqueue(object : Callback<Endereco> {
                override fun onFailure(call: Call<Endereco>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Ocorreu um erro ao cadastrar o endereço, tente novamente mais tarde!",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<Endereco>,
                    response: Response<Endereco>
                ) {
                    when {
                        response.isSuccessful -> {
                            response.body()?.let {
                                Toast.makeText(
                                    applicationContext,
                                    "Endereço cadastrado com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
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
                            "Ocorreu um erro ao cadastrar o endereço, tente novamente!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }


    private fun checkIntent(id: Int) {
        if (id == 0)
            insertGasStationAndAddress()
        else
            updateGasStationAndAddress()
    }

    private fun getLatLng(location: String?): LatLng? {
        var retorno = LatLng(0.0, 0.0)

        val gc = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = gc.getFromLocationName(location, 5)
        if (addresses.size == 0) return retorno
        val ll: MutableList<LatLng> = ArrayList(addresses.size)
        for (a in addresses) {
            if (a.hasLatitude() && a.hasLongitude()) {
                ll.add(LatLng(a.getLatitude(), a.getLongitude()))
            }
        }
        if (ll.size != 0) retorno = ll[0]

        return retorno
    }
}