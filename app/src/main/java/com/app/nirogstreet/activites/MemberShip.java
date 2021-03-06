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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.MemberShipModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.Event_For_Firebase;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import java.util.ArrayList;

/**
 * Created by Preeti on 07-09-2017.
 */
public class MemberShip extends Activity {
    CircularProgressBar circularProgressBar;
    TextView addQualificationTextView, addTextView;
    MemberShipAdapter memberShipAdapter;
    SesstionManager sesstionManager;
    TextView titileText, skipTextView;
    ImageView backImageView;
    LinearLayout no_list;

    boolean isSkip = false;
    private UserDetailModel userDetailModel;
    RecyclerView recyclerview;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        Event_For_Firebase.getEventCount(MemberShip.this,"Feed_Profile_UserProfile_Membership_Visit");
        if (getIntent().hasExtra("userModel")) {
            userDetailModel = (UserDetailModel) getIntent().getSerializableExtra("userModel");
        }
        no_list = (LinearLayout) findViewById(R.id.no_list);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(MemberShip.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        sesstionManager = new SesstionManager(MemberShip.this);
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
                Intent intent = new Intent(MemberShip.this, Dr_Profile.class);
                intent.putExtra("isSkip", false);
                startActivity(intent);
            }
        });

        titileText = (TextView) findViewById(R.id.title_side_left);
        addQualificationTextView = (TextView) findViewById(R.id.addQualification);
        addQualificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event_For_Firebase.getEventCount(MemberShip.this,"Feed_Profile_UserProfile_Membership_Add_Click");
                Intent intent = new Intent(MemberShip.this, AddOrEditMemberShip.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ApplicationSingleton.isMemberShipUpdated()) {
            userDetailModel = ApplicationSingleton.getUserDetailModel();
        }
        if (userDetailModel != null && userDetailModel.getMemberShipModels() != null && userDetailModel.getMemberShipModels().size() > 0) {
            memberShipAdapter = new MemberShipAdapter(MemberShip.this, userDetailModel.getMemberShipModels(), userDetailModel);
            recyclerview.setAdapter(memberShipAdapter);
            recyclerview.setVisibility(View.VISIBLE);
            no_list.setVisibility(View.GONE);
        } else {
            if (memberShipAdapter != null) {
                memberShipAdapter.notifyItemRemoved(0);
                memberShipAdapter.notifyItemRangeChanged(0, 0);
                recyclerview.setVisibility(View.GONE);

            }
            recyclerview.setVisibility(View.GONE);
            no_list.setVisibility(View.VISIBLE);
        }
    }

    public class MemberShipAdapter extends RecyclerView.Adapter<MemberShipAdapter.MyHolderView> {
        Context context;
        UserDetailModel userDetailModel;
        ArrayList<MemberShipModel> memberShipModels;

        public MemberShipAdapter(Context context, ArrayList<MemberShipModel> memberShipModels, UserDetailModel userDetailModel) {
            this.context = context;
            this.memberShipModels = memberShipModels;
            this.userDetailModel = userDetailModel;
        }

        @Override
        public MemberShipAdapter.MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_item, parent, false);
            return new MyHolderView(v);
        }

        @Override
        public void onBindViewHolder(MemberShipAdapter.MyHolderView holder, final int position) {
            MemberShipModel awardsModel = memberShipModels.get(position);
            holder.clgNameTv.setText(awardsModel.getMembership());
            holder.editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddOrEditMemberShip.class);
                    intent.putExtra("userModel", userDetailModel);
                    intent.putExtra("pos", position);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return memberShipModels.size();
        }

        public class MyHolderView extends RecyclerView.ViewHolder {
            TextView clgNameTv, degreeNameTv, passinYearTv;
            ImageView editImageView;

            public MyHolderView(View itemView) {
                super(itemView);
                clgNameTv = (TextView) itemView.findViewById(R.id.clgName);

                editImageView = (ImageView) itemView.findViewById(R.id.edit_Icon);
            }
        }
    }

}
