package com.example.tasks.presentacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.Adapter
import com.example.tasks.R
import com.example.tasks.almacenamiento.Almacenamiento
import com.example.tasks.almacenamiento.AlmacenamientoGSON
import com.example.tasks.databinding.ActivityMainBinding
import com.example.tasks.model.Task

class MainActivity : AppCompatActivity() {

    private val linearLayoutManager = LinearLayoutManager( this )
    private lateinit var data_store : AlmacenamientoGSON

    companion object {
        const val CODE_NEW_TASK = 1;
    }

    private lateinit var binding : ActivityMainBinding
    private lateinit var task_adapter : Adapter
    private lateinit var tasks : MutableList<Task>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate( layoutInflater )

        setContentView(binding.root)

        setSupportActionBar( binding.toolbar )

        data_store = AlmacenamientoGSON( this@MainActivity )

        tasks = data_store.get_tasks( 0 )

        task_adapter = Adapter( this, tasks, onChecked = {
            id -> delete_task( id )
        } );

        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = task_adapter
        }

    }

    private fun delete_task( i: Int ) {
        tasks.removeAt( i )
        task_adapter.notifyItemRemoved( i )
    }
            
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when( item.itemId ) {
            R.id.menu_new_task -> {
                val intent = Intent( this@MainActivity, AddTaskActivity::class.java )
                startActivityForResult( intent, CODE_NEW_TASK )
            }
            R.id.menu_my_info -> {
                data_store.get_info_user()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( requestCode == CODE_NEW_TASK && resultCode == RESULT_OK ) {

            val content = data!!.extras!!.getString("task_text", "")
            val task = Task( content, false, 1 )

            data_store.save_task( task, 0 )
            tasks.add( 0, task )
            task_adapter.notifyItemInserted( 0)

        }
    }
}