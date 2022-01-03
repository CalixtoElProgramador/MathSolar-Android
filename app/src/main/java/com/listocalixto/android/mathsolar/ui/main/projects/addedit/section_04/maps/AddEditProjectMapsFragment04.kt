package com.listocalixto.android.mathsolar.ui.main.projects.addedit.section_04.maps

import android.content.res.Configuration
import android.content.res.Resources
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.transition.MaterialSharedAxis
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentAddeditProjectMaps04Binding
import com.listocalixto.android.mathsolar.presentation.main.projects.addedit_project.AddEditProjectViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditProjectMapsFragment04 : Fragment() {

    private val viewModel by activityViewModels<AddEditProjectViewModel>()
    private val callback = OnMapReadyCallback { googleMap ->


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        binding.run {
            toolbarMap.setOnMenuItemClickListener { onOptionItemSelected(it, googleMap) }

        }
        setMapStyle(googleMap)

    }

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
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)

        }
    }

    private fun applyEnterMotionTransition() {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

    private fun onOptionItemSelected(
        it: MenuItem,
        googleMap: GoogleMap
    ) = when (it.itemId) {
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

    companion object {
        private val TAG = this::class.java.simpleName
    }

}