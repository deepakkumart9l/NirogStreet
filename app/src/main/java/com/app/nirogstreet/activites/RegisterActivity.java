package com.app.nirogstreet.activites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import org.w3c.dom.Text;

/**
 * Created by Preeti on 17-08-2017.
 */

public class RegisterActivity extends AppCompatActivity {
    EditText firstNameEt, lastNameEt, phoneEt, emailEt, confirmpassEt,setPass;
    ImageView backImageView;
    TextView registerHeader, registerAs, AllreadyhaveAccount, signIn, sentTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        registerHeader = (TextView) findViewById(R.id.title_side);
        sentTv=(TextView)findViewById(R.id.sentTv);
backImageView=(ImageView)findViewById(R.id.back);
        AllreadyhaveAccount = (TextView) findViewById(R.id.alreadyTv);
        signIn = (TextView) findViewById(R.id.signIn);
        registerAs = (TextView) findViewById(R.id.registerAs);
        firstNameEt = (EditText) findViewById(R.id.firstNameEt);
        phoneEt = (EditText) findViewById(R.id.phoneEt);
        lastNameEt = (EditText) findViewById(R.id.lastNameEt);
        confirmpassEt = (EditText) findViewById(R.id.confirmpassEt);
        emailEt = (EditText) findViewById(R.id.emailEt);
        setPass=(EditText) findViewById(R.id.passEt);

        TypeFaceMethods.setRegularTypeFaceForTextView(registerAs, RegisterActivity.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(registerHeader, RegisterActivity.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(sentTv, RegisterActivity.this);

        TypeFaceMethods.setRegularTypeFaceForTextView(AllreadyhaveAccount, RegisterActivity.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(signIn, RegisterActivity.this);

        TypeFaceMethods.setRegularTypeFaceEditText(phoneEt, RegisterActivity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(setPass, RegisterActivity.this);

        TypeFaceMethods.setRegularTypeFaceEditText(emailEt, RegisterActivity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(confirmpassEt, RegisterActivity.this);

        TypeFaceMethods.setRegularTypeFaceEditText(lastNameEt, RegisterActivity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(firstNameEt, RegisterActivity.this);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
