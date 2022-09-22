package com.cmontero.tallerdpmkotlin.realtime

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cmontero.tallerdpmkotlin.R
import com.cmontero.tallerdpmkotlin.databinding.FragmentNewuserBinding
import com.cmontero.tallerdpmkotlin.utils.Constantes
import com.cmontero.tallerdpmkotlin.utils.DatePickerFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList


class NewUserFragment : Fragment() {

    private var _binding: FragmentNewuserBinding? = null
    private val binding get() = _binding!!
    private var modoEdicion: Boolean = false
    private var objUsuario: UsuarioModelo? = null
    private lateinit var databaseRealtime: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            modoEdicion = it.getBoolean(Constantes.MODO_EDICION)
            objUsuario = it.getParcelable(Constantes.OBJ_USUARIO)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewuserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseRealtime = Firebase.database.reference

        val sexos: MutableList<String> = ArrayList()
        sexos.add("Masculino")
        sexos.add("Femenino")

        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, sexos)
        binding.spSexoUsuario.adapter = spinnerAdapter
        binding.edtFechaNacUsuario.setOnClickListener { showDatePickerDialog() }

        if (modoEdicion) {// Editar usuario
            binding.txtMiTitulo.text = "Editar usuario"
            binding.edtNombreUsuario.setText(objUsuario?.nombres )
            binding.edtEmailUsuario.setText(objUsuario?.email)
            binding.spSexoUsuario.setSelection(spinnerAdapter.getPosition(objUsuario?.sexo))
            binding.edtFechaNacUsuario.setText(objUsuario?.fechaNac)
            binding.btnEditarUsuario.visibility = View.VISIBLE
            binding.btnGuardarUsuario.visibility = View.GONE
        } else { // Agregar usuario
            binding.btnEditarUsuario.visibility = View.GONE
            binding.btnGuardarUsuario.visibility = View.VISIBLE
        }

        binding.btnGuardarUsuario.setOnClickListener {
            try {
                val nombres: String = binding.edtNombreUsuario.text.toString()
                val email: String = binding.edtEmailUsuario.text.toString()
                val fechaNac: String = binding.edtFechaNacUsuario.text.toString()
                val sexo: String = binding.spSexoUsuario.selectedItem.toString()
                val idUser = databaseRealtime.push().key.toString()
                val usuarioModel = UsuarioModelo(idUser, nombres, email, sexo, fechaNac)

                databaseRealtime.child("usuarios").child(idUser).setValue(usuarioModel)
                findNavController().popBackStack()

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
                val usuarioModel = UsuarioModelo(objUsuario?.idUsuario!!, nombres, email, sexo, fechaNac)

                databaseRealtime.child("usuarios").child(objUsuario?.idUsuario!!).setValue(usuarioModel)
                findNavController().popBackStack()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                val hide: MenuItem = menu.findItem(R.id.action_addUser)
                hide.isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        })


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