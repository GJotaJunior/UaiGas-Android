package com.uai.uaigas.model

import android.icu.util.Calendar

class Cotacao() {
    var id: Long? = null
    var preco: Double? = null
    var dataHora: Calendar? = null
    var combustivel: CombustivelPosto? = null
}