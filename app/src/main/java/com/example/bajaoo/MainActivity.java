package com.example.bajaoo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static android.os.Environment.DIRECTORY_MUSIC;

public class MainActivity extends AppCompatActivity {


    ListView ListView_songs;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView_songs= (ListView) findViewById(R.id.mySongListview);

        runtimePermission();
    }

    public void runtimePermission(){
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    public ArrayList<File> findsong(File file){
        ArrayList<File> arraylist = new ArrayList<>();
        File[] files=file.listFiles();
        for(File singlefile: files){
           if(singlefile.isDirectory() && !singlefile.isHidden()){
               arraylist.addAll(findsong(singlefile));
           }
           else{
               if(singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")){
                   arraylist.add(singlefile);
               }
           }

        }
        return arraylist;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    void display(){
        final ArrayList<File> mySongs = findsong(Environment.getExternalStorageDirectory());

        items= new String[mySongs.size()];

        for(int i=0; i<mySongs.size();i++) {
            items[i] = mySongs.get(i).getName().toString().replace("mp3", "").replace(".wav", "");
        }

        ArrayAdapter<String> myadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        ListView_songs.setAdapter(myadapter);

        ListView_songs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String songName= ListView_songs.getItemAtPosition(i).toString();

                startActivity(new Intent(getApplicationContext(),Playeractivity.class)
                .putExtra("songs",mySongs).putExtra("songname",songName).putExtra("pos",i));

            }
        });

    }


}