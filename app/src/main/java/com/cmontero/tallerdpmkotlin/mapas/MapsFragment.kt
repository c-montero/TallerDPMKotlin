package com.cmontero.tallerdpmkotlin.mapas

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cmontero.tallerdpmkotlin.R
import com.cmontero.tallerdpmkotlin.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map:GoogleMap
    private val DEFAULTZOOM = 15f
    private val REQUESTLOCATION = 1000
    private var lastLocation: Location? = null
    private lateinit var miUbicacion :LatLng
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var listaMarcadores:MutableList<Marker>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        listaMarcadores = mutableListOf()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.map = googleMap

        requestLocationPermission()

        getMyLocation()

        setupUI()

        myInterfaz()
    }

    private fun requestLocationPermission() {
        if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUESTLOCATION)
        }
    }

    private fun getMyLocation() {
        try {

            fusedLocationClient?.lastLocation?.addOnSuccessListener { location->
                if(location!= null){
                    lastLocation = location
                    miUbicacion = LatLng(lastLocation!!.latitude, lastLocation!!.longitude)
                    colocarMarcador(miUbicacion)
                }
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun setupUI() {
        try {

            map.isMyLocationEnabled = true
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true

        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun myInterfaz(){

        val sucursal1 = LatLng(-12.154050, -76.972910)
        val sucursal2 = LatLng(-12.040779,-77.000656)
        val nombre = "Veterinaria Mis Patitas"
        val nombre1 = "Sede Surco"
        val nombre2 = "Sede Miraflores"

        val markerOptions1 = MarkerOptions()
        val markerOptions2 = MarkerOptions()

        markerOptions1.position(sucursal1)
            .title(nombre)
            .snippet(nombre1)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))

        markerOptions2.position(sucursal2)
            .title(nombre)
            .snippet(nombre2)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        val marcador1 = map.addMarker(markerOptions1)
        val marcador2 = map.addMarker(markerOptions2)

        listaMarcadores.add(marcador1!!)
        listaMarcadores.add(marcador2!!)

        centrarMarcadores(listaMarcadores)
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(sucursal1,DEFAULTZOOM))

    }

    private fun colocarMarcador(posicion: LatLng){
        map.addMarker(MarkerOptions().position(posicion))
    }

    private fun centrarMarcadores(listaMarcadores:List<Marker>){

        val constructor = LatLngBounds.Builder()
        for (marker in listaMarcadores){
            constructor.include(marker.position)
        }

        val ancho = resources.displayMetrics.widthPixels
        val alto = resources.displayMetrics.heightPixels
        val padding = (alto*0.25).toInt()

        map.animateCamera(CameraUpdateFactory.newLatLngBounds(constructor.build(), ancho, alto, padding))

    }
}