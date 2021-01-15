package com.uai.uaigas.model

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.uai.uaigas.R
import com.uai.uaigas.api.RetrofitClient
import com.uai.uaigas.view.EditFuelActivity
import com.uai.uaigas.view.GasStationActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostoAdapter : RecyclerView.Adapter<PostoHolder> {

    var data: ArrayList<Endereco>? = null
    var viewMaker: LayoutInflater? = null
    var context: Context?

    constructor(data: ArrayList<Endereco>?, context: Context?) {
        this.data = data
        viewMaker = LayoutInflater.from(context)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostoHolder {
        return PostoHolder(
            viewMaker!!.inflate(
                R.layout.item_posto,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data!!.size


    override fun onBindViewHolder(holder: PostoHolder, position: Int) {
        holder.descricao.text = data!![position].posto!!.descricao
        holder.endereco.text = data!![position].toString()
        holder.edit.setOnClickListener {
            val intent = Intent(it.context, GasStationActivity::class.java)
            intent.putExtra("id", (1+position))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            it.context?.startActivity(intent)
        }
    }
}
