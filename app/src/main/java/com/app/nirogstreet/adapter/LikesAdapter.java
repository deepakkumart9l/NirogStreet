package com.app.nirogstreet.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.Dr_Profile;
import com.app.nirogstreet.model.LikesModel;
import com.app.nirogstreet.uttil.ImageLoader;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Preeti on 27-10-2017.
 */
public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ViewHolder> {
    Context context;
    ArrayList<LikesModel> likesModels;

    public LikesAdapter(Context context, ArrayList<LikesModel> likesModels) {
        this.context = context;
        this.likesModels = likesModels;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View v = layoutInflater.inflate(R.layout.like_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        RecyclerView.ViewHolder genericViewHolder = (RecyclerView.ViewHolder) holder;
        final LikesModel rowItem = likesModels.get(position);
        if (rowItem.getFname() != null) {
            if (rowItem.getLname() != null)
                holder.name.setText("Dr. "+rowItem.getFname() + " " + rowItem.getLname());
            else
                holder.name.setText("Dr. "+rowItem.getFname());
        }
        ((RecyclerView.ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent(context, Dr_Profile.class);
                LikesModel likesModel = likesModels.get(position);
                resultIntent.putExtra("UserId", likesModel.getUserId());
                context.    startActivity(resultIntent);
            }
        });
        //  holder.name.setText(rowItem.getName());
        ImageLoader imageLoader = new ImageLoader(context);

        Picasso.with(context)
                .load(rowItem.getUserProfile_pic())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(holder.imageView);
        TypeFaceMethods.setRegularTypeFaceForTextView(holder.name,context);
    }


    @Override
    public int getItemCount() {
        return likesModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        CircleImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);

            imageView = (CircleImageView) itemView.findViewById(R.id.profileImage);
        }
    }
}
