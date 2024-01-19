package com.example.tasks.connection

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tasks.model.User
import com.example.tasks.presentacion.MainActivity
import org.json.JSONObject

private const val URL = "http://192.168.1.71:8080"

class Peticiones( val context: Activity, val listener: Listener<JSONObject> ) : ErrorListener {

    private val cola = Volley.newRequestQueue( context )

    fun iniciar_sesion( usr : User ) {
        val route = "$URL/api/user/login"

        val jsonBody = JSONObject()
        jsonBody.put("username", usr.username )
        jsonBody.put("password", usr.password )

        val peticion = JsonObjectRequest(
            Request.Method.POST, // Metodo de la peticiòn
            route, // URL de la peticiòn.
            jsonBody, // Body de la peticiòn
            listener, //
            this
            )
        cola.add( peticion )
    }

    fun create_user( usr: User ) {
        val route = "$URL/api/user/create"
        val jsonBody = JSONObject()
        jsonBody.put("username", usr.username )
        jsonBody.put("password", usr.password )

        val peticion = JsonObjectRequest(
            Request.Method.POST, // Metodo de la peticiòn
            route, // URL de la peticiòn.
            jsonBody,// Body de la peticiòn
            listener, // Listener de la peticiòn
            this
        )
        cola.add( peticion )
    }
    fun get_info_user( user: String, token : String ) {
        val route = "$URL/api/user/info/$user"
        val peticion = object : JsonObjectRequest(
            Request.Method.GET,
            route,
            null,
            listener,
            this
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val parametros = hashMapOf<String, String>()
                parametros.put("x-token", token)
                return parametros
            }
        }
        Log.d("PETICION", "Cree la peticion" )

        cola.add( peticion )
    }

    override fun onErrorResponse(error: VolleyError?) {
        val alert = AlertDialog.Builder( context )
            .setTitle("Error in your information")
            .setMessage( error!!.message.toString() )
            .setNegativeButton("Ok", null )

        alert.show()
    }

}