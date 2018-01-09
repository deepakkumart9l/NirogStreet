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
import com.app.nirogstreet.model.RegistrationAndDocumenModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import java.util.ArrayList;

/**
 * Created by Preeti on 28-08-2017.
 */

public class RegistrationAndDocuments extends Activity {
    CircularProgressBar circularProgressBar;
    TextView addQualificationTextView, addTextView;
    SesstionManager sesstionManager;
    TextView titileText, skipTextView;
    RegistrationAdapter registrationAdapter;
    boolean isSkip = false;
    RecyclerView recyclerview;
    ImageView backImageView;
    private LinearLayoutManager linearLayoutManager;
    private UserDetailModel userDetailModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_documents);
        backImageView=(ImageView)findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sesstionManager = new SesstionManager(RegistrationAndDocuments.this);
        if (getIntent().hasExtra("isSkip")) {
            isSkip = getIntent().getBooleanExtra("isSkip", false);
        }
        if (getIntent().hasExtra("userModel")) {
            userDetailModel = (UserDetailModel) getIntent().getSerializableExtra("userModel");
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
Intent intent=new Intent(RegistrationAndDocuments.this,AllClinicListing.class);
                intent.putExtra("isSkip",true);
                startActivity(intent);

            }
        });
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(RegistrationAndDocuments.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);

        titileText = (TextView) findViewById(R.id.title_side_left);
        addQualificationTextView = (TextView) findViewById(R.id.addRegistration);
        addQualificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationAndDocuments.this, EditRegistrationAndDocuments.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ApplicationSingleton.isRegistrationUpdated()) {
            userDetailModel = ApplicationSingleton.getUserDetailModel();
        }
        if (userDetailModel != null && userDetailModel.getRegistrationAndDocumenModels() != null && userDetailModel.getRegistrationAndDocumenModels().size() > 0) {
            registrationAdapter = new RegistrationAdapter(RegistrationAndDocuments.this, userDetailModel.getRegistrationAndDocumenModels(), userDetailModel);
            recyclerview.setAdapter(registrationAdapter);
        }
        else {
            if (registrationAdapter != null) {
                registrationAdapter.notifyItemRemoved(0);
                registrationAdapter.notifyItemRangeChanged(0, 0);
                recyclerview.setVisibility(View.GONE);

            }
        }
    }

    public class RegistrationAdapter extends RecyclerView.Adapter<RegistrationAdapter.MyHolderView> {
        Context context;
        ArrayList<RegistrationAndDocumenModel> qualificationModels;
        UserDetailModel userDetailModel;

        public RegistrationAdapter(Context context, ArrayList<RegistrationAndDocumenModel> qualificationModels, UserDetailModel userDetailModel) {
            this.context = context;
            this.qualificationModels = qualificationModels;
            this.userDetailModel = userDetailModel;

        }

        @Override
        public RegistrationAdapter.MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.qualification_item, parent, false);
            return new RegistrationAdapter.MyHolderView(v);
        }

        @Override
        public void onBindViewHolder(RegistrationAdapter.MyHolderView holder, final int position) {
            RegistrationAndDocumenModel qualificationModel = qualificationModels.get(position);

            holder.clgNameTv.setText(qualificationModel.getCouncil_registration_number());
            holder.degreeNameTv.setText(qualificationModel.getCouncil_name());
            holder.passinYearTv.setText(qualificationModel.getCouncil_year());
            holder.editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditRegistrationAndDocuments.class);
                    intent.putExtra("userModel", userDetailModel);
                    intent.putExtra("pos", position);
                    startActivity(intent);
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

}
