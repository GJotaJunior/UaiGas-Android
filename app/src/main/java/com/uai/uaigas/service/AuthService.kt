package com.uai.uaigas.service

import com.uai.uaigas.model.User

object AuthService {
    var user: User? = null
    var loggedIn: () -> Boolean = { user != null }
}