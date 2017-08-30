package com.app.nirogstreet.activites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

/**
 * Created by Preeti on 30-08-2017.
 */

public class AddOrEditExperience extends AppCompatActivity {
    ImageView backImageView;
    private SesstionManager sesstionManager;
    UserDetailModel userDetailModel;
    EditText fromEt,toEt,cityEt,clinicOrhospital;
    TextView title_side_left,saveTv;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_experience);
        backImageView=(ImageView)findViewById(R.id.back);
        sesstionManager = new SesstionManager(AddOrEditExperience.this);
        title_side_left = (TextView) findViewById(R.id.title_side_left);
        fromEt = (EditText) findViewById(R.id.from);
        toEt = (EditText) findViewById(R.id.to);
        cityEt = (EditText) findViewById(R.id.city);
        clinicOrhospital=(EditText)findViewById(R.id.hospital);
        saveTv = (TextView) findViewById(R.id.saveTv);
        TypeFaceMethods.setRegularTypeFaceEditText(fromEt, AddOrEditExperience.this);
        TypeFaceMethods.setRegularTypeFaceEditText(toEt, AddOrEditExperience.this);

        TypeFaceMethods.setRegularTypeFaceEditText(cityEt, AddOrEditExperience.this);

        TypeFaceMethods.setRegularTypeFaceEditText(clinicOrhospital, AddOrEditExperience.this);

        TypeFaceMethods.setRegularTypeFaceForTextView(title_side_left, AddOrEditExperience.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(saveTv, AddOrEditExperience.this);

    }
}
