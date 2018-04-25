package com.app.nirogstreet.activites;

/**
 * Created by as on 3/26/2018.
 */

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.google.android.gms.internal.me;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Audio_Play_Activity extends AppCompatActivity {

    private ImageView iv;
    private MediaPlayer mediaPlayer;

    private double startTime = 0;
    private double finalTime = 0;

    private Handler myHandler = new Handler();
    TextView txt_forward, txt_backward;
    private int forwardTime = 10000;
    private int backwardTime = 10000;
    private SeekBar seekbar;


    public static int oneTimeOnly = 0;
    TextView txt_play, txt_pause, textview_top;
    ImageView backImageView;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv = (ImageView) findViewById(R.id.imageView);
        name = getIntent().getStringExtra("name");
        textview_top = (TextView) findViewById(R.id.textview_top);

        txt_play = (TextView) findViewById(R.id.txt_play);
        txt_pause = (TextView) findViewById(R.id.txt_pause);
        txt_backward = (TextView) findViewById(R.id.txt_backward);
        txt_forward = (TextView) findViewById(R.id.txt_forward);
        if (name != null && name.length() > 0) {
            textview_top.setText(name);
        }


        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource("https://s3-ap-southeast-1.amazonaws.com/nirog/images/journal/1522064926-learning.mp3");
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setClickable(false);

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        txt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_play.setVisibility(View.GONE);
                txt_pause.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                mediaPlayer.start();

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }
                seekbar.setProgress((int) startTime);


            }
        });

        txt_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_play.setVisibility(View.VISIBLE);
                txt_pause.setVisibility(View.GONE);
                //  Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
            }
        });
        txt_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                   // Toast.makeText(getApplicationContext(), "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
                } else {
                   // Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txt_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                   // Toast.makeText(getApplicationContext(), "You have Jumped backward 5 seconds", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        txt_play.setVisibility(View.GONE);
        txt_pause.setVisibility(View.VISIBLE);
        //Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
        mediaPlayer.start();
    }


    @Override
    protected void onStop() {
        super.onStop();
        txt_play.setVisibility(View.VISIBLE);
        txt_pause.setVisibility(View.GONE);
        //  Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
        mediaPlayer.pause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.pause();
    }
}
