package com.uai.uaigas.model

class UserModel(var email: String, var senha: String?) {
    var id: Long = 0
    var nome: String? = null
    var fotoUrl: String? = null
    var admin: Boolean = false
}