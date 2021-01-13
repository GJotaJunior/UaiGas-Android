package com.uai.uaigas.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.uai.uaigas.R
import com.uai.uaigas.model.User
import com.uai.uaigas.service.AuthService

class MainActivity : AppCompatActivity() {

    private var loggedIn = AuthService.loggedIn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        loadUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        var signInItem = menu?.findItem(R.id.item_sign_in)
        var signUpItem = menu?.findItem(R.id.item_sign_up)
        var profileItem = menu?.findItem(R.id.item_profile)
        var signOutItem = menu?.findItem(R.id.item_sign_out)
        var fuelItem = menu?.findItem(R.id.item_fuel)
        var fuelTypeItem = menu?.findItem(R.id.item_fuel_type)
        var mapItem = menu?.findItem(R.id.item_map)

        signInItem?.isVisible = !loggedIn()
        signUpItem?.isVisible = !loggedIn()
        profileItem?.isVisible = loggedIn()
        signOutItem?.isVisible = loggedIn()
        AuthService.user?.let {
            fuelItem?.isVisible = it.admin
            fuelTypeItem?.isVisible = it.admin
        }
        mapItem?.isVisible = loggedIn()

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_sign_in -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.item_sign_up -> startActivity(Intent(this, RegisterActivity::class.java))
            R.id.item_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.item_fuel -> startActivity(Intent(this, FuelList::class.java))
            R.id.item_fuel_type -> startActivity(Intent(this, FuelTypeList::class.java))
            R.id.item_map -> startActivity(Intent(this, MapActivity::class.java))
            R.id.item_sign_out -> signOut()
            else -> Toast.makeText(this, "${item.title} ainda não implementado", Toast.LENGTH_SHORT)
                .show()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun signOut() {
        clearUser()
        finish()
        startActivity(intent)
    }

    private fun loadUser() {
        val id = getSharedPreferences("USER", Context.MODE_PRIVATE).getString("id", null)
        id?.let {
            val email = getSharedPreferences("USER", Context.MODE_PRIVATE).getString("email", null)
            val nome = getSharedPreferences("USER", Context.MODE_PRIVATE).getString("nome", null)
            val admin = getSharedPreferences("USER", Context.MODE_PRIVATE).getBoolean("admin", false)
            val fotoUrl = getSharedPreferences("USER", Context.MODE_PRIVATE).getString("fotoUrl", null)

            AuthService.user = User(
                id = it.toLong(),
                nome = nome,
                email = email,
                admin = admin,
                fotoUrl = fotoUrl
            )
        }
    }

    private fun clearUser() {
        var sharedPref = getSharedPreferences("USER", Context.MODE_PRIVATE).edit()
        sharedPref.clear()
        sharedPref.commit()
        AuthService.user = null
    }
}