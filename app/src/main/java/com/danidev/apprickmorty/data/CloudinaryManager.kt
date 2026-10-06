package com.danidev.apprickmorty.data

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

object CloudinaryManager {

    fun init(context: Context) {
        try {
            val config = mapOf("cloud_name" to "rguvtc0u")
            MediaManager.init(context, config)
        } catch (_: Exception) {
            // Ya inicializado previamente
        }
    }

    fun subirFoto(uri: Uri, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        MediaManager.get().upload(uri)
            .unsigned("ml_default_unsigned")
            .callback(object : UploadCallback {
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"].toString()
                    onSuccess(url)
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    onError(error.description)
                }

                override fun onStart(requestId: String) {}
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                override fun onReschedule(requestId: String, error: ErrorInfo) {}
            }).dispatch()
    }
}