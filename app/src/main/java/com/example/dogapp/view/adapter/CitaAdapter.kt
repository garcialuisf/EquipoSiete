package com.example.dogapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.dogapp.databinding.ItemCitaBinding
import com.example.dogapp.model.Cita
import com.example.dogapp.view.viewholder.CitaViewHolder

class CitaAdapter(private val onCitaClick: (Int) -> Unit) :
    ListAdapter<Cita, CitaViewHolder>(CitaDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val binding = ItemCitaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CitaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        holder.bind(getItem(position), onCitaClick)
    }

    private object CitaDiffCallback : DiffUtil.ItemCallback<Cita>() {
        override fun areItemsTheSame(oldItem: Cita, newItem: Cita): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Cita, newItem: Cita): Boolean =
            oldItem == newItem
    }
}
