package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentEditarCitaBinding
import com.example.dogapp.model.Cita
import com.example.dogapp.viewmodel.CitaViewModel
import com.example.dogapp.viewmodel.CitaViewModel.EstadoEdicion
import com.example.dogapp.viewmodel.RazaViewModel

class EditarCitaFragment : Fragment() {

    private var _binding: FragmentEditarCitaBinding? = null
    private val binding get() = _binding!!
    private val citaViewModel: CitaViewModel by viewModels()
    private val razaViewModel: RazaViewModel by viewModels()
    private var formularioCargado = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_editar_cita, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        formularioCargado = savedInstanceState?.getBoolean(FORMULARIO_CARGADO) ?: false
        val citaId = requireArguments().getInt(CITA_ID)
        configurarFormulario(citaId)
        observarDatos()
        citaViewModel.cargarCitaParaEditar(citaId)
    }

    private fun configurarFormulario(citaId: Int) {
        binding.toolbar.volver.setOnClickListener {
            val navController = findNavController()
            if (!navController.popBackStack(R.id.detalleCitaFragment, false)) {
                navController.navigate(
                    R.id.action_editarCitaFragment_to_detalleCitaFragment,
                    Bundle().apply { putInt(CITA_ID, citaId) },
                )
            }
        }

        for (campo in listOf(binding.nombre, binding.raza, binding.propietario, binding.telefono)) {
            campo.doAfterTextChanged { actualizarBoton() }
        }

        binding.editar.setOnClickListener {
            binding.raza.dismissDropDown()
            citaViewModel.editarCita(
                binding.nombre.text.toString(),
                binding.raza.text.toString(),
                binding.propietario.text.toString(),
                binding.telefono.text.toString(),
            )
        }
    }

    private fun observarDatos() {
        binding.progresoRazas.isVisible = razaViewModel.razas.value == null
        razaViewModel.razas.observe(viewLifecycleOwner) { razas ->
            binding.progresoRazas.isVisible = false
            binding.raza.setAdapter(ArrayAdapter(requireContext(), R.layout.item_sugerencia_raza, razas))
            if (razas.isNotEmpty() && binding.raza.hasFocus() && binding.raza.enoughToFilter()) {
                val texto = binding.raza.text
                val cursor = binding.raza.selectionStart
                binding.raza.setText(texto)
                if (cursor >= 0) binding.raza.setSelection(cursor)
            }
        }

        citaViewModel.citaSeleccionada.observe(viewLifecycleOwner) { cita ->
            if (cita != null && !formularioCargado) llenarFormulario(cita)
        }
        citaViewModel.estadoEdicion.observe(viewLifecycleOwner) { estado ->
            mostrarEstado(estado)
        }
    }

    private fun llenarFormulario(cita: Cita) {
        binding.nombre.setText(cita.nombreMascota)
        binding.raza.setText(cita.raza, false)
        binding.propietario.setText(cita.propietario)
        binding.telefono.setText(cita.telefono)
        formularioCargado = true
    }

    private fun mostrarEstado(estado: EstadoEdicion) {
        val editable = estado == EstadoEdicion.LISTA || estado == EstadoEdicion.ERROR
        binding.campoNombre.isEnabled = editable
        binding.campoRaza.isEnabled = editable
        binding.campoPropietario.isEnabled = editable
        binding.campoTelefono.isEnabled = editable
        binding.progresoEdicion.isVisible = estado == EstadoEdicion.CARGANDO ||
            estado == EstadoEdicion.GUARDANDO

        val mensaje = when (estado) {
            EstadoEdicion.NO_DISPONIBLE -> R.string.editar_cita_no_disponible
            EstadoEdicion.ERROR -> R.string.editar_cita_error_guardado
            else -> null
        }
        binding.mensaje.isVisible = mensaje != null
        if (mensaje != null) binding.mensaje.setText(mensaje)

        actualizarBoton()
        if (estado == EstadoEdicion.GUARDADA) {
            val navController = findNavController()
            if (!navController.popBackStack(R.id.homeFragment, false)) {
                navController.navigate(R.id.action_editarCitaFragment_to_homeFragment)
            }
        }
    }

    private fun actualizarBoton() {
        val estado = citaViewModel.estadoEdicion.value
        binding.editar.isEnabled = (estado == EstadoEdicion.LISTA || estado == EstadoEdicion.ERROR) &&
            citaViewModel.puedeEditarCita(
                binding.nombre.text.toString(),
                binding.raza.text.toString(),
                binding.propietario.text.toString(),
                binding.telefono.text.toString(),
            )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(FORMULARIO_CARGADO, formularioCargado)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val CITA_ID = "citaId"
        const val FORMULARIO_CARGADO = "formularioCargado"
    }
}
