package com.uai.uaigas.model

class Endereco(var logradouro: String?) {
    var id: Long? = null
    var numero: Int? = null
    var complemento: String? = null
    var bairro: String? = null
    var cidade: String? = null
    var estado: String? = null
    var cep: String? = null
    var latitude: Float? = null
    var longitude: Float? = null
    var posto: Posto? = null

    constructor(id: Long?, logradouro: String, numero: Int, complemento: String, bairro: String, cidade: String, estado: String, cep: String, latitude: Float, longitude: Float, posto: Posto): this(logradouro) {
        this.id = id
        this.numero = numero
        this.complemento = complemento
        this.bairro = bairro
        this.cidade = cidade
        this.estado = estado
        this.cep = cep
        this.latitude = latitude
        this.longitude = longitude
        this.posto = posto
    }
}