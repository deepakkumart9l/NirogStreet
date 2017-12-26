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
import com.app.nirogstreet.model.ClinicDetailModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Preeti on 23-11-2017.
 */

public class AllClinicListing extends  Activity {
    CircularProgressBar circularProgressBar;
    TextView addQualificationTextView, addTextView;
    SesstionManager sesstionManager;
    TextView titileText, skipTextView;
    ImageView backImageView;
    boolean isSkip = false;
    private UserDetailModel userDetailModel;
    RecyclerView recyclerview;
    private LinearLayoutManager linearLayoutManager;
    AllClinicAdapter allClinicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exerience_list);
        userDetailModel = ApplicationSingleton.getUserDetailModel();

        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(AllClinicListing.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        sesstionManager = new SesstionManager(AllClinicListing.this);
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
                Intent intent = new Intent(AllClinicListing.this, Experience.class);
                intent.putExtra("isSkip", true);
                startActivity(intent);
            }
        });

        titileText = (TextView) findViewById(R.id.title_side_left);
        titileText.setText("Clinics ");
        addQualificationTextView = (TextView) findViewById(R.id.addQualification);
        addQualificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllClinicListing.this, AllClinicSearch.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ApplicationSingleton.isClinicUpdated()) {
            userDetailModel = ApplicationSingleton.getUserDetailModel();
        }
        if (userDetailModel != null && userDetailModel.getClinicDetailModels() != null && userDetailModel.getClinicDetailModels().size() > 0) {
            allClinicAdapter = new AllClinicAdapter(AllClinicListing.this, userDetailModel.getClinicDetailModels(), userDetailModel);
            recyclerview.setAdapter(allClinicAdapter);
        } else {
            if (allClinicAdapter != null) {
                allClinicAdapter.notifyItemRemoved(0);
                allClinicAdapter.notifyItemRangeChanged(0, 0);
                recyclerview.setVisibility(View.GONE);

            }
        }

    }

    public class AllClinicAdapter extends RecyclerView.Adapter<AllClinicAdapter.MyHolderView> {
        Context context;
        UserDetailModel userDetailModel;
        ArrayList<ClinicDetailModel> allClinicModelArrayList;

        public AllClinicAdapter(Context context, ArrayList<ClinicDetailModel> allClinicModelArrayList, UserDetailModel userDetailModel) {
            this.context = context;
            this.allClinicModelArrayList = allClinicModelArrayList;
            this.userDetailModel = userDetailModel;
        }

        @Override
        public AllClinicAdapter.MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.clinic_profile_layout_, parent, false);
            return new MyHolderView(v);
        }

        @Override
        public void onBindViewHolder(AllClinicAdapter.MyHolderView holder, final int position) {
            final ClinicDetailModel allClinicModel = allClinicModelArrayList.get(position);
            TypeFaceMethods.setRegularTypeFaceForTextView(holder.clinic_Name, context);
            TypeFaceMethods.setRegularTypeFaceForTextView(holder.services_txt, context);
            TypeFaceMethods.setRegularTypeFaceForTextView(holder.address, context);
            TypeFaceMethods.setRegularTypeFaceForTextView(holder.fees, context);

            TypeFaceMethods.setRegularTypeFaceForTextView(holder.consultaionFees, context);
            holder.clinic_Name.setText(allClinicModel.getName());
            holder.address.setText(allClinicModel.getAddress());
            holder.fees.setText(allClinicModel.getConsultation_fee());
            holder.services_txt.setText(getSelectedNameCsv(allClinicModel));
            /*holder.clgNameTv.setText(allClinicModel.get());
            holder.degreeNameTv.setText(experinceModel.getAddress());
            holder.passinYearTv.setText(experinceModel.getStart_time() + " - " + experinceModel.getEnd_time());
            holder.editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddOrEditExperience.class);
                    intent.putExtra("userModel", userDetailModel);
                    intent.putExtra("pos", position);
                    startActivity(intent);
                }
            });*/
            holder.editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddOrEditClinicDetail.class);
                    intent.putExtra("ClinicModel", (Serializable) allClinicModel);
                    intent.putExtra("pos", position);

                    startActivity(intent);
                    finish();
                }
            });

        }

        @Override
        public int getItemCount() {
            return allClinicModelArrayList.size();
        }

        public String getSelectedNameCsv(ClinicDetailModel clinicDetailModel) {
            String languageCSV = "";

            if (clinicDetailModel.getServicesModels() != null && clinicDetailModel.getServicesModels().size() > 0) {
                for (int i = 0; i < clinicDetailModel.getServicesModels().size(); i++) {
                    String language = clinicDetailModel.getServicesModels().get(i).getSpecializationName();
                    if (language != null && !language.trim().isEmpty()
                            && languageCSV != null && !languageCSV.trim().isEmpty())
                        languageCSV = languageCSV + ", ";
                    languageCSV = languageCSV + language;

                }
            }
            return languageCSV;
        }

        public class MyHolderView extends RecyclerView.ViewHolder {
            TextView clinic_Name, address, services_txt, consultaionFees, fees;
            ImageView editImageView;

            public MyHolderView(View itemView) {
                super(itemView);
                clinic_Name = (TextView) itemView.findViewById(R.id.clinic_Name);
                address = (TextView) itemView.findViewById(R.id.address);
                consultaionFees = (TextView) itemView.findViewById(R.id.consultaionFees);
                fees = (TextView) itemView.findViewById(R.id.fee);
                services_txt = (TextView) itemView.findViewById(R.id.services_txt);
                editImageView = (ImageView) itemView.findViewById(R.id.edit_Icon);
            }
        }
    }

}
