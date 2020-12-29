package com.uai.uaigas.service

import com.uai.uaigas.api.RetrofitClient
import com.uai.uaigas.model.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserService {

    private var user = AuthService.user

    fun updatePhotoUrl(url: String) {
        user?.let {
            it.fotoUrl = url
            updateUser(it)
        }
    }

    fun updateProfileInfo(email: String, name: String) {
        user?.let {
            it.nome = name
            it.email = email
            updateUser(it)
        }
    }

    private fun updateUser(user: UserModel) {
        RetrofitClient.instance.updateUser(user.id, user).enqueue(object : Callback<UserModel> {
            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                when {
                    response.isSuccessful -> AuthService.user = response.body()
                }
            }
        })
    }
}