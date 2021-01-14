package com.uai.uaigas.model

class Endereco(
    var logradouro: String? = null,
    var id: Long? = null,
    var numero: Int? = null,
    var complemento: String? = null,
    var bairro: String? = null,
    var cidade: String? = null,
    var estado: String? = null,
    var cep: String? = null,
    var latitude: Float? = null,
    var longitude: Float? = null,
    var posto: Posto? = null,
)
