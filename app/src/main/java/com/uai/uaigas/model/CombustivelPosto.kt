package com.uai.uaigas.model

import android.os.Parcel
import android.os.Parcelable

class CombustivelPosto() : Parcelable{
    var id: Long? = null
    var tipo: TipoCombustivel? = null
    var combustivel: Combustivel? = null
    var posto: Posto? = null
    var cotacao: Cotacao? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Long::class.java.classLoader) as? Long
        posto = parcel.readParcelable(Posto::class.java.classLoader)
        cotacao = parcel.readParcelable(Cotacao::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeParcelable(posto, flags)
        parcel.writeParcelable(cotacao, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CombustivelPosto> {
        override fun createFromParcel(parcel: Parcel): CombustivelPosto {
            return CombustivelPosto(parcel)
        }

        override fun newArray(size: Int): Array<CombustivelPosto?> {
            return arrayOfNulls(size)
        }
    }
}