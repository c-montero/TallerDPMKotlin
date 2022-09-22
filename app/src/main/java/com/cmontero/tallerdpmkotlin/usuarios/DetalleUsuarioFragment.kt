package com.cmontero.tallerdpmkotlin.usuarios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cmontero.tallerdpmkotlin.R
import com.cmontero.tallerdpmkotlin.databinding.FragmentDetalleusuarioBinding
import com.cmontero.tallerdpmkotlin.utils.Constantes


class DetalleUsuarioFragment : Fragment() {

    private var _binding: FragmentDetalleusuarioBinding? = null
    private val binding get() = _binding!!
    private lateinit var usuario: UsuarioModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usuario = it.getParcelable(Constantes.OBJ_USUARIO)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentDetalleusuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (usuario.sexo == "Masculino")
            binding.imgFotoUsuario.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.user_masculino))
        else if(usuario.sexo =="Femenino")
            binding.imgFotoUsuario.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.user_femenino))

        binding.txtNombresUsuario.text = usuario.nombres
        binding.txtEmailUsuario.text = usuario.email
        binding.txtFechaNacUsuario.text = usuario.fechaNac
        binding.txtSexoUsuario.text = usuario.sexo

    }

}