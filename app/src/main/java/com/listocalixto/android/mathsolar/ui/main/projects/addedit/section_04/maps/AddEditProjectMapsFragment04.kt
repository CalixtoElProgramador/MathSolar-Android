package com.listocalixto.android.mathsolar.ui.main.projects.addedit.section_04.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.transition.MaterialSharedAxis
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentAddeditProjectMaps04Binding
import com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project.AddEditProjectViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddEditProjectMapsFragment04 : Fragment() {

    private val viewModel by activityViewModels<AddEditProjectViewModel>()
    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        setMapStyle(googleMap)
        setOnMapTypeListener(googleMap)
        setMapLongClick(googleMap)
        setPoiClick(googleMap)
        enableLocation()
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: FragmentAddeditProjectMaps04Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyEnterMotionTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_addedit_project_maps_04, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }
        binding = FragmentAddeditProjectMaps04Binding.bind(view)
        binding.run {
            lifecycleOwner = this@AddEditProjectMapsFragment04.viewLifecycleOwner
            addEditProjectViewModel = viewModel
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        }

        setupObservers()

    }

    private fun setupObservers() {
        viewModel.isPermissionsGranted.observe(viewLifecycleOwner, {
            if (it) {
                enableLocation()
            }
        })
    }

    private fun applyEnterMotionTransition() {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            when (activity?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    val success = map.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            requireContext(),
                            R.raw.map_light_style
                        )
                    )
                    if (!success) {
                        Log.e(TAG, "Style parsing failed.")
                    }
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    val success = map.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            requireContext(),
                            R.raw.map_dark_style
                        )
                    )
                    if (!success) {
                        Log.e(TAG, "Style parsing failed.")
                    }
                }
            }

        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            // A Snippet is Additional text that's displayed below the title.
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
        }
    }

    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker?.showInfoWindow()
        }
    }

    private fun setOnMapTypeListener(googleMap: GoogleMap) {
        binding.toolbarMap.setOnMenuItemClickListener {
            when (it.itemId) {
                // Change the map type based on the user's selection.
                R.id.normal_map -> {
                    googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                    true
                }
                R.id.hybrid_map -> {
                    googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                    true
                }
                R.id.satellite_map -> {
                    googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                    true
                }
                R.id.terrain_map -> {
                    googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        if (isPermissionGranted()) {
            googleMap.run {
                isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
                fusedLocationClient.lastLocation.addOnSuccessListener { myLocation ->
                    val lat = myLocation.latitude
                    val lng = myLocation.longitude
                    val position = LatLng(lat, lng)
                    val zoom = 15.0f
                    addMarker(MarkerOptions().position(position).title(getString(R.string.you_are_here)))
                    moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom))
                }
            }
        } else {
            requestLocationPermissions()
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions() {
        val permissionsArray = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        

        ActivityCompat.requestPermissions(
            requireActivity(),
            permissionsArray,
            PERMISSIONS_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.isLocationPermissionsGranted(true)
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName
        private const val PERMISSIONS_CODE = 193
    }

}