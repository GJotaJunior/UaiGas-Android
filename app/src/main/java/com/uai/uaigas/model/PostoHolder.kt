package com.uai.uaigas.model

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.uai.uaigas.R

class PostoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var descricao: TextView = itemView.findViewById(R.id.description)
    var edit: MaterialButton = itemView.findViewById(R.id.edit)
    var delete: MaterialButton = itemView.findViewById(R.id.delete)
    var endereco: TextView = itemView.findViewById(R.id.endereco)
}
