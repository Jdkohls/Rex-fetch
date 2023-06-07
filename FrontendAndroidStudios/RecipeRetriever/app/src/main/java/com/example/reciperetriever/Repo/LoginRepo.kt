package com.example.reciperetriever.Repo

import android.util.Log
import com.example.reciperetriever.Data.*
import com.example.reciperetriever.JWT
import kotlinx.serialization.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlinx.serialization.json.Json
import java.io.OutputStream
import java.net.HttpURLConnection

//this may change based on our server setup
const val base_url = "http://127.0.0.1:8080/"

class LoginRepo {
    fun send_login_request(username: String, password: String): JWT {
        val url = base_url+"clients/login"
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Accept", "application/json")
        connection.doOutput = true
        val credentials = LoginCredentials(username, password)
        val json = Json.encodeToString(credentials)

        try {
            val outputStream: OutputStream = connection.outputStream
            outputStream.write(json.toByteArray())
            val response = StringBuffer()
            BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                var inputLine: String? = reader.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = reader.readLine()
                }
            }

            if (connection.responseCode == HttpsURLConnection.HTTP_OK) {
                return Json.decodeFromString(response.toString())
            } else {
                throw Exception("Failed to POST HTTP ${connection.responseCode}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }
        return Json.decodeFromString("")
    }

    fun send_create_user_request(username: String, password: String): JWT {
        val url = base_url+ "/clients" //POST here creates a new client/user
        with(URL(url).openConnection() as HttpsURLConnection) {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
            outputStream.write(Json.encodeToString(LoginCredentials(username, password)).toByteArray())
            //^create the json to send to backend--username and password

            val response = StringBuffer()
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
            }
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                //this will return whatever Json struct the backend returns.
                //Assuming for now it will be something like {"jwt":"we're screwed"}
                return Json.decodeFromString(response.toString())
            }
            throw Exception("Failed to POST HTTP $responseCode")
        }
    }

    fun send_update_user_request(username: String, password: String, new_username: String): JWT {
        val url = base_url+"clients"
        with(URL(url).openConnection() as HttpsURLConnection) {
            requestMethod = "PUT" //updates a user
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
            outputStream.write(Json.encodeToString(NewUserCredentials(username, password, new_username)).toByteArray())
            //send update user data to backend

            val response = StringBuffer()
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
            }
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                //this will return whatever Json struct the backend returns.
                //Assuming for now it will be something like {"jwt":"we're screwed"}
                return Json.decodeFromString(response.toString())
            }
            throw Exception("Failed to PUT HTTP $responseCode")
        }
    }
}
