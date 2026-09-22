package com.tracelens.app.web

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tracelens.app.ui.WebImageSearchActivity

/**
 * Privacy-first public reverse-image search launcher.
 * No API key, proxy, upload server, database, or analytics is used by TraceLens.
 * The selected search engine receives the image directly because remote visual
 * search cannot work without sending the image to an index provider.
 */
class PublicWebImageSearch(private val context: Context) {
    val isConfigured: Boolean = true

    fun open(uri: Uri, provider: Provider = Provider.GOOGLE_LENS) {
        context.startActivity(
            Intent(context, WebImageSearchActivity::class.java)
                .putExtra(WebImageSearchActivity.EXTRA_IMAGE_URI, uri.toString())
                .putExtra(WebImageSearchActivity.EXTRA_PROVIDER, provider.name)
        )
    }

    enum class Provider(val label: String, val description: String) {
        GOOGLE_LENS("Google Lens", "Visual matches & pages"),
        YANDEX("Yandex Images", "Copies & similar images")
    }

    companion object {
        val providers = Provider.entries.toList()
    }
}
