package com.tracelens.app.ui

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.tracelens.app.web.PublicWebImageSearch
import java.io.ByteArrayOutputStream
import java.util.Base64

/**
 * Hosts the official consumer reverse-image-search page in a WebView and submits
 * the selected image directly to that provider. TraceLens never receives the
 * image back and never stores it remotely.
 */
class WebImageSearchActivity : Activity() {
    companion object {
        const val EXTRA_IMAGE_URI = "image_uri"
        const val EXTRA_PROVIDER = "provider"
    }

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
        }
        setContentView(webView)

        val uri = intent.getStringExtra(EXTRA_IMAGE_URI)?.let(Uri::parse)
        val provider = runCatching {
            PublicWebImageSearch.Provider.valueOf(intent.getStringExtra(EXTRA_PROVIDER) ?: "GOOGLE_LENS")
        }.getOrDefault(PublicWebImageSearch.Provider.GOOGLE_LENS)

        if (uri == null) {
            Toast.makeText(this, "Gambar tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        try {
            val jpeg = encodeImage(uri)
            val base64 = Base64.getEncoder().encodeToString(jpeg)
            val html = when (provider) {
                PublicWebImageSearch.Provider.GOOGLE_LENS -> googleLensHtml(base64)
                PublicWebImageSearch.Provider.YANDEX -> yandexHtml(base64)
            }
            webView.loadDataWithBaseURL(
                "https://lens.google.com/",
                html,
                "text/html",
                "UTF-8",
                null,
            )
        } catch (e: Exception) {
            Toast.makeText(this, e.message ?: "Gagal menyiapkan gambar", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun encodeImage(uri: Uri): ByteArray {
        val source = contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) }
            ?: error("Gambar tidak bisa dibaca")
        val maxSide = 1400
        val scale = minOf(1f, maxSide.toFloat() / maxOf(source.width, source.height))
        val bitmap = if (scale < 1f) Bitmap.createScaledBitmap(source, (source.width * scale).toInt(), (source.height * scale).toInt(), true) else source
        return try {
            ByteArrayOutputStream().use { out ->
                check(bitmap.compress(Bitmap.CompressFormat.JPEG, 88, out)) { "Gagal menyiapkan gambar" }
                out.toByteArray()
            }
        } finally {
            if (bitmap !== source) bitmap.recycle()
            source.recycle()
        }
    }

    private fun googleLensHtml(base64: String): String = """
        <!doctype html><html><head><meta name="viewport" content="width=device-width,initial-scale=1"></head>
        <body><p>Opening Google Lens…</p>
        <script>
        (async()=>{
          const b64='${base64}';
          const bytes=Uint8Array.from(atob(b64),c=>c.charCodeAt(0));
          const file=new File([bytes],'tracelens.jpg',{type:'image/jpeg'});
          const form=document.createElement('form');
          form.method='POST'; form.enctype='multipart/form-data';
          form.action='https://lens.google.com/v3/upload?ep=fntpubb&st='+Date.now();
          const input=document.createElement('input'); input.type='file'; input.name='encoded_image';
          const dt=new DataTransfer(); dt.items.add(file); input.files=dt.files;
          const dim=document.createElement('input'); dim.name='processed_image_dimensions'; dim.value='1400,1400';
          form.append(input,dim); document.body.appendChild(form); form.submit();
        })();
        </script></body></html>
    """.trimIndent()

    private fun yandexHtml(base64: String): String = """
        <!doctype html><html><head><meta name="viewport" content="width=device-width,initial-scale=1"></head>
        <body><p>Opening Yandex Images…</p>
        <script>
        (async()=>{
          const b64='${base64}';
          const bytes=Uint8Array.from(atob(b64),c=>c.charCodeAt(0));
          const file=new File([bytes],'tracelens.jpg',{type:'image/jpeg'});
          const form=document.createElement('form');
          form.method='POST'; form.enctype='multipart/form-data';
          form.action='https://yandex.com/images/search?rpt=imageview';
          const input=document.createElement('input'); input.type='file'; input.name='upfile';
          const dt=new DataTransfer(); dt.items.add(file); input.files=dt.files;
          form.appendChild(input); document.body.appendChild(form); form.submit();
        })();
        </script></body></html>
    """.trimIndent()

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}
