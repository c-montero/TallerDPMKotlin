package com.cmontero.tallerdpmkotlin.cocteles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.cmontero.tallerdpmkotlin.R
import com.cmontero.tallerdpmkotlin.databinding.FragmentDetallecoctelBinding
import com.cmontero.tallerdpmkotlin.utils.Constantes
import org.json.JSONException
import org.json.JSONObject

class DetalleCoctelFragment : Fragment() {

    private var idCoctel: String? = null
    private var urlCoctel: String? = null
    private var _binding: FragmentDetallecoctelBinding? = null
    private val binding get() = _binding!!
    private lateinit var cola: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idCoctel = it.getString(Constantes.ID_COCTEL)
            urlCoctel = it.getString(Constantes.URL_IMAGEN)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetallecoctelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lvIngredientes.divider = null

        cola = Volley.newRequestQueue(requireContext())

        val url = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=$idCoctel"

        binding.progressBar.visibility = View.VISIBLE
        Glide.with(requireContext())
            .load(urlCoctel)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding.imagenCoctel)

        val jsonRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response: JSONObject ->
                try {
                    val jsonArray = response.getJSONArray("drinks")
                    val jsonObject = jsonArray[0] as JSONObject
                    binding.txtCoctel.text = jsonObject.getString("strDrink")
                    binding.txtCoctel.visibility = View.VISIBLE
                    binding.txtIngredientes.visibility = View.VISIBLE
                    binding.txtTitlePreparacion.visibility = View.VISIBLE
                    binding.txtPreparacion.visibility = View.VISIBLE
                    binding.txtPreparacion.text = jsonObject.getString("strInstructions")
                    binding.progressBar.visibility = View.GONE
                    val listaIngredientes = ArrayList<String>()
                    for (i in 1..15) {
                        if (jsonObject.getString("strIngredient$i") != "null") listaIngredientes.add(jsonObject.getString("strIngredient$i")) else break
                    }
                    val adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listaIngredientes)
                    binding.lvIngredientes.adapter = adaptador
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { error: VolleyError ->
            Toast.makeText(requireContext(), "Error al traer los datos: $error", Toast.LENGTH_LONG).show()
        }

        cola.add(jsonRequest)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}