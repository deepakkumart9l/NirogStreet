package com.app.nirogstreet.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.uttil.TypeFaceMethods;

/**
 * Created by Preeti on 25-08-2017.
 */

public class Dr_Qualifications extends AppCompatActivity {
    CircularProgressBar circularProgressBar;
    TextView addQualificationTextView, addTextView;
    TextView titileText, skipTextView;
    boolean isSkip = false;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qualifications);
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
}
