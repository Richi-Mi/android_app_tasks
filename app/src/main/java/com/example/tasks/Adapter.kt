package com.example.tasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.databinding.TaskBinding
import com.example.tasks.model.Task

class Adapter( val context: Context, val tareas: MutableList<Task>, val onChecked: (Int) -> Unit ) : RecyclerView.Adapter<Adapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = TaskBinding.bind( itemView )

        fun personaliza( task : Task, onChecked: (Int) -> Unit ) {
            binding.note.text = task.content

            binding.checkbox.isChecked = false
            binding.checkbox.setOnClickListener { onChecked( adapterPosition ) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from( context ).inflate( R.layout.task, parent, false )
        return TaskViewHolder( view )
    }

    override fun getItemCount(): Int {
        return tareas.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.personaliza( tareas[position], onChecked )
    }
}