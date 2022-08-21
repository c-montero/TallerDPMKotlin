package com.cmontero.tallerdpmkotlin.recyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmontero.tallerdpmkotlin.databinding.FragmentRecyclerviewBinding


class RecyclerviewFragment : Fragment() {

    private var _binding: FragmentRecyclerviewBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecyclerviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listaUsuarios: List<UsuarioModel?>?
        listaUsuarios = getListaUsuarios()

        val adapter: RecyclerView.Adapter<UserAdapter.ViewHolder> = UserAdapter(listaUsuarios)

        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUsers.adapter = adapter
    }

    private fun getListaUsuarios(): List<UsuarioModel> {
        val userModels: MutableList<UsuarioModel> = ArrayList()
        userModels.add(UsuarioModel(1, "Juan Lopez", "juanlopez@mail.com", "Masculino", "01/02/1982"))
        userModels.add(UsuarioModel(2, "Mario Soto", "mariosoto@mail.com", "Masculino", "21/05/1981"))
        userModels.add(UsuarioModel(3, "Luz Perez", "luzperez@mail.com", "Femenino", "11/06/1983"))
        userModels.add(UsuarioModel(4, "Teresa Moreno", "teresamoreno@mail.com", "Femenino", "13/02/1980"))
        userModels.add(UsuarioModel(5, "Omar Quiroz", "omarquiroz@mail.com", "Masculino", "14/04/1981"))
        userModels.add(UsuarioModel(6, "Pedro Linares", "pedrolinares@mail.com", "Masculino", "12/11/1982"))
        userModels.add(UsuarioModel(7, "Mateo Guillen", "mateoguillen@mail.com", "Masculino", "03/03/1981"))
        userModels.add(UsuarioModel(8, "Katy Salazar", "katysalazar@mail.com", "Femenino", "24/10/1980"))
        userModels.add(UsuarioModel(9, "Alfredo Rios", "alfredorios@mail.com", "Masculino", "02/02/1982"))
        userModels.add(UsuarioModel(10, "Elena Valdez", "elenavaldez@mail.com", "Femenino", "09/01/1981"))
        return userModels
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}