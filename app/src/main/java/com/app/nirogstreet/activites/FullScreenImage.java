package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.nirogstreet.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Preeti on 07-11-2017.
 */
public class FullScreenImage extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullimagesingle);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
        ImageView imagecancel = (ImageView) findViewById(R.id.cancel);
        imagecancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
        ImageView imageView = (ImageView) findViewById(R.id.img);
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("ImageUrl");
        String extension = imageUrl.substring(imageUrl.lastIndexOf("."));

        if (extension.equalsIgnoreCase(".gif")) {
            Glide.with(FullScreenImage.this)
                    .load(imageUrl)

                    .asGif(). diskCacheStrategy(DiskCacheStrategy.SOURCE)

                    .into(imageView);
        } else
            // imageLoader.displayImage(imageUrl, imageView, defaultOptions);
        Glide.with(FullScreenImage.this)
                .load(imageUrl) // Uri of the picture
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .crossFade()
                .override(100, 100)
                .into( imageView);



    }
}

