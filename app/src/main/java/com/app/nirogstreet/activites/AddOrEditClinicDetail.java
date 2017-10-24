package com.app.nirogstreet.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.model.SpecializationModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import java.util.ArrayList;

/**
 * Created by Preeti on 08-09-2017.
 */

public class AddOrEditClinicDetail extends AppCompatActivity {
    private static final int RESULT_CODE_LOCATION = 2;
    UserDetailModel userDetailModel;
    ArrayList<SpecializationModel> multipleSelectedItemModels = new ArrayList<>();

    EditText addressEt, clinicName, city, pincode, feeEt,Services_nameEt,countryEt;
    ArrayList<SpecializationModel> servicesMultipleSelectedModels = new ArrayList<>();

    private static final int RESULT_CODE = 1;

    ImageView backImageView;
    private int position=-1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_or_add_clinic);
        userDetailModel = ApplicationSingleton.getUserDetailModel();
        addressEt = (EditText) findViewById(R.id.address);
        countryEt = (EditText) findViewById(R.id.country);
        Services_nameEt = (EditText) findViewById(R.id.Services_name);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clinicName = (EditText) findViewById(R.id.clinicName);
        city = (EditText) findViewById(R.id.city);
        pincode = (EditText) findViewById(R.id.pincode);
        feeEt = (EditText) findViewById(R.id.feeEt);
        TypeFaceMethods.setRegularTypeFaceEditText(feeEt, AddOrEditClinicDetail.this);
        TypeFaceMethods.setRegularTypeFaceEditText(Services_nameEt, AddOrEditClinicDetail.this);
        TypeFaceMethods.setRegularTypeFaceEditText(countryEt, AddOrEditClinicDetail.this);

        TypeFaceMethods.setRegularTypeFaceEditText(clinicName, AddOrEditClinicDetail.this);
        TypeFaceMethods.setRegularTypeFaceEditText(city, AddOrEditClinicDetail.this);
        TypeFaceMethods.setRegularTypeFaceEditText(pincode, AddOrEditClinicDetail.this);


        TypeFaceMethods.setRegularTypeFaceEditText(addressEt, AddOrEditClinicDetail.this);

        addressEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddOrEditClinicDetail.this, SearchLocationCity.class);
                startActivityForResult(intent, RESULT_CODE_LOCATION);

            }
        });
        if (getIntent().hasExtra("pos")) {
            position = getIntent().getIntExtra("pos", -1);
            if (position != -1) {
                if (userDetailModel.getClinicDetailModels() != null && userDetailModel.getClinicDetailModels().size() > 0) {
                    multipleSelectedItemModels = userDetailModel.getClinicDetailModels().get(position).getServicesModels();

                }

            }}
            Services_nameEt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Intent intent = new Intent(AddOrEditClinicDetail.this, Multi_Select_Search_specialization.class);
                    intent.putExtra("isService", true);

                    if (multipleSelectedItemModels != null && multipleSelectedItemModels.size() > 0)
                        intent.putExtra("list", servicesMultipleSelectedModels);

                    else if (userDetailModel.getClinicDetailModels().get(1) != null && userDetailModel.getClinicDetailModels().get(1).getServicesModels().size() > 0)
                        intent.putExtra("list", userDetailModel.getSpecializationModels());

                    startActivityForResult(intent, RESULT_CODE);

                    return false;
                }
            });

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_CODE_LOCATION) {

                if (data != null) {
                    String address=data.getStringExtra("address");
                    String location = data.getStringExtra("city");
                    String pincodes = null;
                    String country=null;
                    if(data.getStringExtra("pincode")!=null)
                    {
                        pincodes=data.getStringExtra("pincode");
                    }
                    String longitude, latitude = null;
                    if (data.hasExtra("longitude")) ;
                    {
                        longitude = data.getStringExtra("longitude");
                    }
                    if (data.hasExtra("latitude")) {
                        latitude = data.getStringExtra("latitude");
                    }
                    if(data.hasExtra("country")&&data.getStringExtra("country")!=null)
                    {
                        country=data.getStringExtra("country");
                       countryEt.setText(country);
                    }
                    city.setText(location);
                    addressEt.setText(address);
                    pincode.setText(pincodes);

                }
            }
            if (requestCode == RESULT_CODE) {
                if (data != null) {

                        String s = data.getStringExtra("friendsCsv");
                        Services_nameEt.setText(s);
                        System.out.print(s);
                        servicesMultipleSelectedModels = (ArrayList<SpecializationModel>) data.getSerializableExtra("list");
                    }
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
