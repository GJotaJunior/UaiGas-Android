package com.uai.uaigas.model

import android.icu.util.Calendar
import android.os.Parcel
import android.os.Parcelable

class Cotacao() : Parcelable {
    var id: Long? = null
    var preco: Double? = null
    var dataHora: Calendar? = null
    var combustivel: Combustivel? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Long::class.java.classLoader) as? Long
        preco = parcel.readValue(Double::class.java.classLoader) as? Double
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeValue(preco)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cotacao> {
        override fun createFromParcel(parcel: Parcel): Cotacao {
            return Cotacao(parcel)
        }

        override fun newArray(size: Int): Array<Cotacao?> {
            return arrayOfNulls(size)
        }
    }
}