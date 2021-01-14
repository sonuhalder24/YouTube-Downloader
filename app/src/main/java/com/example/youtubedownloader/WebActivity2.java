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

public class WebActivity2 extends AppCompatActivity {
    TextView empty_text_mp3;
    WebView webView2;
    String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web2);
        webView2=findViewById(R.id.webView2);
        empty_text_mp3=findViewById(R.id.empty_text2);
        String link=getIntent().getStringExtra("linkaudio");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://immotile-diaries.000webhostapp.com?url="+link)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                empty_text_mp3.setText(R.string.no_internet);
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myResponse=response.body().string();
                    WebActivity2.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            boolean status=false;
                            try{
                                JSONObject baseJsonResponse=new JSONObject(myResponse);
                                status=baseJsonResponse.getBoolean("status");
                                if(status==true) {
                                    JSONArray streamsArray = baseJsonResponse.getJSONArray("streams");
                                    JSONObject firstFeature = streamsArray.getJSONObject(1);
                                    json = firstFeature.getString("url");
                                }
                                else{
                                    Toast.makeText(WebActivity2.this, "Please provide a valid url", Toast.LENGTH_SHORT).show();
                                }

                            }catch(Exception e){
                                e.printStackTrace();
                            }

                            webView2.getSettings().setLoadsImagesAutomatically(true);
                            webView2.getSettings().setJavaScriptEnabled(true);
                            webView2.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                            webView2.setWebViewClient(new WebViewClient());
                            webView2.loadUrl(json);

                            //added
                            WebSettings mywebsettings = webView2.getSettings();
                            mywebsettings.setJavaScriptEnabled(true);

                            webView2.setWebViewClient(new WebViewClient());


                            webView2.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                            webView2.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                            webView2.getSettings().setAppCacheEnabled(true);
                            webView2.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
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

                            webView2.setDownloadListener(new DownloadListener() {
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
                    });
                }
            }
        });
    }
}
