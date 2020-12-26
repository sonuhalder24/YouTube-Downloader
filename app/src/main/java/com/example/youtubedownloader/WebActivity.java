package com.example.youtubedownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class WebActivity extends AppCompatActivity {
    WebView webView;
    String json;
    TextView empty_text_mp4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView=findViewById(R.id.webView);
        empty_text_mp4=findViewById(R.id.empty_text1);
        String link=getIntent().getStringExtra("linkvideo");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://getvideo.p.rapidapi.com/?url="+link)
                .get()
                .addHeader("x-rapidapi-key","ENTER YOUR OWN RAPIDAPI KEY" )
                .addHeader("x-rapidapi-host", "ENTER YOUR OWN RAPIDAPI HOST")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                empty_text_mp4.setText(R.string.no_internet);
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myResponse=response.body().string();
                    WebActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            boolean status=false;
                            try{

                                JSONObject baseJsonResponse=new JSONObject(myResponse);
                                status=baseJsonResponse.getBoolean("status");
                                if(status==true) {
                                    JSONArray streamsArray = baseJsonResponse.getJSONArray("streams");
                                    JSONObject firstFeature = streamsArray.getJSONObject(0);
                                    json = firstFeature.getString("url");
                                }
                                else{
                                    Toast.makeText(WebActivity.this, "Please provide a valid url", Toast.LENGTH_SHORT).show();
                                }

                            }catch(Exception e){
                                e.printStackTrace();
                            }

                            webView.getSettings().setLoadsImagesAutomatically(true);
                            webView.getSettings().setJavaScriptEnabled(true);
                            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                            webView.setWebViewClient(new WebViewClient());
                            webView.loadUrl(json);

                            //added
                            WebSettings mywebsettings = webView.getSettings();
                            mywebsettings.setJavaScriptEnabled(true);

                            webView.setWebViewClient(new WebViewClient());


                            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                            webView.getSettings().setAppCacheEnabled(true);
                            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                            mywebsettings.setDomStorageEnabled(true);
                            mywebsettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                            mywebsettings.setUseWideViewPort(true);
                            mywebsettings.setSavePassword(true);
                            mywebsettings.setSaveFormData(true);
                            mywebsettings.setEnableSmoothTransition(true);
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
                                    request.setMimeType(mimeType);
                                    String cookies = CookieManager.getInstance().getCookie(url);
                                    request.addRequestHeader("cookie",cookies);
                                    request.addRequestHeader("User-Agent",userAgent);
                                    request.setDescription("Downloading file....");
                                    request.setTitle(URLUtil.guessFileName(url,contentDisposition,mimeType));
                                    request.allowScanningByMediaScanner();
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,URLUtil.guessFileName(url, contentDisposition, mimeType));
                                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    dm.enqueue(request);
                                    Toast.makeText(getApplicationContext(),"Downloading File",Toast.LENGTH_SHORT).show();


                                }
                            });
                        }
                    });
                }
            }
        });
    }

}