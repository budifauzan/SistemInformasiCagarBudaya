package com.example.sisteminformasicagarbudaya;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class VRActivity extends Activity {
    private WebView webView;
    private String linkVR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v_r);
        initiateViews();
        setViewFromIntent();
        setWebView();
    }

    private void initiateViews() {
        webView = findViewById(R.id.wv_tampilan_vr);
    }

    private void setViewFromIntent() {
        Intent intent = getIntent();
        linkVR = intent.getStringExtra("linkVR");
    }

    private void setWebView() {

        // Supaya bisa menjalankan JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // Supaya tidak keluar dari aplikasi saat mengunjungi URL yang sudah ditentukan
        webView.setWebViewClient(new MyWebViewClient());

        webView.loadUrl(linkVR);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // Kalau URL mengandung budifauzan.github.io, maka akan ditampilkan di webView
            if ("budifauzan.github.io".equals(Uri.parse(url).getHost())) {
                return false;
            }

            // Kalau URL tidak mengandung budifauzan.github.io, maka akan keluar ke detail cagar
           finish();
            return true;
        }
    }
}