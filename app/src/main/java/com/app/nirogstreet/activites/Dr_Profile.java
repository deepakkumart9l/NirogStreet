package com.app.nirogstreet.activites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import org.w3c.dom.Text;

/**
 * Created by Preeti on 22-08-2017.
 */

public class Dr_Profile extends AppCompatActivity {
    TextView nameTv, placeTv, emailTv, phoneTv, WebTv, yearOfBirthTv, yearOfExperienceTv, QualificationTv, aboutHeading, aboutDetail, QualificationSectionTv, SpecializationSectionHeadingTv, sepcilizationDetailTv,consultationFeesHeading,allTaxes,fee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_detail);
        nameTv = (TextView) findViewById(R.id.nameTv);
        placeTv = (TextView) findViewById(R.id.placeTv);
        emailTv = (TextView) findViewById(R.id.emailTv);
        phoneTv = (TextView) findViewById(R.id.phoneTv);
        WebTv = (TextView) findViewById(R.id.WebTv);
        aboutHeading = (TextView) findViewById(R.id.about);
        aboutDetail = (TextView) findViewById(R.id.about_detail);
        yearOfBirthTv = (TextView) findViewById(R.id.yearOfBirthTv);
        yearOfExperienceTv = (TextView) findViewById(R.id.yearOfExperienceTv);
        QualificationSectionTv = (TextView) findViewById(R.id.QualificationSectionTv);
        SpecializationSectionHeadingTv = (TextView) findViewById(R.id.SpecializationSectionHeadingTv);
        QualificationTv = (TextView) findViewById(R.id.QualificationTv);
        sepcilizationDetailTv = (TextView) findViewById(R.id.sepcilizationDetailTv);
        consultationFeesHeading=(TextView)findViewById(R.id.consultaionFees);
        allTaxes=(TextView)findViewById(R.id.allTaxes);

        TypeFaceMethods.setRegularTypeFaceForTextView(emailTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(phoneTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(WebTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(yearOfBirthTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(yearOfExperienceTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(QualificationTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(nameTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(placeTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(sepcilizationDetailTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(SpecializationSectionHeadingTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(consultationFeesHeading, Dr_Profile.this);

        TypeFaceMethods.setRegularTypeBoldFaceTextView(aboutHeading, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(aboutDetail, Dr_Profile.this);




    }
}
