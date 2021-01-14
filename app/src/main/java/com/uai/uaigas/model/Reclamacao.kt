package com.uai.uaigas.model

import android.icu.util.Calendar
import android.os.Parcel
import android.os.Parcelable
import com.uai.uaigas.enums.ReclamacaoStatus

class Reclamacao() {
    var id: Long? = null
    var descricao: String? = null
    var dataHora: Calendar? = null
    var reclamacaoStatus: ReclamacaoStatus? = null
}