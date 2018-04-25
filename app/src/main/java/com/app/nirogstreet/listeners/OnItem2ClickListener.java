package com.app.nirogstreet.listeners;

import android.widget.ImageView;

import com.app.nirogstreet.model.FeedModel;

import java.util.ArrayList;

/**
 * Created by as on 3/22/2018.
 */

public interface OnItem2ClickListener {
    public void onItemClick(ImageView v, ArrayList<FeedModel> feedModels,int position);
}
