package com.mykara.studio

import android.app.Activity
import android.content.ContentValues
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.webkit.WebViewAssetLoader

/**
 * Bản thử đầu tiên của Mykara Studio: xưởng vẽ (HTML trong assets) chạy trong WebView.
 * Mục đích: cho người dùng thật thử giao diện, vẽ và hoạt hình trên điện thoại Android thật.
 * Phần vẽ bằng Canvas gốc của Android sẽ làm sau khi đo xong độ trễ nét bút của bản này.
 */
class MainActivity : Activity() {
    private lateinit var web: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()
        web = WebView(this)
        web.setBackgroundColor(Color.parseColor("#1E1E1E"))
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.allowFileAccess = false
        web.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? =
                loader.shouldInterceptRequest(request.url)
        }
        web.addJavascriptInterface(Bridge(), "AndroidBridge")
        WebView.setWebContentsDebuggingEnabled(true)
        setContentView(web)
        web.loadUrl("https://appassets.androidplatform.net/assets/index.html")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideBars()
    }

    private fun hideBars() {
        if (Build.VERSION.SDK_INT >= 30) {
            window.insetsController?.let {
                it.hide(WindowInsets.Type.systemBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

    inner class Bridge {
        /** Lưu ảnh PNG (base64) vào thư viện ảnh: Pictures/MykaraStudio. */
        @JavascriptInterface
        fun savePng(base64: String, name: String) {
            try {
                val bytes = Base64.decode(base64, Base64.DEFAULT)
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "studio_${name}_${System.currentTimeMillis()}.png")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MykaraStudio")
                }
                val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    ?: throw IllegalStateException("Không tạo được file ảnh")
                contentResolver.openOutputStream(uri)?.use { it.write(bytes) }
                    ?: throw IllegalStateException("Không ghi được file ảnh")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Đã lưu PNG vào thư viện ảnh (Pictures/MykaraStudio)", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Không lưu được ảnh: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
