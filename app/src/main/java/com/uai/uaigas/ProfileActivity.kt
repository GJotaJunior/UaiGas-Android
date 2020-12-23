package com.uai.uaigas

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout

class ProfileActivity : AppCompatActivity() {

    lateinit var name: TextInputLayout
    lateinit var email: TextInputLayout
    lateinit var userPhoto: ImageView
    private val pickImage = 100
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        name = findViewById(R.id.username)
        email = findViewById(R.id.email)
        userPhoto = findViewById(R.id.user_photo)

        var nameMock = "Alfredo Borges Santana"
        var emailMock = "alfredo.b.s@uaigas.com"
        var photoUrlMock =
            "https://image.freepik.com/free-vector/portrait-african-american-woman-profile-avatar-young-black-girl_102172-418.jpg"
        chargeProfileInfo(nameMock, emailMock, photoUrlMock)
        changeUserPhoto(photoUrlMock)
    }

    fun chargeProfileInfo(name: String, email: String, photoUrl: String) {
        this.name.editText?.setText(name)
        this.email.editText?.setText(email)
        changeUserPhoto(photoUrl)
    }

    fun updateProfileInfo(view: View) {
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
            imageUri?.let { changeUserPhoto(it) }
        }
    }

    private fun changeUserPhoto(uri: Uri) {
        Glide.with(this).load(uri).error(R.drawable.default_avatar).centerCrop().into(userPhoto)
    }

    private fun changeUserPhoto(path: String) {
        Glide.with(this).load(path).error(R.drawable.default_avatar).centerCrop().into(userPhoto)
    }
}