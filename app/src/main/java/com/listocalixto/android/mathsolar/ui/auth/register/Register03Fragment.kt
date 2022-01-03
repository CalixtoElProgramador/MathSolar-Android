package com.listocalixto.android.mathsolar.ui.auth.register

import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyEnterMotionTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw { startPostponedEnterTransition() }

        binding = FragmentRegister03Binding.bind(view).also {
            it.lifecycleOwner = this.viewLifecycleOwner
            it.registerViewModel = viewModel
        }

        activity?.let {
            activityNavHost = it.findViewById(R.id.nav_host_activity)
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

        setupNavigation()

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

    private fun setupNavigation() {
        viewModel.apply {
            selectProfilePictureEvent.observe(viewLifecycleOwner, EventObserver {
                showBottomSheet()
            })
        }
    }

    private fun showBottomSheet() {
        findNavController().navigate(R.id.register03Fragment_to_register03BottomSheet)
    }

    private fun applyEnterMotionTransition() {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }
}