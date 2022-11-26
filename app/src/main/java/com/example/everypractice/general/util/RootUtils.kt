package com.example.everypractice.general.util

import android.os.Build

//in Flutter
fun isEmulator(): Boolean {
    return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
            || Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.HARDWARE.contains("goldfish")
            || Build.HARDWARE.contains("ranchu")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.PRODUCT.contains("sdk_google")
            || Build.PRODUCT.contains("google_sdk")
            || Build.PRODUCT.contains("sdk")
            || Build.PRODUCT.contains("sdk_x86")
            || Build.PRODUCT.contains("sdk_gphone64_arm64")
            || Build.PRODUCT.contains("vbox86p")
            || Build.PRODUCT.contains("emulator")
            || Build.PRODUCT.contains("simulator"))
}

/*
*
* override fun onPause() {
        super.onPause()
        Timber.d("ON PAUSE")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("ON PAUSE")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("ON DESTROY")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("ON DESTROY VIEW")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("ON DETACH")
    }

*
* */