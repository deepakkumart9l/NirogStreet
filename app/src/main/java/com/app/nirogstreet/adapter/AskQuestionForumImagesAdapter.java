package com.app.nirogstreet.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.PostingActivity;
import com.app.nirogstreet.model.AskQuestionImages;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by Preeti on 24-10-2017.
 */
public class AskQuestionForumImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int VIEW_TYPE_LIST = 1;
    public final int VIEW_TYPE_ADD_NEW = 2;
    Context context;
    ArrayList<Object> askQuestionImagesarr;

    public AskQuestionForumImagesAdapter(ArrayList<Object> askQuestionImages, Context context) {
        this.context = context;
        this.askQuestionImagesarr = askQuestionImages;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_ADD_NEW:
                View v1 = inflater.inflate(R.layout.add_image, parent, false);

                viewHolder = new AddNewArtistHolder(v1);
                break;
            case VIEW_TYPE_LIST:
                View v2 = inflater.inflate(R.layout.grid_image_item, parent, false);
                viewHolder = new MyHolderView(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_TYPE_LIST:
                AskQuestionImages askQuestionImages = (AskQuestionImages) askQuestionImagesarr.get(position);
                MyHolderView myViewHolder = (MyHolderView) viewHolder;
                if (!askQuestionImages.isServerImage()) {
                    final int maxSize = 960;
                    int outWidth;
                    int outHeight;
                    int inWidth = BitmapFactory.decodeFile(askQuestionImages.getImages()).getWidth();
                    int inHeight = BitmapFactory.decodeFile(askQuestionImages.getImages()).getHeight();
                    if (inWidth > inHeight) {
                        outWidth = maxSize;
                        outHeight = (inHeight * maxSize) / inWidth;
                    } else {
                        outHeight = maxSize;
                        outWidth = (inWidth * maxSize) / inHeight;
                    }

                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(askQuestionImages.getImages()), outWidth, outHeight, false);
                    ((MyHolderView) viewHolder).imageViewString.setImageBitmap(resizedBitmap);
                } else {
                  //  imageLoader.DisplayImage(context,askQuestionImages.getImages(), myViewHolder.imageViewString, null, 100, 100, R.drawable.default_image);
                    Glide.with(context)
                            .load(askQuestionImages.getImages()) // Uri of the picture
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .crossFade()
                            .override(100, 100)
                            .into(myViewHolder.imageViewString);
                }
                myViewHolder.cancelImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        askQuestionImagesarr.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, askQuestionImagesarr.size());
                    }
                });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (askQuestionImagesarr.get(position) instanceof AskQuestionImages) {
            return VIEW_TYPE_LIST;
        } else {
            return VIEW_TYPE_ADD_NEW;
        }
    }

    @Override
    public int getItemCount() {
        return askQuestionImagesarr.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        ImageView imageViewString, cancelImageView;

        public MyHolderView(View itemView) {
            super(itemView);
            imageViewString = (ImageView) itemView.findViewById(R.id.gallaryimages);
            cancelImageView = (ImageView) itemView.findViewById(R.id.cancel);
        }
    }

    public class AddNewArtistHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public AddNewArtistHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.addImage);
        }
    }

}
