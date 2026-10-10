package com.example.aidhopemobile.models

data class Beneficiary(
    val id: String,
    val firstName: String,
    val lastName: String,
    val contactNumber: String,
    val email: String = "",
    val address: String = "",
    val status: String
) {
    val fullName: String
        get() = "$firstName $lastName"
}