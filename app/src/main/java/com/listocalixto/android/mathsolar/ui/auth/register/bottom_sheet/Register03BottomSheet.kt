package com.listocalixto.android.mathsolar.ui.auth.register.bottom_sheet

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.listocalixto.android.mathsolar.BuildConfig
import com.listocalixto.android.mathsolar.R
import com.listocalixto.android.mathsolar.app.Constants.ERROR_PERMISION_DENIED
import com.listocalixto.android.mathsolar.databinding.BottomSheetRegister03Binding
import com.listocalixto.android.mathsolar.presentation.auth.register.RegisterViewModel
import com.listocalixto.android.mathsolar.utils.EventObserver
import com.listocalixto.android.mathsolar.utils.ProfilePicture
import com.listocalixto.android.mathsolar.utils.setupSnackbar
import java.net.URI

class Register03BottomSheet : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<RegisterViewModel>()

    private var optionProfilePicture = -1
    private var externalStoragePermission = -1
    private var cameraPermission = -1

    private lateinit var binding: BottomSheetRegister03Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_register03, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BottomSheetRegister03Binding.bind(view).also {
            it.lifecycleOwner = parentFragment
            it.registerViewModel = viewModel
        }
        initPermissions()
        onOptionProfilePictureSelected()

    }

    private fun initPermissions() {
        externalStoragePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        cameraPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )
    }

    private fun onOptionProfilePictureSelected() {
        viewModel.run {
            openGalleryEvent.observe(viewLifecycleOwner, EventObserver {
                requestReadExternalStoragePermission()
            })
            openCameraEvent.observe(viewLifecycleOwner, EventObserver {
                requestCameraPermission()
            })
        }
    }

    private fun requestCameraPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            cameraPermission -> {
                navigateToCamaraApp()
            }
            else -> {
                optionProfilePicture = REQUEST_IMAGE_CAPTURE
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun navigateToCamaraApp() {
        optionProfilePicture = REQUEST_IMAGE_CAPTURE
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityCamera.launch(takePictureIntent)
        } catch (e: ActivityNotFoundException) {
            viewModel.setExceptionMessage(e)
            this.dismiss()
        }
    }

    private fun requestReadExternalStoragePermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            externalStoragePermission -> {
                navigateToGalleryApp()
            }
            else -> {
                optionProfilePicture = REQUEST_IMAGE_GALLERY
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when {
                isGranted && optionProfilePicture == REQUEST_IMAGE_GALLERY -> navigateToGalleryApp()
                isGranted && optionProfilePicture == REQUEST_IMAGE_CAPTURE -> navigateToCamaraApp()
                else -> {
                    viewModel.setErrorStringResMessage(ERROR_PERMISION_DENIED)
                    this.dismiss()
                }
            }

        }

    private val startActivityGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && optionProfilePicture == REQUEST_IMAGE_GALLERY) {
                val data = result.data?.data
                viewModel.setProfilePictureType(ProfilePicture(uri = data))
                this.dismiss()
            }
        }

    private val startActivityCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && optionProfilePicture == REQUEST_IMAGE_CAPTURE) {
                val imageBitmap = result?.data?.extras?.get("data") as Bitmap
                viewModel.setProfilePictureType(ProfilePicture(bitmap = imageBitmap))
                this.dismiss()
            }
        }

    private fun navigateToGalleryApp() {
        optionProfilePicture = REQUEST_IMAGE_GALLERY
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        try {
            startActivityGallery.launch(galleryIntent)
        } catch (e: ActivityNotFoundException) {
            viewModel.setExceptionMessage(e)
            this.dismiss()
        }
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1001
        const val REQUEST_IMAGE_GALLERY = 1002
    }

}
