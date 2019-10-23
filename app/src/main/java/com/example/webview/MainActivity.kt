package com.example.webview

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.inputmethod.BaseInputConnection
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val webView: CustomWebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = MyWebViewClient()
        webView.addJavascriptInterface(WebAppInterface(this), "Android")

        // webView.loadUrl("http://10.0.2.2:5000/")
        webView.loadUrl("file:///android_asset/index.html")

    }

}

class CustomWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {
    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        val inputConnection = BaseInputConnection(this, false)
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE
        outAttrs.inputType = EditorInfo.TYPE_CLASS_NUMBER
        return inputConnection
    }
}

class WebAppInterface(private val mContext: Context) {

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
    }
}

private class MyWebViewClient : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        if (request.url.host == "example.com") {
            // This is my web site, so do not override; let my WebView load the page
            return false
        }

        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
        Intent(Intent.ACTION_VIEW, request.url).apply {
            startActivity(view.context,this, null)
        }
        return true
    }
}
