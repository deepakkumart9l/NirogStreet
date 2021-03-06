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
import com.app.nirogstreet.activites.EditQualificationDetailOrAddQualificationsDetails;
import com.app.nirogstreet.model.QualificationModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import java.util.ArrayList;

/**
 * Created by Preeti on 25-08-2017.
 */

public class QualificationAdapter extends RecyclerView.Adapter<QualificationAdapter.MyHolderView> {
    Context context;
    UserDetailModel userDetailModel;
    ArrayList<QualificationModel> qualificationModels;

    public QualificationAdapter(Context context, ArrayList<QualificationModel> qualificationModels, UserDetailModel userDetailModel) {
        this.context = context;
        this.qualificationModels = qualificationModels;
        this.userDetailModel = userDetailModel;
    }

    @Override
    public QualificationAdapter.MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.qualification_item, parent, false);
        return new MyHolderView(v);
    }

    @Override
    public void onBindViewHolder(QualificationAdapter.MyHolderView holder, int position) {
        QualificationModel qualificationModel = qualificationModels.get(position);

        holder.clgNameTv.setText(qualificationModel.getClgName());
        holder.degreeNameTv.setText(qualificationModel.getDegreeName());
        holder.passinYearTv.setText(qualificationModel.getPassingYear());
        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, EditQualificationDetailOrAddQualificationsDetails.class);
                intent.putExtra("userModel",userDetailModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return qualificationModels.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        TextView clgNameTv, degreeNameTv, passinYearTv;
        ImageView editImageView;

        public MyHolderView(View itemView) {
            super(itemView);
            clgNameTv = (TextView) itemView.findViewById(R.id.clgName);
            degreeNameTv = (TextView) itemView.findViewById(R.id.degree_name);
            passinYearTv = (TextView) itemView.findViewById(R.id.year_of_passing);
            editImageView = (ImageView) itemView.findViewById(R.id.edit_Icon);
        }
    }
}
