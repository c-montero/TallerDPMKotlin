package com.cmontero.tallerdpmkotlin.cocteles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cmontero.tallerdpmkotlin.databinding.FragmentCoctelesBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

class CoctelesFragment : Fragment() {

    private var _binding: FragmentCoctelesBinding? = null
    private val binding get() = _binding!!
    private lateinit var cola : RequestQueue
    private lateinit var url: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCoctelesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerCocteles.layoutManager = LinearLayoutManager(requireContext())

        val listaIngredientes: MutableList<String> = ArrayList()
        listaIngredientes.add("Amaretto")
        listaIngredientes.add("Gin")
        listaIngredientes.add("Pisco")
        listaIngredientes.add("Rum")
        listaIngredientes.add("Tequila")
        listaIngredientes.add("Vodka")
        val adapterSpinner : ArrayAdapter<*> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaIngredientes)
        binding.spinner.adapter = adapterSpinner

        var ingrediente:String

        binding.spinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                ingrediente = binding.spinner.selectedItem.toString()
                url = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=$ingrediente"

            }
            override fun onNothingSelected(p0: AdapterView<*>?) { }

        }

        // PASOS PARA USAR LA LIBRERIA VOLLEY

        // PASO 1: MODIFICAMOS EL ARCHIVO MANIFEST

        // PASO 2: AGREGAMOS LAS DEPENDENCIAS EN EL ARCHIVO BUILD.GRADLE (MODULO)

        // PASO 3: INSTANCIAR LA COLA DE PETICIONES
        cola = Volley.newRequestQueue(requireContext())


        binding.btnBuscar.setOnClickListener {
            buscar()
        }

    }

    private fun buscar() {
        try {
            binding.progressBar.visibility = View.VISIBLE
            // PASO 4: CREAR LA PETICIÓN
            val jsonRequest = JsonObjectRequest(Request.Method.GET, url,null,
            { response: JSONObject ->
                try {
                    val jsonArray = response.getJSONArray("drinks")
                    val gson = Gson()
                    val type :Type = object: TypeToken<List<CoctelModel>>(){}.type
                    val listaCocteles = gson.fromJson<List<CoctelModel>>(jsonArray.toString(), type)
                    val miAdapter = CoctelAdapter(listaCocteles)
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerCocteles.adapter = miAdapter
                }
                catch (e: JSONException){
                    e.printStackTrace()
                }
            }){error: VolleyError ->
                Toast.makeText(requireContext(),"Error:$error",Toast.LENGTH_SHORT).show()
            }

            cola.add(jsonRequest)
        }
        catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}