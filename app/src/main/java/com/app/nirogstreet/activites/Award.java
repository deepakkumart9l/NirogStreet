package com.app.nirogstreet.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.AwardsModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import java.util.ArrayList;

/**
 * Created by Preeti on 07-09-2017.
 */
public class Award extends AppCompatActivity{
    CircularProgressBar circularProgressBar;
    TextView addQualificationTextView, addTextView;

    SesstionManager sesstionManager;
    TextView titileText, skipTextView;
    ImageView backImageView;
    boolean isSkip = false;
    AwardAdapter awardAdapter;
    private UserDetailModel userDetailModel;
    RecyclerView recyclerview;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onResume() {
        super.onResume();
        if (ApplicationSingleton.isAwardUpdated()) {
            userDetailModel = ApplicationSingleton.getUserDetailModel();
        }
        if (userDetailModel != null && userDetailModel.getAwardsModels() != null && userDetailModel.getAwardsModels().size() > 0) {
            awardAdapter = new AwardAdapter(Award.this, userDetailModel.getAwardsModels(), userDetailModel);
            recyclerview.setAdapter(awardAdapter);
        }else {
            if (awardAdapter != null) {
                awardAdapter.notifyItemRemoved(0);
                awardAdapter.notifyItemRangeChanged(0, 0);
                recyclerview.setVisibility(View.GONE);

            }}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.award_list);
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
        linearLayoutManager = new LinearLayoutManager(Award.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        sesstionManager = new SesstionManager(Award.this);
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
                Intent intent = new Intent(Award.this, MemberShip.class);
                intent.putExtra("isSkip", false);
                startActivity(intent);
            }
        });

        titileText = (TextView) findViewById(R.id.title_side_left);
        addQualificationTextView = (TextView) findViewById(R.id.addQualification);
        addQualificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Award.this, AddOrEditAward.class);
                startActivity(intent);
            }
        });

    }
    public class AwardAdapter extends RecyclerView.Adapter<AwardAdapter.MyHolderView> {
        Context context;
        UserDetailModel userDetailModel;
        ArrayList<AwardsModel> awardsModels;

        public AwardAdapter(Context context, ArrayList<AwardsModel> awardsModels, UserDetailModel userDetailModel) {
            this.context = context;
            this.awardsModels = awardsModels;
            this.userDetailModel = userDetailModel;
        }

        @Override
        public AwardAdapter.MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.qualification_item, parent, false);
            return new MyHolderView(v);
        }

        @Override
        public void onBindViewHolder(AwardAdapter.MyHolderView holder, final int position) {
            AwardsModel awardsModel = awardsModels.get(position);

            holder.clgNameTv.setText(awardsModel.getAwardName());
            holder.degreeNameTv.setVisibility(View.GONE);
            holder.passinYearTv.setText(awardsModel.getYear());
            holder.editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddOrEditAward.class);
                    intent.putExtra("userModel", userDetailModel);
                    intent.putExtra("pos", position);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return awardsModels.size();
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

