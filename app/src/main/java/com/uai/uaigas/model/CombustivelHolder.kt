package com.uai.uaigas.model

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.uai.uaigas.R

class CombustivelHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView? = itemView.findViewById(R.id.description)
    var edit: MaterialButton = itemView.findViewById(R.id.edit)
    var delete: MaterialButton = itemView.findViewById(R.id.delete)
}