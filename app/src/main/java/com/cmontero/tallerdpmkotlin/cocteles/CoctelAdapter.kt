package com.cmontero.tallerdpmkotlin.cocteles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cmontero.tallerdpmkotlin.R
import com.cmontero.tallerdpmkotlin.utils.Constantes

class CoctelAdapter(private val coctelList:List<CoctelModel>):
    RecyclerView.Adapter<CoctelAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fila_coctel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val contexto = holder.itemView.context
        val nombreCoctel = coctelList[position].strDrink
        val urlImagenCoctel = "${coctelList[position].strDrinkThumb}/preview"

        holder.name.text = nombreCoctel

        Glide.with(contexto)
            .load(urlImagenCoctel)
            .centerCrop()
            .placeholder(R.drawable.ic_wine)
            .into(holder.img)

        holder.itemView.setOnClickListener{
            val bundle = bundleOf(Constantes.ID_COCTEL to coctelList[position].idDrink, Constantes.URL_IMAGEN to urlImagenCoctel)
            Navigation.findNavController(holder.itemView).navigate(R.id.detalleCoctelFragment, bundle)
        }

    }

    override fun getItemCount(): Int {
        return coctelList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.nombreCoctel)
        val img : ImageView = view.findViewById(R.id.imagenCoctel)
    }

}