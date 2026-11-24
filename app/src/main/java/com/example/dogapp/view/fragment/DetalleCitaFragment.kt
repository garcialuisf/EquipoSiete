package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentDetalleCitaBinding
import com.example.dogapp.viewmodel.CitaViewModel

class DetalleCitaFragment : Fragment() {

    private val citaViewModel: CitaViewModel by viewModels()

    private var _binding: FragmentDetalleCitaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detalle_cita, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.volver.setOnClickListener { volverAHome() }

        val citaId = arguments?.getInt("citaId") ?: 0
        if (citaId <= 0) {
            binding.toolbar.titulo = getString(R.string.detalle_cita_no_disponible)
            return
        }

        citaViewModel.citaSeleccionada.observe(viewLifecycleOwner) { cita ->
            val citaActual = cita?.takeIf { it.id == citaId }
            binding.toolbar.titulo = citaActual?.nombreMascota
                ?: getString(R.string.detalle_cita_no_disponible)
            mostrarImagenMascota(citaActual?.imagenUrl)
        }
        citaViewModel.cargarCita(citaId)
    }

    private fun mostrarImagenMascota(imagenUrl: String?) {
        val imagenMascota = binding.ivDetalleMascota
        binding.cardImagenMascota.isVisible = imagenUrl != null

        if (imagenUrl == null) {
            Glide.with(this).clear(imagenMascota)
            return
        }

        Glide.with(this)
            .load(imagenUrl.takeIf { it.isNotBlank() })
            .fitCenter()
            .placeholder(R.drawable.ic_cita_mascota_placeholder)
            .fallback(R.drawable.ic_cita_mascota_placeholder)
            .error(R.drawable.ic_cita_mascota_placeholder)
            .into(imagenMascota)
    }

    private fun volverAHome() {
        val navController = findNavController()
        if (navController.currentDestination?.id != R.id.detalleCitaFragment) return

        if (!navController.popBackStack(R.id.homeFragment, false)) {
            navController.navigate(R.id.action_detalleCitaFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        Glide.with(this).clear(binding.ivDetalleMascota)
        super.onDestroyView()
        _binding = null
    }
}
