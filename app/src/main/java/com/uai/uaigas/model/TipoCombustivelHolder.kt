package com.uai.uaigas.model

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.uai.uaigas.R

class TipoCombustivelHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var description: TextView? = itemView.findViewById(R.id.description)
    var edit: MaterialButton = itemView.findViewById(R.id.edit)
    var delete: MaterialButton = itemView.findViewById(R.id.delete)
}