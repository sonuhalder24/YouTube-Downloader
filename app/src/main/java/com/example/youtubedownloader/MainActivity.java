package com.example.youtubedownloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button mp4,mp3;
    RelativeLayout relativeLayout;
    TextView textView;
    ImageView headphone;
    CharSequence charSequence;
    int index;
    long delay=200;
    Handler handler=new Handler();
    String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        relativeLayout=findViewById(R.id.relativeLay);
        textView=findViewById(R.id.text_view);
        headphone=findViewById(R.id.headphone);
        editText=findViewById(R.id.editText);
        mp4=findViewById(R.id.mp4);
        mp3=findViewById(R.id.mp3);

        serverInitialize();

        ObjectAnimator objectAnimator=ObjectAnimator.ofPropertyValuesHolder(
                headphone,
                PropertyValuesHolder.ofFloat("scaleX",1.2f),
                PropertyValuesHolder.ofFloat("scaleY",1.2f)
        );
        //Set Animation
        objectAnimator.setDuration(500);
        //set Repeat
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        //set Repeat mode
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        //Start animation
        objectAnimator.start();
        animateText("YouTube Downloader");


        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.GONE);
                getSupportActionBar().show();
            }
        });
        //go MP4 Activity
        mp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().trim().isEmpty() &&
                        editText.getText().toString().trim().matches(pattern))
                {
                    String link = editText.getText().toString().trim();
                    Intent intent = new Intent(MainActivity.this, WebActivity.class);
                    intent.putExtra("linkvideo", link);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "Please provide a valid url", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //go MP3 Activity
        mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().trim().isEmpty() &&
                        editText.getText().toString().trim().matches(pattern))
                {
                    String link = editText.getText().toString().trim();
                    Intent intent = new Intent(MainActivity.this, WebActivity2.class);
                    intent.putExtra("linkaudio", link);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Please provide a valid url", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            textView.setText(charSequence.subSequence(0,index++));
            if(index<=charSequence.length()){
                //when index is equal to text length
                //Run handler
                handler.postDelayed(runnable,delay);
            }
        }
    };
    //Create animated text method
    public void animateText(CharSequence cs){
        charSequence=cs;
        index=0;
        textView.setText("");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,delay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                Intent intent=new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void serverInitialize() {
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        String url="ENTER YOUR HEROKU SERVER LINK";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

}