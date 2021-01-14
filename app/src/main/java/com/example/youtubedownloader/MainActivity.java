package com.example.youtubedownloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        mp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link=editText.getText().toString().trim();
                Intent intent=new Intent(MainActivity.this,WebActivity.class);
                intent.putExtra("linkvideo",link);
                startActivity(intent);
            }
        });
        mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link=editText.getText().toString().trim();
                Intent intent=new Intent(MainActivity.this,WebActivity2.class);
                intent.putExtra("linkaudio",link);
                startActivity(intent);
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
}
