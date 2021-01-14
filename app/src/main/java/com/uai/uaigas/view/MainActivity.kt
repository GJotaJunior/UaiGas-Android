package com.uai.uaigas.view

import android.content.Context
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.uai.uaigas.R
import com.uai.uaigas.api.RetrofitClient
import com.uai.uaigas.model.Endereco
import com.uai.uaigas.model.Posto
import com.uai.uaigas.model.User
import com.uai.uaigas.service.AuthService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.util.*

class MainActivity : AppCompatActivity() {

    private var loggedIn = AuthService.loggedIn
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var adresses: List<Endereco>
    private lateinit var locale: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        loadUser()

        if (checkLocationPermission()) {
            getLocation()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        var signInItem = menu?.findItem(R.id.item_sign_in)
        var signUpItem = menu?.findItem(R.id.item_sign_up)
        var profileItem = menu?.findItem(R.id.item_profile)
        var signOutItem = menu?.findItem(R.id.item_sign_out)
        var fuelItem = menu?.findItem(R.id.item_fuel)
        var fuelTypeItem = menu?.findItem(R.id.item_fuel_type)
        var mapItem = menu?.findItem(R.id.item_map)
        var gasStationItem = menu?.findItem(R.id.item_gas_station_and_address)

        signInItem?.isVisible = !loggedIn()
        signUpItem?.isVisible = !loggedIn()
        profileItem?.isVisible = loggedIn()
        signOutItem?.isVisible = loggedIn()
        AuthService.user?.let {
            fuelItem?.isVisible = it.admin
            fuelTypeItem?.isVisible = it.admin
        }
        mapItem?.isVisible = loggedIn()
        gasStationItem?.isVisible = loggedIn()

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_sign_in -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.item_sign_up -> startActivity(Intent(this, RegisterActivity::class.java))
            R.id.item_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.item_fuel -> startActivity(Intent(this, FuelList::class.java))
            R.id.item_fuel_type -> startActivity(Intent(this, FuelTypeList::class.java))
            R.id.item_map -> {
                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra("adresses", adresses as Serializable)
                var gasStation: List<Posto> = listOf()
//                adresses?.forEach {
//                    it.posto?.let { it1 -> gasStation.toMutableList().add(Posto(it1.id, it1.descricao)) }
//                }
                intent.putExtra("gasStationList", gasStation as Serializable)
                intent.putExtra("location", locale)
                startActivity(intent)
            }
            R.id.item_gas_station_and_address -> {
                val intent = Intent(this, GasStationActivity::class.java)
                startActivity(intent)
            }
            R.id.item_sign_out -> signOut()
            else -> Toast.makeText(this, "${item.title} ainda não implementado", Toast.LENGTH_SHORT)
                .show()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun signOut() {
        clearUser()
        finish()
        startActivity(intent)
    }

    private fun loadUser() {
        val id = getSharedPreferences("USER", Context.MODE_PRIVATE).getString("id", null)
        id?.let {
            val email = getSharedPreferences("USER", Context.MODE_PRIVATE).getString("email", null)
            val nome = getSharedPreferences("USER", Context.MODE_PRIVATE).getString("nome", null)
            val admin =
                getSharedPreferences("USER", Context.MODE_PRIVATE).getBoolean("admin", false)
            val fotoUrl =
                getSharedPreferences("USER", Context.MODE_PRIVATE).getString("fotoUrl", null)

            AuthService.user = User(
                id = it.toLong(),
                nome = nome,
                email = email,
                admin = admin,
                fotoUrl = fotoUrl
            )
        }
    }

    private fun clearUser() {
        var sharedPref = getSharedPreferences("USER", Context.MODE_PRIVATE).edit()
        sharedPref.clear()
        sharedPref.commit()
        AuthService.user = null

    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    locale = LatLng(location.latitude, location.longitude)

                    getNomeCidade(
                        locale.latitude,
                        locale.longitude
                    )?.let {
                        RetrofitClient.instance.findGasStationByCidade(
                            it
                        )
                            .enqueue(object : Callback<List<Endereco>> {
                                override fun onFailure(call: Call<List<Endereco>>?, t: Throwable?) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Ocorreu um erro, tente novamente mais tarde!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                override fun onResponse(
                                    call: Call<List<Endereco>>?,
                                    response: Response<List<Endereco>>?
                                ) {
                                    if (response != null) {
                                        when {
                                            response.isSuccessful -> {
                                                adresses = response?.body()!!
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
                                }
                            })
                    }
                }
            }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        getLocation()
                    }
                }
                return
            }
        }

    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Permissão de localização")
                    .setMessage("Você necessita permitir acesso à localização para utilizar algumas funções")
                    .setPositiveButton(
                        "OK",
                        DialogInterface.OnClickListener { dialogInterface, i ->

                            ActivityCompat.requestPermissions(
                                this@MainActivity,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                100
                            )
                        })
                    .create()
                    .show()

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    100
                )
            }
            return false
        } else {
            return true
        }
    }

    fun getNomeCidade(lat: Double, lng: Double): String? {
        val retorno: String
        val gcd = Geocoder(this, Locale.getDefault())
        var addresses: List<Address>? = null
        addresses = gcd.getFromLocation(lat, lng, 1)
        retorno = if (addresses!!.size > 0) {
            addresses.get(0).getAddressLine(0).split(", ").get(2).split(" - ").get(0)
        } else {
            "não encontrado"
        }
        return retorno
    }
}
