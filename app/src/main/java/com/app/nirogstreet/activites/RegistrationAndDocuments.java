package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

/**
 * Created by Preeti on 28-08-2017.
 */

public class RegistrationAndDocuments extends Activity {
    CircularProgressBar circularProgressBar;
    TextView addQualificationTextView, addTextView;
    SesstionManager sesstionManager;
    TextView titileText, skipTextView;
    boolean isSkip = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_documents);
        sesstionManager = new SesstionManager(RegistrationAndDocuments.this);
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

            }
        });
        TypeFaceMethods.setRegularTypeFaceForTextView(skipTextView, RegistrationAndDocuments.this);

        titileText = (TextView) findViewById(R.id.title_side_left);
        TypeFaceMethods.setRegularTypeFaceForTextView(titileText, RegistrationAndDocuments.this);
        addQualificationTextView = (TextView) findViewById(R.id.addQualification);
        TypeFaceMethods.setRegularTypeFaceForTextView(addQualificationTextView, RegistrationAndDocuments.this);
        addQualificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationAndDocuments.this, EditQualificationDetailOrAddQualificationsDetails.class);
                startActivity(intent);
            }
        });


    }
}
