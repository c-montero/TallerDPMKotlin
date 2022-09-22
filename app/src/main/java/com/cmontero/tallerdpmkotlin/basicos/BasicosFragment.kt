package com.cmontero.tallerdpmkotlin.basicos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cmontero.tallerdpmkotlin.R
import com.cmontero.tallerdpmkotlin.databinding.FragmentBasicosBinding


class BasicosFragment : Fragment() {

    private var _binding: FragmentBasicosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBasicosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val txtTitulo = binding.txtMiTitulo
        val edtNombres = binding.edtNombres
        val edtApellidos = binding.edtApellidos
        val btnBoton = binding.btnMiBoton
        val imgImagen = binding.imgImagen
        val switchMaterial = binding.switch1
        val toggle = binding.toggleButton
        val progressBar = binding.progressBar
        val rgSexo = binding.rgSexo
        val rbMasculino = binding.rbMasculino
        val rbFemenino = binding.rbFemenino
        val spinner = binding.spinner

        txtTitulo.text = "Controles Basicos"

        btnBoton.setOnClickListener {
            val nombres = edtNombres.text.toString()
            val apellidos = edtApellidos.text
            Toast.makeText(requireContext(), "Hola $nombres $apellidos", Toast.LENGTH_LONG).show()
        }

        imgImagen.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_andy))
        imgImagen.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.background))

        switchMaterial.setOnClickListener {
            if (switchMaterial.isChecked) {
                switchMaterial.text = "ON"
                toggle.isChecked=true
                progressBar.visibility = View.VISIBLE
            } else {
                switchMaterial.text = "OFF"
                toggle.isChecked = false
                progressBar.visibility = View.INVISIBLE
            }
        }

        toggle.setOnClickListener {
            if(toggle.isChecked){
                switchMaterial.isChecked = true
                switchMaterial.text = "ON"
                progressBar.visibility = View.VISIBLE
            } else {
                switchMaterial.isChecked = false
                switchMaterial.text = "OFF"
                progressBar.visibility = View.INVISIBLE
            }
        }

        val estados = mutableListOf<String>()
        estados.add("Soltero")
        estados.add("Casado")
        estados.add("Viudo")
        estados.add("Divorciado")

        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, estados)
        spinner.adapter = spinnerAdapter

        rgSexo.setOnCheckedChangeListener { _, _ ->
            if (rbMasculino.isChecked) {
                Toast.makeText(requireContext(), "Sexo Masculino", Toast.LENGTH_SHORT).show()
            } else if (rbFemenino.isChecked) {
                Toast.makeText(requireContext(), "Sexo Femenino", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}