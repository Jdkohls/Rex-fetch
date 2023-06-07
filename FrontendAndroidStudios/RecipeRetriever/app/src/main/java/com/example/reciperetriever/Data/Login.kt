package com.example.reciperetriever.Data
import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentials(val username: String, val password: String)

@Serializable
data class NewUserCredentials(val username: String, val password: String, val new_username: String)