package com.example.youtubedownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class WebActivity2 extends AppCompatActivity {
    TextView empty_text_mp3;
    WebView webView;
    LottieAnimationView loadingAnim2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web2);
        webView=findViewById(R.id.webView2);
        empty_text_mp3=findViewById(R.id.empty_text2);
        loadingAnim2=findViewById(R.id.loading2);
        String link=getIntent().getStringExtra("linkaudio");

        //API call using Volley

        loadingAnim2.setVisibility(View.VISIBLE);
        RequestQueue requestQueue= Volley.newRequestQueue(WebActivity2.this);
        String url="ENTER YOUR HEROKU SERVER LINK"+link;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                webView.setWebViewClient(new WebViewClient());
                loadingAnim2.setVisibility(View.GONE);
                webView.loadUrl(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingAnim2.setVisibility(View.GONE);
                empty_text_mp3.setText(R.string.problem);

            }
        });
        requestQueue.add(stringRequest);

        //External storage permission for saving file

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

                Log.d("permission","permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,1);
            }
        }

        //handle downloading

        webView.setDownloadListener(new DownloadListener() {
            @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setMimeType("audio/mpeg");
                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie",cookies);
                    request.addRequestHeader("User-Agent",userAgent);
                    request.setDescription("Downloading file....");
                    request.setTitle(URLUtil.guessFileName(url,contentDisposition,"audio/mpeg"));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,URLUtil.guessFileName(url, contentDisposition, "audio/mpeg"));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(),"Downloading File",Toast.LENGTH_SHORT).show();

            }
        });
    }

}