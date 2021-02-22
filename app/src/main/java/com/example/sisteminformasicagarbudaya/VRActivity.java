package com.example.sisteminformasicagarbudaya;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class VRActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v_r);

        webView = findViewById(R.id.wv_tampilan_vr);
        webView.loadUrl("https://odisuklam.github.io/pages/candi-badut/sisi-kanan-dalam.html");

        // Supaya bisa menjalankan JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Supaya tidak keluar dari aplikasi saat mengunjungi URL yang sudah ditentukan
        webView.setWebViewClient(new MyWebViewClient());
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if ("odisuklam.github.io".equals(Uri.parse(url).getHost())) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            startActivity(new Intent(VRActivity.this, MainActivity.class));
            return true;
        }
    }
}