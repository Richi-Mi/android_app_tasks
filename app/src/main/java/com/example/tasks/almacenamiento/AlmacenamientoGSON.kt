package com.example.tasks.almacenamiento

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.example.tasks.connection.Peticiones
import com.example.tasks.model.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


private const val KEY_TAKS     = "tasks"
private const val KEY_COMPLETE = "complete_tasks"
const val PREFERENCES  = "data_tasks"
const val TOKEN = "token"
const val USER_TASKS = "user"

class AlmacenamientoGSON(val context: Activity) : Almacenamiento, Response.Listener<JSONObject> {

    // USO DE ID`s por peticiòn.
    private var ID_PETICIONEs = 1
    // Uso de GSON
    private val gson = Gson()
    private val type_token = object : TypeToken<MutableList<Task>>() {}.type
    private var requests : Peticiones = Peticiones( context, this@AlmacenamientoGSON )

    override fun save_task( task: Task, type: Int ) {
        // Añadiendo la nueva tarea a una lista con las anteriores tareas.
        val new_tasks = get_tasks( type )
        new_tasks.add( 0, task )

        // Uso de preferencias
        val preferences = context.getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE )
        val editor = preferences.edit()

        // Parseo a JSON.
        val str = gson.toJson( new_tasks, type_token )
        when( type ) {
            0 -> editor.putString( KEY_TAKS, str )
            1 -> editor.putString( KEY_COMPLETE, str )
        }
        editor.apply()
    }

    override fun get_tasks( type: Int ): MutableList<Task> {
        val preferences = context.getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE )
        val str = when( type ) {
            0 -> preferences.getString( KEY_TAKS, "" ).toString()
            1 -> preferences.getString( KEY_COMPLETE, "").toString()
            else -> preferences.getString( KEY_TAKS, "").toString()
        }

        if(str == "")
            return mutableListOf<Task>()

        return gson.fromJson( str, type_token )
    }

    override fun completeTask(task: Task) {
        task.checked = true
        save_task( task, 1 )
    }

    fun get_info_user() {
        val preferences = context.getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE )
        val usr = preferences.getString( USER_TASKS, "")
        val token = preferences.getString( TOKEN, "")

        ID_PETICIONEs = 1
        requests.get_info_user( usr!!, token!! )
    }

    override fun onResponse(response: JSONObject?) {
        when( ID_PETICIONEs ) {
            1 -> {
                val user = response!!.get("username").toString()
                val pass = response.get("password").toString()
                val alert = AlertDialog.Builder( context )
                    .setTitle("This is your information")
                    .setMessage("Hello $user \nThis is your encrypted password: \n $pass")
                    .setNegativeButton("Ok", null )
                alert.show()
            }
        }
    }
}