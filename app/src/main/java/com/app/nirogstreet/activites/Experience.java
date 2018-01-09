package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.ExperinceModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import java.util.ArrayList;

/**
 * Created by Preeti on 31-08-2017.
 */

public class Experience extends Activity {
    CircularProgressBar circularProgressBar;
    TextView addQualificationTextView, addTextView;
    SesstionManager sesstionManager;
    TextView titileText, skipTextView;
    ExperienceAdapter experienceAdapter;
    ImageView backImageView;
    boolean isSkip = false;
    private UserDetailModel userDetailModel;
    RecyclerView recyclerview;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exerience_list);
        if (getIntent().hasExtra("userModel")) {
            userDetailModel = (UserDetailModel) getIntent().getSerializableExtra("userModel");
        }
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(Experience.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        sesstionManager = new SesstionManager(Experience.this);
        if (getIntent().hasExtra("isSkip")) {
            isSkip = getIntent().getBooleanExtra("isSkip", false);
        }

        skipTextView = (TextView) findViewById(R.id.skip);
        if (isSkip) {
            skipTextView.setVisibility(View.VISIBLE);
        } else {
            skipTextView.setVisibility(View.GONE);
        }
        skipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Experience.this, Award.class);
                intent.putExtra("isSkip", true);
                startActivity(intent);
            }
        });

        titileText = (TextView) findViewById(R.id.title_side_left);
        addQualificationTextView = (TextView) findViewById(R.id.addQualification);
        addQualificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Experience.this, AddOrEditExperience.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ApplicationSingleton.isExperinceUpdated()) {
            userDetailModel = ApplicationSingleton.getUserDetailModel();
        }
        if (userDetailModel != null && userDetailModel.getExperinceModels() != null && userDetailModel.getExperinceModels().size() > 0) {
            experienceAdapter = new ExperienceAdapter(Experience.this, userDetailModel.getExperinceModels(), userDetailModel);
            recyclerview.setAdapter(experienceAdapter);
        }else {
            if (experienceAdapter != null) {
                experienceAdapter.notifyItemRemoved(0);
                experienceAdapter.notifyItemRangeChanged(0, 0);
                recyclerview.setVisibility(View.GONE);

            }
        }
    }

    public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.MyHolderView> {
        Context context;
        UserDetailModel userDetailModel;
        ArrayList<ExperinceModel> experinceModels;

        public ExperienceAdapter(Context context, ArrayList<ExperinceModel> qualificationModels, UserDetailModel userDetailModel) {
            this.context = context;
            this.experinceModels = qualificationModels;
            this.userDetailModel = userDetailModel;
        }

        @Override
        public ExperienceAdapter.MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.qualification_item, parent, false);
            return new MyHolderView(v);
        }

        @Override
        public void onBindViewHolder(ExperienceAdapter.MyHolderView holder, final int position) {
            ExperinceModel experinceModel = experinceModels.get(position);

            holder.clgNameTv.setText(experinceModel.getOrganizationName());
            holder.degreeNameTv.setText(experinceModel.getAddress());
            if(experinceModel.getEnd_time()==null)
            {
                holder.passinYearTv.setText(experinceModel.getStart_time() + " - " +"Currently Working" );

            }else
            holder.passinYearTv.setText(experinceModel.getStart_time() + " - " + experinceModel.getEnd_time());
            holder.editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddOrEditExperience.class);
                    intent.putExtra("userModel", userDetailModel);
                    intent.putExtra("pos", position);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return experinceModels.size();
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

}
