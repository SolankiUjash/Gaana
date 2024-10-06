package com.example.gaana;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView noMusicTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        recyclerView = findViewById(R.id.recycler_view);
        noMusicTextView = findViewById(R.id.no_songs_text);

        if(checkPermission() == false){
            requestPermission();
            return;
        }

    }

        boolean checkPermission(){
            int result = ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);
            if(result == PackageManager.PERMISSION_GRANTED){
                return true;
            }
            else{
                return false;
            }
        }
        void requestPermission()
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(MainActivity.this,"READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTTINGS",Toast.LENGTH_SHORT).show();
            }
            else
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
        }
}