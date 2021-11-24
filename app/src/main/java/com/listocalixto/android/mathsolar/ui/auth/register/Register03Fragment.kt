package com.listocalixto.android.mathsolar.ui.auth.register

import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.databinding.FragmentRegister03Binding
import com.listocalixto.android.mathsolar.presentation.auth.register.RegisterViewModel
import com.listocalixto.android.mathsolar.utils.EventObserver
import com.listocalixto.android.mathsolar.utils.setupSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Register03Fragment : Fragment(R.layout.fragment_register_03) {

    private val viewModel by activityViewModels<RegisterViewModel>()

    private var activityNavHost: View? = null

    private lateinit var binding: FragmentRegister03Binding
    private lateinit var drawable: Drawable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegister03Binding.bind(view).also {
            it.lifecycleOwner = this.viewLifecycleOwner
            it.registerViewModel = viewModel
        }

        activity?.let {
            activityNavHost = it.findViewById(R.id.nav_host_activity)
            setupNavigation(it)
            setupSnackbar(it)
        }

        viewModel.apply {
            if (isBitmapProfilePictureNull()) {
                drawable = binding.profilePicture.drawable
                val bitmap = drawable.toBitmap()
                setBitmapProfilePicture(bitmap)
            }
            setCurrentFragment(R.id.register03Fragment)
        }

    }

    private fun setupSnackbar(activity: Activity) {
        binding.root.setupSnackbar(
            this.viewLifecycleOwner,
            viewModel.snackbarText,
            Snackbar.LENGTH_INDEFINITE,
            activity.findViewById<MaterialButton>(R.id.button_next)
        )

        viewModel.apply {
            errorMessage.observe(viewLifecycleOwner, { message ->
                message?.let {
                    showErrorMessage(it)
                }
            })
        }
    }

    private fun setupNavigation(activity: Activity) {
        viewModel.apply {
            backEvent.observe(viewLifecycleOwner, EventObserver {
                activity.onBackPressed()
            })
            selectProfilePictureEvent.observe(viewLifecycleOwner, EventObserver {
                showBottomSheet()
            })
            successfullyUserCreatedEvent.observe(viewLifecycleOwner, EventObserver {
                //navigateToSuccessfulFragment()
                navigateToMainParentFragment()
            })
        }
    }

    private fun navigateToMainParentFragment() {
        activityNavHost?.let {
            Navigation.findNavController(it)
                .navigate(R.id.registerParentFragment_to_mainParentFragment)
        }
    }

    private fun navigateToSuccessfulFragment() {
        findNavController().navigate(R.id.register03Fragment_to_successfulFragment)
    }

    private fun showBottomSheet() {
        findNavController().navigate(R.id.register03Fragment_to_register03BottomSheet)
    }

}