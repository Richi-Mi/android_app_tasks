package com.example.tasks.presentacion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response.Listener
import com.example.tasks.R
import com.example.tasks.almacenamiento.PREFERENCES
import com.example.tasks.almacenamiento.TOKEN
import com.example.tasks.almacenamiento.USER_TASKS
import com.example.tasks.connection.Peticiones
import com.example.tasks.databinding.ActivityLoginBinding
import com.example.tasks.model.User
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), Listener<JSONObject> {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        var ID = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate( layoutInflater )
        setContentView( binding.root )

        val peticiones = Peticiones( this, this )

        // Spinner configuration
        val adapter_spinner = ArrayAdapter<String>(
            this@LoginActivity,
            android.R.layout.simple_spinner_item,
            resources.getStringArray( R.array.type_login )
        )
        adapter_spinner.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item )
        binding.spinnerCreate.apply {
            adapter = adapter_spinner
            setSelection( 0 )
        }
        // Click en el boton
        binding.btnLogin.setOnClickListener {
            val user = User( binding.inUsername.text.toString(), binding.inPassword.text.toString() )
            ID = if( binding.spinnerCreate.selectedItem.toString() == "Login" ) {
                peticiones.iniciar_sesion( user )
                1
            } else {
                peticiones.create_user( user )
                2
            }
        }
    }

    override fun onResponse(response: JSONObject?) {
        when( ID ) {
            1 -> {
                val token = response!!.get("token")
                val msg = response.get("message")

                val preferences = this.getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE )
                val editor = preferences.edit()

                val alert = AlertDialog.Builder( this )
                    .setTitle("You have loged")
                    .setMessage( msg.toString() )
                    .setPositiveButton("Got it") { dialog, which ->
                        val intent = Intent( this@LoginActivity, MainActivity::class.java )
                        startActivity( intent )
                    }

                editor.putString( TOKEN, token.toString() )
                editor.putString( USER_TASKS, binding.inUsername.text.toString() )
                editor.apply()

                alert.show()
            }
            2 -> {
                val msg = response!!.get("message")
                val alert = AlertDialog.Builder( this )
                    .setTitle("You have created your user")
                    .setMessage( msg.toString() )
                    .setPositiveButton("Ok got it", null )
                alert.show()
            }
        }
    }
}