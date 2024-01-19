package com.example.tasks.almacenamiento

import com.example.tasks.model.Task

interface Almacenamiento {
    fun save_task( task : Task, type: Int )
    fun get_tasks( type : Int ) : MutableList<Task>
    fun completeTask( task: Task )
}