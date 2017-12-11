package com.app.nirogstreet.activites;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.app.nirogstreet.R;

/**
 * Created by Preeti on 11-12-2017.
 */

public class Module_Detail_Activity extends Activity {
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail);
        videoView = (VideoView) findViewById(R.id.VideoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        try {
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            videoView.setLayoutParams(new FrameLayout.LayoutParams(width,height));

        } catch (Exception e) {
            e.printStackTrace();
        }
        videoView.setVideoPath("https://s3-ap-southeast-1.amazonaws.com/nirog/videos/1512636856-VID-20171117-WA0000-0.mp4");
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                Log.i("TAG", "Duration = " +
                        videoView.getDuration());
                videoView.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // not playVideo
                // playVideo();

                mp.start();
            }
        });


    }
}
