package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentHomeBinding
import com.example.dogapp.view.adapter.CitaAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurarSalidaAlEscritorio()
        configurarListaCitas()
        configurarBotonNuevaCita()
    }

    private fun configurarBotonNuevaCita() {
        binding.fabNuevaCita.setOnClickListener {
            val navController = findNavController()
            if (navController.currentDestination?.id == R.id.homeFragment) {
                navController.navigate(R.id.action_homeFragment_to_nuevaCitaFragment)
            }
        }
    }

    private fun configurarListaCitas() {
        binding.rvCitas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCitas.adapter = CitaAdapter(::abrirDetalleCita)
    }

    private fun abrirDetalleCita(citaId: Int) {
        val navController = findNavController()
        if (navController.currentDestination?.id == R.id.homeFragment) {
            val argumentos = Bundle().apply { putInt("citaId", citaId) }
            navController.navigate(R.id.action_homeFragment_to_detalleCitaFragment, argumentos)
        }
    }

    private fun configurarSalidaAlEscritorio() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().moveTaskToBack(true)
                }
            }
        )
    }

    override fun onDestroyView() {
        binding.rvCitas.adapter = null
        super.onDestroyView()
        _binding = null
    }
}
