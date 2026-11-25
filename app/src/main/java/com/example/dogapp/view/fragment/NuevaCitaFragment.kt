package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentNuevaCitaBinding
import com.example.dogapp.viewmodel.CitaViewModel
import com.example.dogapp.viewmodel.CitaViewModel.EstadoCreacion
import com.example.dogapp.viewmodel.NuevaCitaViewModel
import com.example.dogapp.viewmodel.RazaViewModel

class NuevaCitaFragment : Fragment() {

    private var _binding: FragmentNuevaCitaBinding? = null
    private val binding get() = _binding!!
    private val nuevaCitaViewModel: NuevaCitaViewModel by viewModels()
    private val citaViewModel: CitaViewModel by viewModels()
    private val razaViewModel: RazaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nueva_cita, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurarFormulario()
        configurarRazas()
        configurarToolbar()
        configurarGuardado()
    }

    private fun configurarFormulario() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.formulario = nuevaCitaViewModel
    }

    private fun configurarRazas() {
        binding.progresoRazas.isVisible = razaViewModel.razas.value == null
        razaViewModel.razas.observe(viewLifecycleOwner) { razas ->
            binding.progresoRazas.isVisible = false
            val campo = binding.raza
            campo.setAdapter(ArrayAdapter(requireContext(), R.layout.item_sugerencia_raza, razas))
            if (razas.isNotEmpty() && campo.hasFocus() && campo.enoughToFilter()) {
                val cursor = campo.selectionStart
                campo.setText(campo.text.toString())
                if (cursor >= 0) campo.setSelection(cursor)
            }
        }
    }

    private fun configurarToolbar() {
        binding.toolbar.volver.setOnClickListener {
            volverAlHome()
        }
    }

    private fun configurarGuardado() {
        val bloquearAtras = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, false) {}
        binding.guardarCita.setOnClickListener { guardarCita() }
        citaViewModel.estadoCreacion.observe(viewLifecycleOwner) { estado ->
            val guardando = estado == EstadoCreacion.GUARDANDO
            bloquearAtras.isEnabled = guardando
            binding.guardando = guardando || estado == EstadoCreacion.GUARDADA
            binding.progresoGuardado.isVisible = guardando
            binding.toolbar.volver.isEnabled = !guardando

            when (estado) {
                EstadoCreacion.GUARDADA -> volverAlHome()
                EstadoCreacion.ERROR -> {
                    mostrarMensaje(R.string.nueva_cita_error_guardado)
                    citaViewModel.consumirErrorCreacion()
                }
                else -> Unit
            }
        }
    }

    private fun guardarCita() {
        binding.raza.dismissDropDown()
        val sintomas = binding.sintomas
        if (!nuevaCitaViewModel.tieneSintomaSeleccionado(sintomas.selectedItemPosition)) {
            mostrarMensaje(R.string.nueva_cita_selecciona_sintoma)
            return
        }
        val sintoma = sintomas.selectedItem as? String ?: return
        nuevaCitaViewModel.prepararCita(sintoma)?.let(citaViewModel::guardarNuevaCita)
    }

    private fun mostrarMensaje(texto: Int) {
        Toast.makeText(requireContext(), texto, Toast.LENGTH_SHORT).show()
    }

    private fun volverAlHome() {
        val navController = findNavController()
        if (navController.currentDestination?.id == R.id.nuevaCitaFragment) {
            navController.popBackStack(R.id.homeFragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
