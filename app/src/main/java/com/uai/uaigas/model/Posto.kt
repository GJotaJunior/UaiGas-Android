package com.uai.uaigas.model

import com.uai.uaigas.enums.PostoStatus


data class Posto(var id: Long? = null,
                 var descricao: String? = "",
                 var status: PostoStatus? = null,
                 var reclamacoes: List<Reclamacao>? = null,
                 var combustiveis: List<CombustivelPosto>? = null,
                 var endereco: Endereco? = null)


