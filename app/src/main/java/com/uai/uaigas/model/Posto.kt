package com.uai.uaigas.model

import com.uai.uaigas.enums.PostoStatus


data class Posto(var descricao: String?) {
    var id: Long? = null
    var status: PostoStatus? = null
    var reclamacoes: List<Reclamacao>? = null
    var combustiveis: List<CombustivelPosto>? = null
    var endereco: Endereco? = null

    constructor(id: Long?, descricao: String, status: PostoStatus): this(descricao){
        this.id = id
        this.status = status
    }

    constructor(id: Long,  descricao: String, status: PostoStatus, reclamacao: List<Reclamacao>, combustivel: List<Combustivel>) : this(descricao){
        this.id = id
        this.status = status
        this.reclamacoes = reclamacoes
        this.combustiveis = combustiveis
    }
}