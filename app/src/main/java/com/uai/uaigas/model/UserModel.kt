package com.uai.uaigas.model

class UserModel(
    var nome: String? = null,
    var email: String? = null,
    var senha: String?,
    var fotoUrl: String? = null,
    var admin: Boolean = false,
    var id: Long = 0
) {
    override fun toString(): String {
        return "Nome: $nome, email: $email, senha: $senha"
    }
}