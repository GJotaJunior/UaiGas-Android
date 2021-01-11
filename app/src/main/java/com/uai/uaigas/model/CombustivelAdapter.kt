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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CombustivelAdapter : RecyclerView.Adapter<CombustivelHolder> {

    var data: ArrayList<Combustivel>? = null
    var viewMaker: LayoutInflater? = null
    var context: Context?

    constructor(data: ArrayList<Combustivel>?, context: Context?) {
        this.data = data
        viewMaker = LayoutInflater.from(context)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CombustivelHolder {
        return CombustivelHolder(
            viewMaker!!.inflate(
                R.layout.item_tipo_combustivel,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data!!.size

    override fun onBindViewHolder(holder: CombustivelHolder, position: Int) {
        holder.name!!.text = data!![position].nome
        holder.edit.setOnClickListener {
            val i = Intent(it.context, EditFuelActivity::class.java)
            i.putExtra("id", (1 + position).toString())
            i.putExtra("description", data!![position].nome)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            it.context?.startActivity(i)
        }
        holder.delete.setOnClickListener {
            RetrofitClient.instance.deleteFuel((1 + position).toLong()).enqueue(object :
                Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        context!!,
                        "Ocorreu um erro, tente novamente mais tarde!",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    when {
                        response.isSuccessful -> {
                            data!!.removeAt(position)
                            notifyDataSetChanged()
                            Toast.makeText(
                                context!!,
                                "Excluido com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> Toast.makeText(
                            context!!,
                            "Você não pode excluir um combustível que está sendo usado!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }
    }
}