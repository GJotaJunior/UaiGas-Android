package com.uai.uaigas.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.uai.uaigas.R
import com.uai.uaigas.api.RetrofitClient
import com.uai.uaigas.model.User
import com.uai.uaigas.service.AuthService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    lateinit var name: TextInputLayout
    lateinit var email: TextInputLayout
    lateinit var userPhoto: ImageView
    private lateinit var storageRef: StorageReference
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var user = AuthService.user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        name = findViewById(R.id.username)
        email = findViewById(R.id.email)
        userPhoto = findViewById(R.id.user_photo)

        storageRef = FirebaseStorage.getInstance().getReference("users/${user?.id}")

        chargeProfileInfo()
        chargeUserPhoto()
    }

    fun updateProfileInfo(view: View) {
        val password = findViewById<TextInputLayout>(R.id.password).editText?.text

        val validatorEmpty = password.toString().isNullOrEmpty() || name.editText?.text.toString()
            .isNullOrEmpty() || email.editText?.text.toString().isNullOrEmpty()

        when {
            validatorEmpty ->
                Toast.makeText(
                    applicationContext,
                    "Todos os campos precisam estar preenchidos!",
                    Toast.LENGTH_SHORT
                ).show();
            else -> {
                val user = User(
                    nome = name.editText?.text.toString(),
                    email = email.editText?.text.toString(),
                    senha = password.toString()
                )
                updateUser(user)
            }
        }
    }

    fun updateProfilePhoto(view: View) {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageUri?.let {
                storageRef.putFile(it).addOnSuccessListener { taskSnapshot ->
                    Toast.makeText(this, "Foto alterada!", Toast.LENGTH_SHORT).show()
                    updatePhotoUrl("https://firebasestorage.googleapis.com/v0/b/${storageRef.bucket}/o/users%2F${user?.id}?alt=media")
                    chargeUserPhoto(it)
                }.addOnFailureListener {
                    Log.d("Erro!", it.toString())
                    Toast.makeText(this, "Erro! Tente novamente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun chargeProfileInfo() {
        this.name.editText?.setText(user?.nome)
        this.email.editText?.setText(user?.email)
        chargeUserPhoto()
    }

    private fun chargeUserPhoto(uri: Uri) {
        Glide.with(this).load(uri).error(R.drawable.default_avatar).centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(userPhoto)
    }

    private fun chargeUserPhoto() {
        Glide.with(this).load(user?.fotoUrl).error(R.drawable.default_avatar).centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(userPhoto)
    }

    private fun updatePhotoUrl(url: String) {
        AuthService.user?.let {
            it.fotoUrl = url
            updateUser(it)
        }
    }

    private fun updateUser(user: User) {
        RetrofitClient.instance.updateUser(user).enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(applicationContext, "Ocorreu um erro, tente novamente mais tarde!", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                when {
                    response.isSuccessful -> {
                        response.body()?.let {
                            AuthService.user = it
                            Toast.makeText(
                                applicationContext,
                                "Perfil atualizado com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    response.code() == 400 -> {
                        response.errorBody()?.let {
                            var resp = it.string()
                            if (resp.contains("message")) {
                                resp = JSONObject(resp).getString("message")
                            }
                            Toast.makeText(applicationContext, resp, Toast.LENGTH_SHORT).show()
                        }
                    }
                    else -> Toast.makeText(
                        applicationContext,
                        "Ocorreu um erro, tente novamente!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}