package com.app.nirogstreet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.nirogstreet.BharamTool.Bharam_Model;
import com.app.nirogstreet.BharamTool.SwarnBhoota_Yog_Type;
import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.Journals;
import com.app.nirogstreet.listeners.OnItemClickListeners;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by as on 3/16/2018.
 */

public class Journals_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Object tag;
    Context mContext;
    List<Bharam_Model> data;
    Journals fh;
    OnItemClickListeners onLoadMoreListener;
    int x = 0;

    public Journals_Adapter(Context context, List<Bharam_Model> data) {
        this.data = data;
        this.mContext = context;
    }

    public Bharam_Model getItemAt(int position) {
        return data.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View v = layoutInflater.inflate(R.layout.journal_adapter_item, parent, false);
        return new MyViewHolder(v);
    }


    public void setData(List<Bharam_Model> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListeners onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final MyViewHolder genericViewHolder = (MyViewHolder) holder;
        final Bharam_Model item = data.get(position);
        try {
            genericViewHolder.description.setText(item.getName());

            Glide.with(mContext)
                    .load(item.getThumbnail()).placeholder(R.drawable.default_).centerCrop() // Uri of the picture
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .crossFade()
                    .override(100, 100)
                    .into(((MyViewHolder) holder).groupImage);

            genericViewHolder.relativeLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onItemClick("itemclick", 0,item.getDetail(), item.getName());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getTag() {
        return tag;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        TextView description;
        LinearLayout relativeLayout1;
        RoundedImageView groupImage;

        public MyViewHolder(View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.description);
            relativeLayout1 = (LinearLayout) view.findViewById(R.id.relativeLayout1);
            groupImage = (RoundedImageView) itemView.findViewById(R.id.groupImage);
        }
    }
}
