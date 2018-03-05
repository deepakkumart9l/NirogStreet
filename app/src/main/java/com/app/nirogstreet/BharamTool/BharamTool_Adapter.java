package com.app.nirogstreet.BharamTool;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.listeners.OnItemClickListeners;

import java.util.List;

/**
 * Created by as on 2/22/2018.
 */

public class BharamTool_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Object tag;
    Context mContext;
    List<Bharam_Model> data;
    SwarnBhoota_Yog_Type fh;
    private OnItemClickListeners onLoadMoreListener;
    int x = 0;

    public BharamTool_Adapter(Context context, List<Bharam_Model> data) {
        this.data = data;
        this.mContext = context;
    }

    public Bharam_Model getItemAt(int position) {
        return data.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View v = layoutInflater.inflate(R.layout.swarnbhoota_yog_view, parent, false);
        return new BharamTool_Adapter.MyViewHolder(v);
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

        final BharamTool_Adapter.MyViewHolder genericViewHolder = (BharamTool_Adapter.MyViewHolder) holder;
        final Bharam_Model item = data.get(position);
        try {
            genericViewHolder.swarn_txt.setText(item.getName());
            genericViewHolder.swanbhootattype_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onItemClick("itemclick", item.getId(),item.getTotal_comment(),item.getName());
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
        TextView swarn_txt;
        LinearLayout swanbhootattype_layout;

        public MyViewHolder(View view) {
            super(view);
            swarn_txt = (TextView) view.findViewById(R.id.swarn_txt);
            swanbhootattype_layout=(LinearLayout)view.findViewById(R.id.swanbhootattype_layout);

        }
    }
}
