package com.uai.uaigas.model

import android.os.Parcel
import android.os.Parcelable
import com.uai.uaigas.enums.PostoStatus


class Posto() : Parcelable {
    var id: Long? = null
    var descricao: String? = null
    var status: PostoStatus? = null
    var reclamacoes: List<Reclamacao>? = null
    var combustiveis: List<CombustivelPosto>? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Long::class.java.classLoader) as? Long
        descricao = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(descricao)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Posto> {
        override fun createFromParcel(parcel: Parcel): Posto {
            return Posto(parcel)
        }

        override fun newArray(size: Int): Array<Posto?> {
            return arrayOfNulls(size)
        }
    }
}