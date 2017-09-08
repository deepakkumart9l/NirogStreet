package com.app.nirogstreet.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.TypeFaceMethods;

/**
 * Created by Preeti on 08-09-2017.
 */

public class AddOrEditClinicDetail extends AppCompatActivity {
    private static final int RESULT_CODE_LOCATION = 2;
    EditText addressEt, clinicName, city, pincode, feeEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_or_add_clinic);
        addressEt = (EditText) findViewById(R.id.address);
        clinicName = (EditText) findViewById(R.id.clinicName);
        city = (EditText) findViewById(R.id.city);
        pincode = (EditText) findViewById(R.id.pincode);
        feeEt = (EditText) findViewById(R.id.feeEt);
        TypeFaceMethods.setRegularTypeFaceEditText(feeEt, AddOrEditClinicDetail.this);

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
                    city.setText(location);
                    addressEt.setText(address);
                    pincode.setText(pincodes);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
