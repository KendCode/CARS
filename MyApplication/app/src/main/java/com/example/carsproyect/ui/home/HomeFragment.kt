package com.example.carsproyect.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.carsproyect.databinding.FragmentHomeBinding
import com.example.carsproyect.model.cars.Car
import com.example.carsproyect.model.cars.CarDataSource
import com.example.carsproyect.model.cars.CarRepository
import android.widget.Button
import android.widget.LinearLayout


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val factory = HomeViewModelFactory(CarRepository(CarDataSource(requireContext())))
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        /*val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/

        observandoHttp()
        homeViewModel.getCars()
        return root

    }


    private fun observandoHttp(){
        val btnRefresh = Button(requireContext()).apply {
            text = "Actualizar lista"
            setOnClickListener {
                homeViewModel.getCars() // Esto vuelve a pedir los datos al ViewModel
            }
        }
        val container = binding.carContainer

        homeViewModel.getAll.observe(this){ posts ->
            posts?.forEach {
                Log.d("Cars","Datos de la HTTP: ${it}")
            }
        }
        homeViewModel.error.observe(this){errors ->
            errors?.let {
                Log.d("Cars","Eroor en la peticion: ${it}")
            }
        }
        homeViewModel.getAll.observe(viewLifecycleOwner) { cars ->
            container.removeAllViews() // Limpiar contenido anterior

            cars?.forEach { car ->
                val carLayout = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(16, 16, 16, 16)
                    setBackgroundColor(0xFFE0E0E0.toInt())
                    val params = ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    params.bottomMargin = 20
                    layoutParams = params
                }

                val tvMake = TextView(requireContext()).apply { text = "Marca: ${car.make}" }
                val tvModel = TextView(requireContext()).apply { text = "Modelo: ${car.model}" }
                val tvYear = TextView(requireContext()).apply { text = "Año: ${car.year}" }
                val tvSpeed = TextView(requireContext()).apply { text = "Velocidad: ${car.speed} km/h" }
                val tvFuel = TextView(requireContext()).apply { text = "Combustible: ${car.fuel} L" }

                val btnEdit = Button(requireContext()).apply {
                    text = "Editar"
                    setOnClickListener {
                        Log.d("EDITAR", "Editar: ${car.model}")
                        // Aquí puedes abrir un diálogo o navegar a otra pantalla para editar
                    }
                }

                val btnDelete = Button(requireContext()).apply {
                    text = "Eliminar"
                    setOnClickListener {
                        homeViewModel.deleteCar() // <- Aquí se llama correctamente
                    }
                }
                val btnCreate = Button(requireContext()).apply {
                    text = "Crear"
                    setOnClickListener {
                        homeViewModel.createCar() // <- se crea
                    }
                }

                carLayout.addView(tvMake)
                carLayout.addView(tvModel)
                carLayout.addView(tvYear)
                carLayout.addView(tvSpeed)
                carLayout.addView(tvFuel)
                carLayout.addView(btnEdit)
                carLayout.addView(btnDelete)
                carLayout.addView(btnCreate)

                container.addView(carLayout)
            }
        }
        val btnDelete = Button(requireContext()).apply {
            text = "Eliminar"
            setOnClickListener {
                homeViewModel.deleteCar() // ahora sí llama al ViewModel
            }
        }
        val btnEdit = Button(requireContext()).apply {
            text = "Editar"
            setOnClickListener {
                homeViewModel.updateCar() // si ya tienes lógica para esto
            }
        }
        val btnCreate = Button(requireContext()).apply {
            text = "Crear"
            setOnClickListener {
                homeViewModel.createCar() // si ya tienes lógica para esto
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}