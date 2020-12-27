package com.uai.uaigas

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

class ProfileActivity : AppCompatActivity() {

    lateinit var name: TextInputLayout
    lateinit var email: TextInputLayout
    lateinit var userPhoto: ImageView
    lateinit private var storageRef: StorageReference
    private val pickImage = 100
    private var imageUri: Uri? = null

    //    Mocks
    private var idMock = 3
    private var nameMock = "Alfredo Borges Santana"
    private var emailMock = "alfredo.b.s@uaigas.com"
    private var photoUrlMock =
        "https://image.freepik.com/free-vector/portrait-african-american-woman-profile-avatar-young-black-girl_102172-418.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        name = findViewById(R.id.username)
        email = findViewById(R.id.email)
        userPhoto = findViewById(R.id.user_photo)

        storageRef = FirebaseStorage.getInstance().getReference("users/${idMock}")
        updatePhotoUrl("https://firebasestorage.googleapis.com/v0/b/${storageRef.bucket}/o/users%2F${idMock}?alt=media");

        chargeProfileInfo(nameMock, emailMock, photoUrlMock)
        chargeUserPhoto(photoUrlMock)
    }

    fun updateProfileInfo(view: View) {
//        TODO atualizar no backend
        val password = findViewById<TextInputLayout>(R.id.password).editText?.text

        val validator = !password.toString().isNullOrEmpty()

        var message =
            if (validator) "Alterado com sucesso!"
            else "Senha incorreta!"

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
                    photoUrlMock =
                        "https://firebasestorage.googleapis.com/v0/b/${storageRef.bucket}/o/users%2F${idMock}?alt=media)"
                    chargeUserPhoto(it)
                }.addOnFailureListener {
                    Log.d("Erro!", it.toString())
                    Toast.makeText(this, "Erro! Tente novamente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun chargeProfileInfo(name: String, email: String, photoUrl: String) {
        this.name.editText?.setText(name)
        this.email.editText?.setText(email)
        chargeUserPhoto(photoUrl)
    }

    private fun chargeUserPhoto(uri: Uri) {
        Glide.with(this).load(uri).error(R.drawable.default_avatar).centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(userPhoto)
    }

    private fun chargeUserPhoto(path: String) {
        Glide.with(this).load(path).error(R.drawable.default_avatar).centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(userPhoto)
    }

    private fun updatePhotoUrl(url: String) {
        photoUrlMock = url
//        TODO Atualizar no backend
    }
}