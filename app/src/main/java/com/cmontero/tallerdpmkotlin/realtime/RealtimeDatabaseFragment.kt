package com.cmontero.tallerdpmkotlin.realtime

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmontero.tallerdpmkotlin.R
import com.cmontero.tallerdpmkotlin.databinding.FragmentRealtimeBinding
import com.cmontero.tallerdpmkotlin.utils.Constantes
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RealtimeDatabaseFragment : Fragment() {

    private var _binding:FragmentRealtimeBinding?=null
    private val binding get() = _binding!!
    private lateinit var listaUsuarios:MutableList<UsuarioModelo>
    private lateinit var databaseRealtime: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentRealtimeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        databaseRealtime = Firebase.database.reference

        binding.recyclerViewUsuarios.layoutManager = LinearLayoutManager(requireContext())
        listaUsuarios = mutableListOf()

        val adapter = MiUsuarioAdapter(listaUsuarios)
        binding.recyclerViewUsuarios.adapter = adapter

        databaseRealtime.child("usuarios").addValueEventListener(object:ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                listaUsuarios.clear()
                binding.progressBar.visibility = View.GONE
                if(snapshot.exists()){
                    for (ds in snapshot.children){
                        val id = ds.child("idUsuario").value.toString()
                        val name = ds.child("nombres").value.toString()
                        val email = ds.child("email").value.toString()
                        val sexo = ds.child("sexo").value.toString()
                        val fechaNac = ds.child("fechaNac").value.toString()
                        val user = UsuarioModelo(id,name,email,sexo,fechaNac)
                        listaUsuarios.add(user)
                    }

                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"Canlelled",Toast.LENGTH_SHORT).show()
            }

        })

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                val hide: MenuItem = menu.findItem(R.id.action_addUser)
                hide.isVisible = true
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.action_addUser) {
                    val bundle = Bundle()
                    bundle.putBoolean(Constantes.MODO_EDICION, false)
                    bundle.putString(Constantes.ID_USUARIO, "")
                    findNavController().navigate(R.id.newUserFragment, bundle)
                    return true
                }
                return false
            }
        })

    }
}