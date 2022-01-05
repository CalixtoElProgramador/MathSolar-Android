package com.listocalixto.android.mathsolar.ui.main.projects.addedit.section_04.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentAddeditProjectMaps04Binding
import com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project.AddEditProjectViewModel
import com.listocalixto.android.mathsolar.utils.EventObserver
import com.listocalixto.android.mathsolar.utils.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddEditProjectMapsFragment04 : Fragment(R.layout.fragment_addedit_project_maps_04) {

    private val viewModel by activityViewModels<AddEditProjectViewModel>()
    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        setMapStyle(googleMap)
        setMapLongClick(googleMap)
        setPoiClick(googleMap)
        setupObservers()

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
        viewModel.saveIsLocationPermissionEnabled(isLocationGranted())
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }
        Log.d(TAG, "onViewCreated")
        binding = FragmentAddeditProjectMaps04Binding.bind(view)
        binding.run {
            lifecycleOwner = this@AddEditProjectMapsFragment04.viewLifecycleOwner
            addEditProjectViewModel = viewModel
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        }
        setupSnackbar()

    }

    private fun showMapTypesPopUpMenu() {
        PopupMenu(requireContext(), binding.ImgBtnMore).run {
            menuInflater.inflate(R.menu.map_options, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
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
            show()
        }
    }

    private fun inflateMyLocationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.my_location_dialog_map_title))
            .setMessage(resources.getString(R.string.my_location_dialog_map_message))
            .setIcon(R.drawable.ic_my_location)
            .setNegativeButton(resources.getString(R.string.my_location_dialog_map_negative_option)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.my_location_dialog_map_positive_option)) { _, _ ->
                enableLocation()
            }
            .show()
    }

    private fun inflateHelpDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.help_dialog_map_title))
            .setMessage(resources.getString(R.string.help_dialog_map_message))
            .setIcon(R.drawable.ic_help)
            .setPositiveButton(resources.getString(R.string.help_dialog_map_positive_option)) { _, _ ->
                viewModel.showSnackbarMessage(R.string.you_welcome)
            }
            .show()
    }

    private fun setupObservers() {
        viewModel.run {

            moreEvent.observe(viewLifecycleOwner, EventObserver {
                showMapTypesPopUpMenu()
            })

            helpEvent.observe(viewLifecycleOwner, EventObserver {
                openInstructionsDialog()
            })

            myLocationEvent.observe(viewLifecycleOwner, EventObserver {
                openEnableMyLocationDialog()
            })

            isLocationEnable.observe(viewLifecycleOwner, {
                if (it) { enableLocation() }
            })

        }
    }

    private fun openInstructionsDialog() {
        inflateHelpDialog()
    }

    private fun openEnableMyLocationDialog() {
        inflateMyLocationDialog()
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
            map.clear()
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

    /*private fun setOnMapTypeListener(googleMap: GoogleMap) {
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
    }*/

    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        if (isLocationGranted()) {
            googleMap.run {
                isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
                fusedLocationClient.lastLocation.addOnSuccessListener { myLocation: Location? ->
                    myLocation?.let {
                        val lat = it.latitude
                        val lng = it.longitude
                        val position = LatLng(lat, lng)
                        val zoom = 15.0f
                        addMarker(
                            MarkerOptions().position(position)
                                .title(getString(R.string.you_are_here))
                        )
                        moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom))
                    } ?: run {
                        viewModel.showSnackbarMessage(R.string.location_disable)
                    }
                }
            }
        } else {
            requestMultiplePermissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun isLocationGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestMultiplePermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.filterValues { it.not() }.isEmpty()) {
                viewModel.saveIsLocationPermissionEnabled(true)
            } else {
                viewModel.saveIsLocationPermissionEnabled(false)
                viewModel.showSnackbarMessage(R.string.permission_denied)
            }
        }


    private fun setupSnackbar() {
        binding.root.setupSnackbar(
            this.viewLifecycleOwner,
            viewModel.snackbarText,
            Snackbar.LENGTH_LONG,
            binding.btnSaveLocation
        )
    }

    companion object {
        private val TAG = this::class.java.simpleName
    }

}