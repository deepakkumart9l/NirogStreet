package com.app.nirogstreet.activites;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.QualificationAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.QualificationModel;
import com.app.nirogstreet.model.SpecializationModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by Preeti on 25-08-2017.
 */

public class Dr_Qualifications extends AppCompatActivity {
    CircularProgressBar circularProgressBar;
    TextView addQualificationTextView, addTextView;
    SesstionManager sesstionManager;
    TextView titileText, skipTextView;
    ImageView backImageView;
    boolean isSkip = false;
    private UserDetailModel userDetailModel;
    RecyclerView recyclerview;
    private LinearLayoutManager linearLayoutManager;
    QualificationAdapter qualificationAdapter;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra("userModel")) {
            userDetailModel = (UserDetailModel) getIntent().getSerializableExtra("userModel");
        }
        setContentView(R.layout.qualifications);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(Dr_Qualifications.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        sesstionManager = new SesstionManager(Dr_Qualifications.this);
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
                Intent intent = new Intent(Dr_Qualifications.this, RegistrationAndDocuments.class);
                intent.putExtra("isSkip", true);
                startActivity(intent);
            }
        });
        TypeFaceMethods.setRegularTypeFaceForTextView(skipTextView, Dr_Qualifications.this);

        titileText = (TextView) findViewById(R.id.title_side_left);
        TypeFaceMethods.setRegularTypeFaceForTextView(titileText, Dr_Qualifications.this);
        addQualificationTextView = (TextView) findViewById(R.id.addQualification);
        TypeFaceMethods.setRegularTypeFaceForTextView(addQualificationTextView, Dr_Qualifications.this);
        addQualificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dr_Qualifications.this, EditQualificationDetailOrAddQualificationsDetails.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ApplicationSingleton.isQualificationUpdated()) {
            userDetailModel = ApplicationSingleton.getUserDetailModel();
        }
        if (userDetailModel != null && userDetailModel.getQualificationModels() != null && userDetailModel.getQualificationModels().size() > 0) {
            qualificationAdapter = new QualificationAdapter(Dr_Qualifications.this, userDetailModel.getQualificationModels(), userDetailModel);
            recyclerview.setAdapter(qualificationAdapter);
            recyclerview.setVisibility(View.VISIBLE);
        } else {
            if (qualificationAdapter != null) {
                qualificationAdapter.notifyItemRemoved(0);
                qualificationAdapter.notifyItemRangeChanged(0, 0);
                recyclerview.setVisibility(View.GONE);

            }
        }

    }

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
            return new QualificationAdapter.MyHolderView(v);
        }

        @Override
        public void onBindViewHolder(QualificationAdapter.MyHolderView holder, final int position) {
            QualificationModel qualificationModel = qualificationModels.get(position);
            TypeFaceMethods.setRegularTypeFaceForTextView(holder.degreeNameTv, context);
            TypeFaceMethods.setRegularTypeFaceForTextView(holder.clgNameTv, context);
            TypeFaceMethods.setRegularTypeFaceForTextView(holder.passinYearTv, context);
            holder.clgNameTv.setText(qualificationModel.getDegreeName());
            holder.degreeNameTv.setText(qualificationModel.getClgName());
            holder.passinYearTv.setText(qualificationModel.getPassingYear());
            holder.editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditQualificationDetailOrAddQualificationsDetails.class);
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
