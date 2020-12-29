package com.uai.uaigas.service

import com.uai.uaigas.model.UserModel

object AuthService {
    var user: UserModel? = null
    var loggedIn: () -> Boolean = { user != null }
}