package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
            binding.toolbar.titulo = cita?.takeIf { it.id == citaId }?.nombreMascota
                ?: getString(R.string.detalle_cita_no_disponible)
        }
        citaViewModel.cargarCita(citaId)
    }

    private fun volverAHome() {
        val navController = findNavController()
        if (navController.currentDestination?.id != R.id.detalleCitaFragment) return

        if (!navController.popBackStack(R.id.homeFragment, false)) {
            navController.navigate(R.id.action_detalleCitaFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
