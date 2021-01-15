package com.uai.uaigas.model

import android.os.Parcel
import android.os.Parcelable

class CombustivelPosto(){
    var id: Long? = null
    var tipo: TipoCombustivel? = null
    var combustivel: Combustivel? = null
    var posto: Posto? = null
    var cotacao: Cotacao? = null
}