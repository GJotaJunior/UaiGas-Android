package com.uai.uaigas.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.uai.uaigas.R
import com.uai.uaigas.helper.BitmapHelper
import com.uai.uaigas.model.Endereco
import com.uai.uaigas.model.Posto


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var gasStationList: List<Posto>
    private lateinit var adresses: List<Endereco>
    private val gasIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, R.color.primary)
        BitmapHelper.vectorToBitmap(this, R.drawable.ic_gas, color)
    }
    private lateinit var locale: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locale = intent.getParcelableExtra<LatLng>("location")!!
//        gasStationList = intent.getSerializableExtra("gasStationList") as List<Posto>
        adresses = intent.getSerializableExtra("adresses") as List<Endereco>
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        adresses?.forEach {
            map.addMarker(
                it.latitude?.toDouble()?.let { it1 ->
                    it.longitude?.toDouble()?.let { it2 ->
                        LatLng(it1,
                            it2
                        )
                    }
                }?.let { it2 ->
                    MarkerOptions().position(it2)
                        .title(
                            it.posto?.descricao
                        ).icon(gasIcon)
                }
            )
        }

        map.addMarker(MarkerOptions().position(locale).title("Você"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(locale, 14.0f))
    }
}