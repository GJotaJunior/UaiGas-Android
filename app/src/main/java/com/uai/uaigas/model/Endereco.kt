package com.uai.uaigas.model

import android.os.Parcel
import android.os.Parcelable

class Endereco() : Parcelable{
    var id: Long? = null
    var logradouro: String? = null
    var numero: Int? = null
    var complemento: String? = null
    var bairro: String? = null
    var cidade: String? = null
    var estado: String? = null
    var cep: String? = null
    var latitude: Float? = null
    var longitude: Float? = null
    var posto: Posto? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Long::class.java.classLoader) as? Long
        logradouro = parcel.readString()
        numero = parcel.readValue(Int::class.java.classLoader) as? Int
        complemento = parcel.readString()
        bairro = parcel.readString()
        cidade = parcel.readString()
        estado = parcel.readString()
        cep = parcel.readString()
        latitude = parcel.readValue(Float::class.java.classLoader) as? Float
        longitude = parcel.readValue(Float::class.java.classLoader) as? Float
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(logradouro)
        parcel.writeValue(numero)
        parcel.writeString(complemento)
        parcel.writeString(bairro)
        parcel.writeString(cidade)
        parcel.writeString(estado)
        parcel.writeString(cep)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Endereco> {
        override fun createFromParcel(parcel: Parcel): Endereco {
            return Endereco(parcel)
        }

        override fun newArray(size: Int): Array<Endereco?> {
            return arrayOfNulls(size)
        }
    }
}