package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentNuevaCitaBinding
import com.example.dogapp.viewmodel.NuevaCitaViewModel

class NuevaCitaFragment : Fragment() {

    private var _binding: FragmentNuevaCitaBinding? = null
    private val binding get() = _binding!!
    private val nuevaCitaViewModel: NuevaCitaViewModel by viewModels()

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
        configurarToolbar()
        configurarGuardado()
    }

    private fun configurarFormulario() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.formulario = nuevaCitaViewModel
    }

    private fun configurarToolbar() {
        binding.toolbar.volver.setOnClickListener {
            findNavController().popBackStack(R.id.homeFragment, false)
        }
    }

    private fun configurarGuardado() {
        binding.guardarCita.setOnClickListener {
            if (!nuevaCitaViewModel.tieneSintomaSeleccionado(binding.sintomas.selectedItemPosition)) {
                Toast.makeText(requireContext(), R.string.nueva_cita_selecciona_sintoma, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
