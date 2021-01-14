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

class PostoAdapter : RecyclerView.Adapter<PostoHolder> {

    var data: ArrayList<Posto>? = null
    var viewMaker: LayoutInflater? = null
    var context: Context?

    constructor(data: ArrayList<Posto>?, context: Context?) {
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
        holder.descricao.text = data!![position].descricao
        holder.endereco.text = data!![position].endereco.toString()
        holder.edit.setOnClickListener {
//            val i = Intent(it.context, EditGasStationActivity::class.java)
//            i.putExtra("id", (1 + position).toString())
//            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            it.context?.startActivity(i)
        }
        holder.delete.setOnClickListener {
            RetrofitClient.instance.deleteGasStation((1 + position).toLong()).enqueue(object :
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
