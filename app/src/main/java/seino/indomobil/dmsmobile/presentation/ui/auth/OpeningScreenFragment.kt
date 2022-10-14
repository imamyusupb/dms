/*
 * *
 *    Created by Seino Developer on 10/13/22, 8:45 AM                           *
 *    Copyright (c) 2022 DevTeam - New DMS Mobile. All rights reserved.           *
 *                                                                                       *
 *    This project and associated documentation files are limited to be used,            *
 *    reproduced, distributed, copied, modified, merged, published, sublicensed,         *
 *    and/or sold to only authorized staff. Should you find yourself is unauthorized,    *
 *    please do not use this project and its associated documentation files in any       *
 *    kind of intentions and conditions.                                                 *
 *                                                                                       *
 *    In order to obtain access to use and involve in this project, you may proceed      *
 *    to inform the authorized staff. By using and involving in this project, you agree  *
 *    to follow our regulations, terms and conditions.                                   *
 *                                                                                       *
 *    This project and source code may use libraries or frameworks that are released     *
 *    under various Open-Source license. Use of those libraries and frameworks are       *
 *    governed by their own individual licenses.                                         *
 *                                                                                       *
 *    The use of this project and source code follows the guideline as described and     *
 *    explained on confluence seinoindomobil.co.id under DMS Mobile New Platform project.*
 *    Please always refer to the project space to follow the guideline.                  *
 *                                                                                       *
 *    If you have any question, please inform our staff or development leader.           *
 *
 */

package seino.indomobil.dmsmobile.presentation.ui.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import seino.indomobil.dmsmobile.R
import seino.indomobil.dmsmobile.databinding.AlertGpsNoActiveDriverBinding
import seino.indomobil.dmsmobile.databinding.FragmentOpeningScreenBinding
import seino.indomobil.dmsmobile.presentation.utils.BaseFragment
import seino.indomobil.dmsmobile.presentation.utils.Constants.PERMISSIONS_CODE

@AndroidEntryPoint
class OpeningScreenFragment : BaseFragment<FragmentOpeningScreenBinding>(
    FragmentOpeningScreenBinding::inflate
) {
    var count = 0
    private var isLocationEnabled: Boolean? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setContent() {
        if (isLocationEnabled == true) {
            findNavController().navigate(R.id.action_openingScreenFragment_to_onBoardSlideFragment)
        } else {
            if (count == 1) {
                popupInformationGps()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        count += 1
        val locationManager =
            requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        Handler(Looper.getMainLooper()).postDelayed({
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (
                    (requireActivity().applicationContext.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) ||
                    (requireActivity().applicationContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) ||
                    (requireActivity().applicationContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) ||
                    (requireActivity().applicationContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) ||
                    (requireActivity().applicationContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
                ) {
                    val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    requestPermissions(permission, PERMISSIONS_CODE)
                } else {
                    setContent()
                }
            } else {
                setContent()
            }
        }, 2000)

    }


    @Suppress("KotlinConstantConditions")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    && (grantResults.isNotEmpty() && grantResults[2] == PackageManager.PERMISSION_GRANTED)
                    && (grantResults.isNotEmpty() && grantResults[3] == PackageManager.PERMISSION_GRANTED)
                    && (grantResults.isNotEmpty() && grantResults[4] == PackageManager.PERMISSION_GRANTED)
                ) {
                    setContent()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Aplikasi memerlukan izin yang diminta!",
                        Toast.LENGTH_SHORT
                    ).show()
                    requireActivity().finish()
                }
            }
        }
    }

    private fun popupInformationGps() {
        var dialogV: AlertDialog? = null
        val dialog = AlertDialog.Builder(requireContext())
        val alertLayout: AlertGpsNoActiveDriverBinding = AlertGpsNoActiveDriverBinding.inflate(
            layoutInflater
        )
        dialog.setView(alertLayout.root)
        val btnOke: AppCompatButton = alertLayout.btnOke
        btnOke.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            dialogV?.dismiss()
            count = 0   //count, flag utk tidak double view popup
        }
        dialogV = dialog.create()
        dialogV.show()
        dialogV.setCancelable(false)
        dialogV.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}