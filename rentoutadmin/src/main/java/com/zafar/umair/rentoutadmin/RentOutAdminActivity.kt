package com.zafar.umair.rentoutadmin

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RentOutAdminActivity : AppCompatActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _: Boolean ->
//            if (isGranted == true) {
//                viewModel.getDeviceLocation(fusedLocationProviderClient)
//            }
        }

    private fun askPermissions() = when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        ) -> {
//            viewModel.getDeviceLocation(fusedLocationProviderClient)
        }

        else -> {
            requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        askPermissions()
        updateFcmToken()
        setContent { RentOutAdminApp() }
    }

    private fun updateFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null && !currentUser.isAnonymous) {
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(currentUser.uid)
                    .update("fcmToken", it.result)
            }
        }
    }
}