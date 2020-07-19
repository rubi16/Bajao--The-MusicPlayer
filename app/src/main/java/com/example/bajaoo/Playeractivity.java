package com.example.bajaoo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Playeractivity extends AppCompatActivity {
    Button btn_next, btn_previous, btn_pause;
    TextView songTextlabel;
    SeekBar songSeekBar;

    static MediaPlayer mymediaPlayer;
    int positions;
    ArrayList<File> mySongs;
    Thread updateSeekbar;
    String sname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playeractivity);

        btn_next=(Button)findViewById(R.id.next);
        btn_previous=(Button)findViewById(R.id.previous);
        btn_pause=(Button)findViewById(R.id.pause);
        songTextlabel=(TextView)findViewById(R.id.songlabel);
        songSeekBar=(SeekBar)findViewById(R.id.seekbar);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        updateSeekbar= new Thread(){
            @Override
            public void run() {

             int totalDuration =mymediaPlayer.getDuration();
             int currentPosition=0;

             while(currentPosition<totalDuration)
             {
                 try{
                   sleep(500);
                   currentPosition=mymediaPlayer.getCurrentPosition();
                   songSeekBar.setProgress(currentPosition);
                 }
                 catch (InterruptedException e){
                     e.printStackTrace();
                 }
             }

            }
        };

        if(mymediaPlayer!=null)
        {
            mymediaPlayer.stop();
            mymediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle =i.getExtras();

        mySongs =(ArrayList) bundle.getParcelableArrayList("songs");

        sname= mySongs.get(positions).getName().toString();

        String songNmae = i.getStringExtra("songname");

        songTextlabel.setText(songNmae);
        songTextlabel.setSelected(true);

        positions=bundle.getInt("pos",0);

        Uri u = Uri.parse(mySongs.get(positions).toString());

        mymediaPlayer= MediaPlayer.create(getApplicationContext(),u);

        mymediaPlayer.start();
        songSeekBar.setMax(mymediaPlayer.getDuration());

        updateSeekbar.start();

        songSeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        songSeekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);

        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mymediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songSeekBar.setMax(mymediaPlayer.getDuration());

                if (mymediaPlayer.isPlaying()) {
                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    mymediaPlayer.pause();
                } else {
                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    mymediaPlayer.start();
                }

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mymediaPlayer.stop();
                mymediaPlayer.release();
                positions= ((positions+1)%mySongs.size());

                Uri u =Uri.parse(mySongs.get(positions).toString());

                mymediaPlayer=MediaPlayer.create(getApplicationContext(),u);

                sname=mySongs.get(positions).getName().toString();
                songTextlabel.setText(sname);

                mymediaPlayer.start();
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mymediaPlayer.stop();
                mymediaPlayer.release();

                positions=((positions-1)<0)?(mySongs.size()-1):(positions-1);

                Uri u =Uri.parse(mySongs.get(positions).toString());
                mymediaPlayer=MediaPlayer.create(getApplicationContext(),u);

                sname=mySongs.get(positions).getName().toString();
                songTextlabel.setText(sname);

                mymediaPlayer.start();

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}