package com.uai.uaigas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private var loggedIn = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val mainMenu: Int =
            if (loggedIn) R.menu.main_menu_connected
            else R.menu.main_menu_disconnected

        menuInflater.inflate(mainMenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_sign_in -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.item_sign_on -> startActivity(Intent(this, RegisterActivity::class.java))
            R.id.item_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.item_sign_out -> Toast.makeText(this, "TODO logout", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }
}