package com.example.tasks.presentacion

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.databinding.ActivityNewBinding

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBinding.inflate( layoutInflater )

        setContentView( binding.root )

        binding.btnSave.setOnClickListener {
            val task_text = binding.inTask.text.toString()
            val i = Intent()

            i.putExtra("task_text", task_text )
            setResult( RESULT_OK, i )
            finish()
        }
    }
}