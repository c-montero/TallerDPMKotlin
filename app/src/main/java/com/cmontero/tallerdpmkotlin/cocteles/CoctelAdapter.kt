package com.cmontero.tallerdpmkotlin.cocteles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cmontero.tallerdpmkotlin.R

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