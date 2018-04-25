package com.app.nirogstreet.BharamTool;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.listeners.OnItemClickListeners;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by as on 4/21/2018.
 */

public class Herbalsection_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Object tag;
    Context mContext;
    List<Bharam_Model> data;
    SwarnBhoota_Yog_Type fh;
    private OnItemClickListeners onLoadMoreListener;
    int x = 0;
    int classical;

    public Herbalsection_Adapter(Context context, List<Bharam_Model> data, int classical) {
        this.data = data;
        this.mContext = context;
        this.classical = classical;
    }

    public Bharam_Model getItemAt(int position) {
        return data.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View v = layoutInflater.inflate(R.layout.herbalsection_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return data.size();
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
            if (classical == 3) {
                genericViewHolder.txt_engname.setVisibility(View.GONE);
                genericViewHolder.txt_herbsname.setVisibility(View.VISIBLE);
                genericViewHolder.txt_herbsname.setText(item.getName());
            } else {
                genericViewHolder.txt_herbsname.setVisibility(View.GONE);
                genericViewHolder.txt_engname.setText(item.getEngname() + " (" + item.getHindiname() + ")");
            }
            // genericViewHolder.txt_hindiname.setText(item.getHindiname(
            // ));
            if (classical == 3) {
                genericViewHolder.roundimg.setVisibility(View.GONE);
            } else {
                Picasso.with(mContext)
                        .load(item.getThumbnail())
                        .placeholder(R.drawable.default_)
                        .error(R.drawable.default_)
                        .into(genericViewHolder.img_herbs);
            }

            genericViewHolder.relativeLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onLoadMoreListener != null) {
                        if(classical==3){
                            onLoadMoreListener.onItemClick("itemclick", item.getId(), "", item.getName());
                        }else {
                            onLoadMoreListener.onItemClick("itemclick", item.getId(), "", item.getEngname());
                        }
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
        TextView txt_engname, txt_herbsname;
        ImageView img_herbs;
        LinearLayout relativeLayout1;
        RelativeLayout roundimg;

        public MyViewHolder(View view) {
            super(view);
            txt_engname = (TextView) view.findViewById(R.id.txt_engname);
            // txt_hindiname = (TextView) view.findViewById(R.id.txt_hindiname);
            relativeLayout1 = (LinearLayout) view.findViewById(R.id.relativeLayout1);
            img_herbs = (ImageView) view.findViewById(R.id.img_herbs);
            roundimg = (RelativeLayout) view.findViewById(R.id.roundimg);
            txt_herbsname = (TextView) view.findViewById(R.id.txt_herbsname);

        }
    }
}
