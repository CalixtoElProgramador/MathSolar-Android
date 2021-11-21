package com.listocalixto.android.mathsolar.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.listocalixto.android.mathsolar.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

}