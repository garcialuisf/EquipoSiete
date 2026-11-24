package com.example.dogapp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dogapp.R
import com.example.dogapp.databinding.ItemCitaBinding
import com.example.dogapp.model.Cita

class CitaViewHolder(private val binding: ItemCitaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(cita: Cita, onCitaClick: (Int) -> Unit) {
        binding.root.setOnClickListener { onCitaClick(cita.id) }
        binding.cita = cita
        binding.executePendingBindings()

        Glide.with(binding.ivMascota)
            .load(cita.imagenUrl.takeIf { it.isNotBlank() })
            .circleCrop()
            .placeholder(R.drawable.ic_cita_mascota_placeholder)
            .fallback(R.drawable.ic_cita_mascota_placeholder)
            .error(R.drawable.ic_cita_mascota_placeholder)
            .into(binding.ivMascota)
    }
}
