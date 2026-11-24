package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentEditarCitaBinding

class EditarCitaFragment : Fragment() {

    private var _binding: FragmentEditarCitaBinding? = null
    private val binding get() = _binding!!

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
        val citaId = requireArguments().getInt(CITA_ID)
        configurarFormulario(citaId)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val CITA_ID = "citaId"
    }
}
