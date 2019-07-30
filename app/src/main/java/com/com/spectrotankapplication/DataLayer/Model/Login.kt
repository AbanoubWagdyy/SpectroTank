package com.spectrotank.DataLayer.Model

data class Login(
    var username: String = "",
    var password: String = ""
) {
    override fun toString(): String {
        return "{username='$username', password='$password'}"
    }
}