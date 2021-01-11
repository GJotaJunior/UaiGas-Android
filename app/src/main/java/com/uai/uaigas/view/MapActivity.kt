package com.uai.uaigas.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.uai.uaigas.R
import java.util.*
import kotlin.collections.ArrayList


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var locale: LatLng = LatLng(0.0, 0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (checkLocationPermission()) {
            getLocation()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.addMarker(MarkerOptions().position(locale!!).title("Você"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(locale, 15.0f))
    }

    private fun getLatLng(location: String?): LatLng {
        var retorno = LatLng(0.0, 0.0)
        if (Geocoder.isPresent()) {
            val gc = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> = gc.getFromLocationName(location, 5)
            if (addresses.isEmpty()) return retorno
            val ll: MutableList<LatLng> = ArrayList(addresses.size)
            for (a in addresses) {
                if (a.hasLatitude() && a.hasLongitude()) {
                    ll.add(LatLng(a.latitude, a.longitude))
                }
            }
            if (ll.size != 0) retorno = ll[0]
        }
        return retorno
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location!=null){
                    locale = LatLng(location.latitude, location.longitude)
                }
            }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                    .setTitle("Permissão de localização")
                    .setMessage("Você necessita permitir acesso à localização para utilizar algumas funções")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->

                        ActivityCompat.requestPermissions(this@MapActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            100)
                    })
                    .create()
                    .show()

            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    100)
            }
            return false
        } else {
            return true
        }
    }
}