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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.editText);
        mp4=findViewById(R.id.mp4);
        mp3=findViewById(R.id.mp3);

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