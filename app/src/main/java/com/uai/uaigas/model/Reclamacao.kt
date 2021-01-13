package com.uai.uaigas.model

import android.icu.util.Calendar
import android.os.Parcel
import android.os.Parcelable
import com.uai.uaigas.enums.ReclamacaoStatus

class Reclamacao() : Parcelable {
    var id: Long? = null
    var descricao: String? = null
    var dataHora: Calendar? = null
    var reclamacaoStatus: ReclamacaoStatus? = null

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

    companion object CREATOR : Parcelable.Creator<Reclamacao> {
        override fun createFromParcel(parcel: Parcel): Reclamacao {
            return Reclamacao(parcel)
        }

        override fun newArray(size: Int): Array<Reclamacao?> {
            return arrayOfNulls(size)
        }
    }
}