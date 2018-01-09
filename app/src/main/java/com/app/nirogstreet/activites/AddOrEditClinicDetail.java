package com.app.nirogstreet.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.model.AllClinicModel;
import com.app.nirogstreet.model.ClinicDetailModel;
import com.app.nirogstreet.model.SpecializationModel;
import com.app.nirogstreet.model.TimingsModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Preeti on 08-09-2017.
 */

public class AddOrEditClinicDetail extends AppCompatActivity {
    private static final int RESULT_CODE_LOCATION = 2;
    UserDetailModel userDetailModel;
    boolean isServiceClicked = false;
    String longitude, latitude = null;

    ArrayList<SpecializationModel> multipleSelectedItemModels = new ArrayList<>();
    AllClinicModel allClinicModel;
    ClinicDetailModel clinicDetailModel;
    EditText addressEt, clinicName, city, pincode, feeEt, Services_nameEt, countryEt,locality;
    ArrayList<SpecializationModel> servicesMultipleSelectedModels = new ArrayList<>();
    String clinic_Name;
    private static final int RESULT_CODE = 1;
    TextView saveTv;
    ImageView backImageView;
    private int position = -1;
    SesstionManager sesstionManager;

    @Override
    protected void onResume() {
        if (ApplicationSingleton.isListingFinish()) {
            ApplicationSingleton.setIsListingFinish(false);
            finish();
        }
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_or_add_clinic);
        if (getIntent().hasExtra("item")) {
            allClinicModel = (AllClinicModel) getIntent().getSerializableExtra("item");
        }
        if (getIntent().hasExtra("ClinicModel")) {
            clinicDetailModel = (ClinicDetailModel) getIntent().getSerializableExtra("ClinicModel");
        }
        saveTv = (TextView) findViewById(R.id.saveTv);
        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String id = null;
                    String created_by = null;
                    String clinic_docID = null;
                    if (allClinicModel != null && allClinicModel.getId() != null) {
                        id = allClinicModel.getId();
                        created_by = allClinicModel.getCreated_by();
                    }
                    if (clinicDetailModel != null && clinicDetailModel.getClinic_docID() != null) {
                        clinic_docID = clinicDetailModel.getClinic_docID();
                    }
                    ArrayList<TimingsModel> timingsModels=new ArrayList<TimingsModel>();
                    if(clinicDetailModel!=null&&clinicDetailModel.getId()!=null)
                    {
                        id=clinicDetailModel.getId();
                    }
                    if(clinicDetailModel!=null&&clinicDetailModel.getCreated_by()!=null)
                    {
                       created_by=clinicDetailModel.getCreated_by();
                    }
                    if(clinicDetailModel!=null&&clinicDetailModel.getTimingsModels()!=null)
                    {
                        timingsModels=clinicDetailModel.getTimingsModels();
                    }
                    ClinicDetailModel clinicDetailModel = new ClinicDetailModel(id, clinicName.getText().toString(),locality.getText().toString(), "", addressEt.getText().toString(), countryEt.getText().toString(), city.getText().toString(), pincode.getText().toString(), latitude, longitude, feeEt.getText().toString(), multipleSelectedItemModels, timingsModels, created_by, clinic_docID);
                    Intent intent = new Intent(AddOrEditClinicDetail.this, Timings.class);
                    intent.putExtra("ClinicModel", clinicDetailModel);
                    startActivity(intent);
                }
            }
        });
        sesstionManager = new SesstionManager(AddOrEditClinicDetail.this);
        userDetailModel = ApplicationSingleton.getUserDetailModel();
        addressEt = (EditText) findViewById(R.id.address);
        countryEt = (EditText) findViewById(R.id.country);
        locality=(EditText)findViewById(R.id.locality);

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
        if (allClinicModel != null) {
            if (allClinicModel.getId() != null) {

            }
            if(allClinicModel.getLocality()!=null)
                locality.setText(allClinicModel.getLocality());
            if (allClinicModel.getClinic_name() != null)
                clinicName.setText(allClinicModel.getClinic_name());
            if (allClinicModel.getAddress() != null)
                addressEt.setText(allClinicModel.getAddress());
            if (allClinicModel.getCity() != null)
                city.setText(allClinicModel.getCity());
            if (allClinicModel.getState() != null)
                countryEt.setText(allClinicModel.getState());
            if (allClinicModel.getPincode() != null)
                pincode.setText(allClinicModel.getPincode());
            if (allClinicModel.getFees() != null)
                feeEt.setText(allClinicModel.getFees());
            if (allClinicModel.getSpecializationModels() != null && allClinicModel.getSpecializationModels().size() > 0) {
                Services_nameEt.setText(getSelectedNameCsv(allClinicModel.getSpecializationModels()));
                multipleSelectedItemModels = allClinicModel.getSpecializationModels();
            }
            if (allClinicModel.getCreated_by().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID))) {
                Services_nameEt.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (!isServiceClicked) {
                            isServiceClicked = true;
                            Intent intent = new Intent(AddOrEditClinicDetail.this, Multi_Select_Search_specialization.class);
                            intent.putExtra("isService", true);

                            if (multipleSelectedItemModels != null && multipleSelectedItemModels.size() > 0)
                                intent.putExtra("list", multipleSelectedItemModels);


                            startActivityForResult(intent, RESULT_CODE);
                        }
                        return false;
                    }
                });
                locality.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NetworkUtill.isNetworkAvailable(AddOrEditClinicDetail.this)) {
                            Intent intent = new Intent(AddOrEditClinicDetail.this, SearchLocationCity.class);
                            startActivityForResult(intent, RESULT_CODE_LOCATION);
                        }

                    }
                });
            } else {
                city.setClickable(false);
                city.setFocusable(false);
                addressEt.setClickable(false);
                addressEt.setFocusable(false);
                locality.setClickable(false);
                locality.setFocusable(false);
                clinicName.setClickable(false);
                clinicName.setFocusable(false);
                countryEt.setClickable(false);
                countryEt.setFocusable(false);
                pincode.setClickable(false);
                pincode.setFocusable(false);
            }
        } else {
            Services_nameEt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!isServiceClicked) {
                        isServiceClicked = true;
                        Intent intent = new Intent(AddOrEditClinicDetail.this, Multi_Select_Search_specialization.class);
                        intent.putExtra("isService", true);

                        if (multipleSelectedItemModels != null && multipleSelectedItemModels.size() > 0)
                            intent.putExtra("list", servicesMultipleSelectedModels);


                        startActivityForResult(intent, RESULT_CODE);
                    }
                    return false;
                }
            });
        }
        if (clinicDetailModel != null) {
            if (clinicDetailModel.getId() != null) {

            }
            if (clinicDetailModel.getName() != null)
                clinicName.setText(clinicDetailModel.getName());
            if (clinicDetailModel.getAddress() != null)
                addressEt.setText(clinicDetailModel.getAddress());

            if (clinicDetailModel.getCity() != null)
                city.setText(clinicDetailModel.getCity());
            if (clinicDetailModel.getState() != null)
                countryEt.setText(clinicDetailModel.getState());
            if(clinicDetailModel.getLocality()!=null)
            {
                locality.setText(clinicDetailModel.getLocality());
            }
            if (clinicDetailModel.getPincode() != null)
                pincode.setText(clinicDetailModel.getPincode());
            if (clinicDetailModel.getConsultation_fee() != null)
                feeEt.setText(clinicDetailModel.getConsultation_fee());
            if (clinicDetailModel.getServicesModels() != null && clinicDetailModel.getServicesModels().size() > 0) {
                Services_nameEt.setText(getSelectedNameCsv(clinicDetailModel.getServicesModels()));
                multipleSelectedItemModels = clinicDetailModel.getServicesModels();
            }
            if (clinicDetailModel.getCreated_by().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID))) {
                Services_nameEt.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (!isServiceClicked) {
                            isServiceClicked = true;
                            Intent intent = new Intent(AddOrEditClinicDetail.this, Multi_Select_Search_specialization.class);
                            intent.putExtra("isService", true);

                            if (multipleSelectedItemModels != null && multipleSelectedItemModels.size() > 0)
                                intent.putExtra("list", multipleSelectedItemModels);


                            startActivityForResult(intent, RESULT_CODE);
                        }
                        return false;
                    }
                });
                locality.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(NetworkUtill.isNetworkAvailable(AddOrEditClinicDetail.this)) {
                            Intent intent = new Intent(AddOrEditClinicDetail.this, SearchLocationCity.class);
                            startActivityForResult(intent, RESULT_CODE_LOCATION);
                        }
                    }
                });
            } else {
                city.setClickable(false);
                city.setFocusable(false);
                addressEt.setClickable(false);
                addressEt.setFocusable(false);
                clinicName.setClickable(false);
                clinicName.setFocusable(false);
                countryEt.setClickable(false);
                locality.setClickable(false);
                locality.setFocusable(false);
                countryEt.setFocusable(false);
                pincode.setClickable(false);
                pincode.setFocusable(false);
            }
        } else {

            Services_nameEt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!isServiceClicked) {
                        isServiceClicked = true;
                        Intent intent = new Intent(AddOrEditClinicDetail.this, Multi_Select_Search_specialization.class);
                        intent.putExtra("isService", true);

                        if (multipleSelectedItemModels != null && multipleSelectedItemModels.size() > 0)
                            intent.putExtra("list", servicesMultipleSelectedModels);


                        startActivityForResult(intent, RESULT_CODE);
                    }
                    return false;
                }
            });
        }
        if(clinicDetailModel==null&&allClinicModel==null)
        {locality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtill.isNetworkAvailable(AddOrEditClinicDetail.this)) {
                    Intent intent = new Intent(AddOrEditClinicDetail.this, SearchLocationCity.class);
                    startActivityForResult(intent, RESULT_CODE_LOCATION);
                }
            }
        });

        }
        if (getIntent().hasExtra("clinic_Name")) {
            clinic_Name = getIntent().getStringExtra("clinic_Name");
        }
        if (clinic_Name != null) {
            clinicName.setText(clinic_Name);
        }



        if (getIntent().hasExtra("pos")) {
            position = getIntent().getIntExtra("pos", -1);
            if (position != -1) {
                if (userDetailModel.getClinicDetailModels() != null && userDetailModel.getClinicDetailModels().size() > 0) {
                    multipleSelectedItemModels = userDetailModel.getClinicDetailModels().get(position).getServicesModels();

                }

            }
        }


    }

    public String getSelectedNameCsv(ArrayList<SpecializationModel> list) {
        String languageCSV = "";
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String language = list.get(i).getSpecializationName();
                if (language != null && !language.trim().isEmpty()
                        && languageCSV != null && !languageCSV.trim().isEmpty())
                    languageCSV = languageCSV + ", ";
                languageCSV = languageCSV + language;

            }
        }
        return languageCSV;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isServiceClicked = false;
        try {
            if (requestCode == RESULT_CODE_LOCATION) {

                if (data != null) {
                    String address = data.getStringExtra("address");
                    String location = data.getStringExtra("city");
                    String pincodes = null;
                    String country = null;
                    if (data.getStringExtra("pincode") != null) {
                        pincodes = data.getStringExtra("pincode");
                    }
                    if (data.hasExtra("longitude")) ;
                    {
                        longitude = data.getStringExtra("longitude");
                    }
                    if (data.hasExtra("latitude")) {
                        latitude = data.getStringExtra("latitude");
                    }
                    if (data.hasExtra("country") && data.getStringExtra("country") != null) {
                        country = data.getStringExtra("country");
                        countryEt.setText(country);
                    }
                    city.setText(location);
                    locality.setText(address);
                    pincode.setText(pincodes);

                }
            }
            if (requestCode == RESULT_CODE) {
                if (data != null) {

                    String s = data.getStringExtra("friendsCsv");
                    Services_nameEt.setText(s);
                    System.out.print(s);

                    servicesMultipleSelectedModels = (ArrayList<SpecializationModel>) data.getSerializableExtra("list");
                    multipleSelectedItemModels=servicesMultipleSelectedModels;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validate() {
        if (clinicName.getText().toString().length() == 0) {
            Toast.makeText(AddOrEditClinicDetail.this, "Enter Clinic Name.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (addressEt.getText().toString().length() == 0) {
            Toast.makeText(AddOrEditClinicDetail.this, "Enter Address Detail.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (countryEt.getText().toString().length() == 0) {
            Toast.makeText(AddOrEditClinicDetail.this, "Enter Country Name.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (city.getText().toString().length() == 0) {
            Toast.makeText(AddOrEditClinicDetail.this, "Enter City Name.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (feeEt.getText().toString().length() == 0) {
            Toast.makeText(AddOrEditClinicDetail.this, "Enter Fee Detail.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (pincode.getText().length() == 0) {
            Toast.makeText(AddOrEditClinicDetail.this, "Enter Pincode Detail.", Toast.LENGTH_LONG).show();
            return false;

        }
        return true;
    }

}
