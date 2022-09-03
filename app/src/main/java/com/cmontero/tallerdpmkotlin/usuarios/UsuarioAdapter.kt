package com.cmontero.tallerdpmkotlin.usuarios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.cmontero.tallerdpmkotlin.R
import com.cmontero.tallerdpmkotlin.utils.Constantes


class UsuarioAdapter(private val userList: MutableList<UsuarioModel>): RecyclerView.Adapter<UsuarioAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fila_usuarios, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nombreUsuario = userList[position].nombres
        val emailUsuario = userList[position].email
        val sexoUsuario = userList[position].sexo
        val fechaNac = userList[position].fechaNac
        val context = holder.itemView.context
        holder.txtName.text = nombreUsuario
        holder.txtSexo.text = sexoUsuario
        holder.txtEmail.text = emailUsuario
        holder.fechaNac.text = fechaNac

        if(sexoUsuario == "Masculino")
            holder.imgFoto.setImageResource(R.drawable.user_masculino)
        else if(sexoUsuario == "Femenino")
            holder.imgFoto.setImageResource(R.drawable.user_femenino)

        holder.imgEditar.setOnClickListener {
            val bundle = bundleOf(Constantes.MODO_EDICION to true, Constantes.OBJ_USUARIO to userList[position])
            Navigation.findNavController(holder.itemView).navigate(R.id.nav_newusuario, bundle)
        }

        holder.imgDelete.setOnClickListener {
            val daoUsuarios = DAOUsuarios(context)
            val result = daoUsuarios.eliminarUsuario(userList[position].id!!)
            if (result <= 0) {
                Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Se eliminó correctamente", Toast.LENGTH_SHORT).show()
                userList.removeAt(holder.adapterPosition)
                notifyItemRemoved(position)
            }
        }

        holder.itemView.setOnClickListener {
            val bundle = bundleOf(Constantes.OBJ_USUARIO to userList[position])
            Navigation.findNavController(it).navigate(R.id.action_nav_usuarios_to_detalleFragment,bundle)
        }
    }


    override fun getItemCount(): Int {
        return userList.size
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txtNombres)
        val txtEmail: TextView = view.findViewById(R.id.txtEmail)
        val txtSexo:TextView = view.findViewById(R.id.txtSexo)
        val fechaNac:TextView = view.findViewById(R.id.txtFechaNac)
        val imgFoto: ImageView = view.findViewById(R.id.imgFoto)
        val imgEditar:ImageView = view.findViewById(R.id.imgEdit)
        val imgDelete:ImageView = view.findViewById(R.id.imgDelete)

    }

}