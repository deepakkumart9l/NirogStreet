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
import com.app.nirogstreet.activites.SearchActivity;
import com.app.nirogstreet.model.SearchModel;
import com.app.nirogstreet.uttil.SesstionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Preeti on 28-10-2017.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    Context context;
    SesstionManager sesstionManager;
    ArrayList<SearchModel> rowItems;


    public SearchAdapter(Context context, ArrayList<SearchModel> items) {
        this.context = context;
        this.rowItems = items;
        sesstionManager = new SesstionManager(context);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView txtTitle, department;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.name);
            department = (TextView) itemView.findViewById(R.id.depart);
            imageView = (CircleImageView) itemView.findViewById(R.id.img);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View v = layoutInflater.inflate(R.layout.search_item_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SearchModel rowItem = rowItems.get(position);
        holder.txtTitle.setText(rowItem.getFname().trim() + " " + rowItem.getLname());
        holder.department.setText(rowItem.getDeprtment());
        //ImageLoader imageLoader=new ImageLoader(context);
        String imgUrl = rowItem.getProfileimage();
        if (imgUrl != null && !imgUrl.equalsIgnoreCase(""))
            Glide.with(context)
                    .load(imgUrl) .placeholder(R.drawable.user)// Uri of the picture
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .crossFade()
                    .override(100, 100)
                    .into(holder.imageView);
        else {
            Glide.with(context)
                    .load(R.drawable.user) // Uri of the picture
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .crossFade()
                    .override(100, 100)
                    .into(holder.imageView);
        }
        // imageLoader.DisplayImage(context,imgUrl,holder.imageView,null,150,150,R.drawable.profile_default);
        ((RecyclerView.ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Dr_Profile.class);
                if (!rowItem.getId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID)))

                    intent.putExtra("UserId", rowItem.getId());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return rowItems.size();
    }
}