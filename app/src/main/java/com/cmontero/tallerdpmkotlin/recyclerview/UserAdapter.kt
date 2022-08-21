package com.cmontero.tallerdpmkotlin.recyclerview

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.cmontero.tallerdpmkotlin.R
import android.widget.TextView

class UserAdapter(private val userList: List<UsuarioModel>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

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

        /*if(sexoUsuario == "Masculino")
            holder.imgFoto.setImageResource(R.drawable.user_masculino)
        else if(sexoUsuario == "Femenino")
            holder.imgFoto.setImageResource(R.drawable.user_femenino)*/

        //val imageid = context.resources.getIdentifier(foto, "drawable", context.packageName)
        //holder.imgFoto.setImageDrawable(ContextCompat.getDrawable(context, imageid))

        /*holder.itemView.setOnClickListener {
            val bundle = bundleOf(Constants.nombreUsuario to nombreUsuario, Constants.apellidoUsuario to apellidosUsuario, Constants.idFotoUser to imageid)
            it.findNavController().navigate(R.id.action_recyclerViewFragment_to_detalleFragment,bundle)
        }*/
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

    }

}