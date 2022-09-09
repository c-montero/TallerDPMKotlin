package com.cmontero.tallerdpmkotlin.mapas

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cmontero.tallerdpmkotlin.R
import com.cmontero.tallerdpmkotlin.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map:GoogleMap
    private val DEFAULTZOOM = 13F
    private var lastLocation: Location? = null
    private lateinit var miUbicacion :LatLng
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var listaMarcadores:MutableList<Marker> = mutableListOf()
    private var isLocationPermissionsGranted = false
    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION

    private val locationPermissionsGranted = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {permissions ->
        val fineLocationGranted = permissions.getOrDefault(FINE_LOCATION,false)
        val coarseLocationGranted = permissions.getOrDefault(COARSE_LOCATION,false)
        if (Objects.requireNonNull(fineLocationGranted) || Objects.requireNonNull(coarseLocationGranted)) {
            Toast.makeText(requireContext(), "Tiene los permisos", Toast.LENGTH_SHORT).show()
            isLocationPermissionsGranted = true
            inicializarMapa()
        } else {
            Toast.makeText(requireContext(), "No tiene los permisos aun", Toast.LENGTH_SHORT).show()
            isLocationPermissionsGranted = false
        }
    }

    private fun inicializarMapa() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkPermissionGranted()) {
            inicializarMapa()
        } else {
            locationPermissionsGranted.launch(arrayOf(FINE_LOCATION, COARSE_LOCATION))
        }

    }

    private fun checkPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.map = googleMap

        if (!isGPSAvailable()) {
            enabledGPS()
        }

        if (ContextCompat.checkSelfPermission(requireContext(), FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(), COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            locationPermissionsGranted.launch(arrayOf(FINE_LOCATION, COARSE_LOCATION))
            return
        }
        map.isMyLocationEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        //map.uiSettings.isMyLocationButtonEnabled = true

        val sucursal1 = LatLng(-12.154050, -76.972910)
        val sucursal2 = LatLng(-12.040779,-77.000656)
        val sucursal3 = LatLng(-12.168455, -76.999254)
        val nombre = "Veterinaria Mis Patitas"
        val sede1 = "Sede Surco"
        val sede2 = "Sede Miraflores"
        val sede3 = "Sede Chorrillos"

        val markerOptions1 = MarkerOptions()
        val markerOptions2 = MarkerOptions()
        val markerOptions3 = MarkerOptions()

        markerOptions1.position(sucursal1)
            .title(nombre)
            .snippet(sede1)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))

        markerOptions2.position(sucursal2)
            .title(nombre)
            .snippet(sede2)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        markerOptions3.position(sucursal3)
            .title(nombre)
            .snippet(sede3)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))

        val marcador1 = map.addMarker(markerOptions1)
        val marcador2 = map.addMarker(markerOptions2)
        val marcador3 = map.addMarker(markerOptions3)

        listaMarcadores.add(marcador1!!)
        listaMarcadores.add(marcador2!!)
        listaMarcadores.add(marcador3!!)

        centrarMarcadores(listaMarcadores)
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(sucursal1,DEFAULTZOOM))

        getMyLocation()

    }

    private fun enabledGPS() {
        AlertDialog.Builder(requireContext())
            .setMessage("Se requiere habilitar el GPS")
            .setPositiveButton("Configuración") { _: DialogInterface?, _: Int -> requireActivity().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton("Cancelar", null)
            .show()

    }

    private fun isGPSAvailable(): Boolean {
        return try {
            val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun isNetworkAvailable(): Boolean {
        return try {
            val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            false
        }
    }


    private fun getMyLocation() {
        try {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(),COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                locationPermissionsGranted.launch(arrayOf(FINE_LOCATION, COARSE_LOCATION))
                return
            }
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
        val padding = (alto*0.1).toInt()

        map.animateCamera(CameraUpdateFactory.newLatLngBounds(constructor.build(), ancho, alto, padding))

    }
}