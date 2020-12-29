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
import com.uai.uaigas.service.AuthService
import com.uai.uaigas.service.UserService

class ProfileActivity : AppCompatActivity() {

    lateinit var name: TextInputLayout
    lateinit var email: TextInputLayout
    lateinit var userPhoto: ImageView
    private lateinit var storageRef: StorageReference
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var user = AuthService.user

    // MOCK
    private val passwordMock = "123456"

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
        val validatorPassword = !password.toString().contentEquals(passwordMock)

        var message: String

        when {
            validatorEmpty ->
                message = "Todos os campos precisam estar preenchidos!"
            validatorPassword -> message = "Informe a senha correta"
            else -> {
                UserService.updateProfileInfo(
                    email.editText?.text.toString(),
                    name.editText?.text.toString()
                )
                message = "Alterado com sucesso!"
            }
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
        UserService.updatePhotoUrl(url)
    }
}