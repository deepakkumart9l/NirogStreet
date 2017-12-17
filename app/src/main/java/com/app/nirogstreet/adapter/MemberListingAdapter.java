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
import com.app.nirogstreet.model.SearchModel;
import com.app.nirogstreet.model.UserList;
import com.app.nirogstreet.uttil.SesstionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Preeti on 30-11-2017.
 */

public class MemberListingAdapter extends RecyclerView.Adapter<MemberListingAdapter.ViewHolder> {

    Context context;

    ArrayList<UserList> rowItems;

    SesstionManager sesstionManager;

    public MemberListingAdapter(Context context, ArrayList<UserList> items) {
        this.context = context;
        this.rowItems = items;
        sesstionManager = new SesstionManager(context);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView txtTitle, department;

        public ViewHolder(View itemView) {
            super(itemView);
            department=(TextView)itemView.findViewById(R.id.depart) ;
            txtTitle = (TextView) itemView.findViewById(R.id.name);
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
        final UserList rowItem = rowItems.get(position);
        holder.txtTitle.setText(rowItem.getName());
        //ImageLoader imageLoader=new ImageLoader(context);
        if(position==0)
        {
            holder.department.setVisibility(View.VISIBLE);
        }else {
            holder.department.setVisibility(View.GONE);

        }
        String imgUrl = rowItem.getProfile_pic();
        if (imgUrl != null && !imgUrl.equalsIgnoreCase(""))

            Picasso.with(context)
                    .load(imgUrl)
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(holder.imageView);
        else
            Picasso.with(context)
                    .load(R.drawable.user)
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(holder.imageView);
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
