package com.cmontero.tallerdpmkotlin.usuarios

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.cmontero.tallerdpmkotlin.R
import com.cmontero.tallerdpmkotlin.databinding.FragmentNewusuarioBinding
import com.cmontero.tallerdpmkotlin.utils.Constantes
import com.cmontero.tallerdpmkotlin.utils.DatePickerFragment


class NewUsuarioFragment : Fragment() {

    private var _binding: FragmentNewusuarioBinding? = null
    private val binding get() = _binding!!
    private var modoEdicion: Boolean = false
    private var idUsuario: Int = 0


    private lateinit var daoUsuarios:DAOUsuarios
    private lateinit var usuario: UsuarioModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            modoEdicion = it.getBoolean(Constantes.MODO_EDICION)
            idUsuario = it.getInt(Constantes.ID_USUARIO)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewusuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        daoUsuarios = DAOUsuarios(requireContext())

        val sexos: MutableList<String> = ArrayList()
        sexos.add("Masculino")
        sexos.add("Femenino")

        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, sexos)
        binding.spSexoUsuario.adapter = spinnerAdapter

        binding.edtFechaNacUsuario.setOnClickListener { showDatePickerDialog() }

        if(modoEdicion){// Editar usuario
            binding.txtMiTitulo.text = "Editar usuario"
            usuario = daoUsuarios.obtenerUsuario(idUsuario)!!
            binding.edtNombreUsuario.setText(usuario.nombres)
            binding.edtEmailUsuario.setText(usuario.email)
            binding.btnEditarUsuario.visibility = View.VISIBLE
            binding.btnGuardarUsuario.visibility = View.GONE
        }
        else{ // Agregar usuario
            binding.btnEditarUsuario.visibility = View.GONE
            binding.btnGuardarUsuario.visibility = View.VISIBLE
        }

        binding.btnGuardarUsuario.setOnClickListener { v ->
            try {
                val nombres: String = binding.edtNombreUsuario.text.toString()
                val email: String = binding.edtEmailUsuario.text.toString()
                val fechaNac: String = binding.edtFechaNacUsuario.text.toString()
                val sexo: String = binding.spSexoUsuario.selectedItem.toString()
                val usuarioModel = UsuarioModel(0, nombres, email, sexo, fechaNac)
                val x = daoUsuarios.registrarUsuario(usuarioModel)
                if (x > 0) {
                    Toast.makeText(requireContext(), "Se registró correctamente", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Ocurrió un error al registrar", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.btnEditarUsuario.setOnClickListener {
            try {
                val nombres: String = binding.edtNombreUsuario.text.toString()
                val email: String = binding.edtEmailUsuario.text.toString()
                val fechaNac: String = binding.edtFechaNacUsuario.text.toString()
                val sexo: String = binding.spSexoUsuario.selectedItem.toString()
                val usuarioModel = UsuarioModel(idUsuario, nombres, email, fechaNac, sexo)
                val x = daoUsuarios.editarUsuario(usuarioModel)
                if (x > 0) {
                    Toast.makeText(requireContext(), "Se editó correctamente", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Ocurrió un error al editar", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }



    }

    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance { _, year, mes, dia ->
            val fecha = dia.toString() + "/" + (mes + 1) + "/" + year // 18/06/2022
            binding.edtFechaNacUsuario.setText(fecha)
        }
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}