package com.app.nirogstreet.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.QualificationModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.SSLContext;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by Preeti on 25-08-2017.
 */

public class EditQualificationDetailOrAddQualificationsDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static int year;
    static boolean isVisible = true;
    int position = -1;
    ImageView backImageView;
    EditText yearEditText, clgEt, degree_name, sepcialization;
    private int STORAGE_PERMISSION_CODE_DOCUMENT = 3;
    int REQUEST_CODE = 4;
    UserDetailModel userDetailModel;


    TextView title_side_left, saveTv;

    public void checkPermissionForDoc() {
        if (
                ContextCompat.checkSelfPermission(EditQualificationDetailOrAddQualificationsDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(EditQualificationDetailOrAddQualificationsDetails.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("", " Permission Already given ");
            openFile();
        } else {
            Log.e("", "Current app does not have READ_PHONE_STATE permission");
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE_DOCUMENT);
        }
    }

    public void openFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes = {"application/pdf", "application/doc", "application/docx", "application/xlsx", "application/xls",
                "application/ppt", "application/PDF", "application/DOCX", "application/DOC", "application/XLSX",
                "application/XLS", "application/PPTX", "application/PPT"};
        intent.setType("documents/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Documents"), REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_qualification);
        if (getIntent().hasExtra("userModel")) {
            userDetailModel = (UserDetailModel) getIntent().getSerializableExtra("userModel");
        }
        backImageView=(ImageView)findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_side_left = (TextView) findViewById(R.id.title_side_left);
        yearEditText = (EditText) findViewById(R.id.year);
        degree_name = (EditText) findViewById(R.id.degree_name);
        clgEt = (EditText) findViewById(R.id.clgEt);
        sepcialization = (EditText) findViewById(R.id.sepcialization);
        saveTv = (TextView) findViewById(R.id.saveTv);
        TypeFaceMethods.setRegularTypeFaceEditText(sepcialization, EditQualificationDetailOrAddQualificationsDetails.this);
        TypeFaceMethods.setRegularTypeFaceEditText(yearEditText, EditQualificationDetailOrAddQualificationsDetails.this);

        TypeFaceMethods.setRegularTypeFaceEditText(degree_name, EditQualificationDetailOrAddQualificationsDetails.this);

        TypeFaceMethods.setRegularTypeFaceEditText(clgEt, EditQualificationDetailOrAddQualificationsDetails.this);

        TypeFaceMethods.setRegularTypeFaceForTextView(title_side_left, EditQualificationDetailOrAddQualificationsDetails.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(saveTv, EditQualificationDetailOrAddQualificationsDetails.this);

        yearEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(yearEditText.getWindowToken(), 0);
                show();
            }
        });
        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtill.isNetworkAvailable(EditQualificationDetailOrAddQualificationsDetails.this)) {
                    if (validate()) {

                    }

                } else {
                    NetworkUtill.showNoInternetDialog(EditQualificationDetailOrAddQualificationsDetails.this);
                }
            }
        });
        yearEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(yearEditText.getWindowToken(), 0);
                show();
                return false;
            }
        });
        if (getIntent().hasExtra("pos")) {
            position = getIntent().getIntExtra("pos", -1);

        }
        if (userDetailModel != null && userDetailModel.getQualificationModels() != null && userDetailModel.getQualificationModels().size() > 0 && position != -1)

        {
            if (userDetailModel.getSpecializationModels() != null && userDetailModel.getSpecializationModels().size() > 0) {
                sepcialization.setText(getSelectedNameCsv());
            }
            QualificationModel qualificationModel = userDetailModel.getQualificationModels().get(position);

            degree_name.setText(qualificationModel.getDegreeName());
            clgEt.setText(qualificationModel.getClgName());
            yearEditText.setText(qualificationModel.getPassingYear());
        }
        else {
            title_side_left.setText("Add Qualification");
        }

    }

    public String getSelectedNameCsv() {
        String languageCSV = "";

        if (userDetailModel != null && userDetailModel.getSpecializationModels() != null && userDetailModel.getSpecializationModels().size() > 0) {
            for (int i = 0; i < userDetailModel.getSpecializationModels().size(); i++) {
                String language = userDetailModel.getSpecializationModels().get(i).getSpecializationName();
                if (language != null && !language.trim().isEmpty()
                        && languageCSV != null && !languageCSV.trim().isEmpty())
                    languageCSV = languageCSV + ", ";
                languageCSV = languageCSV + language;

            }
        }
        return languageCSV;
    }

    public void show() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

    /*MonthYearPickerDialog pd = new MonthYearPickerDialog();
    pd.setListener((View.OnClickListener) this);
    pd.show(getFragmentManager(), "MonthYearPickerDialog");*/
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        MonthYearPickerDialog newFragment = new MonthYearPickerDialog();
        newFragment.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                System.out.print(year);
                isVisible = true;

                yearEditText.setText(year + "");
            }
        });
        if (isVisible) {
            isVisible = false;
            newFragment.show(fm, "date");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    public void showDateDialog() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        TimePickerFragment newFragment = new TimePickerFragment(this);

        newFragment.show(fm, "date");

    }

    @SuppressLint("ValidFragment")
    public static class TimePickerFragment extends DialogFragment {


        private DatePickerDialog.OnDateSetListener listener;

        public TimePickerFragment(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of TimePickerDialog and return it
            return new DatePickerDialog(getActivity(), listener, year, 0,
                    0);
        }


    }

    @SuppressLint("ValidFragment")
    static public class MonthYearPickerDialog extends DialogFragment {

        private static final int MAX_YEAR = year;
        private DatePickerDialog.OnDateSetListener listener;

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            Calendar cal = Calendar.getInstance();

            View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
            // final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
            final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

           /* monthPicker.setMinValue(1);
            monthPicker.setMaxValue(12);
            monthPicker.setValue(cal.get(Calendar.MONTH) + 1);*/

            int year = cal.get(Calendar.YEAR);
            yearPicker.setMinValue(1917);
            yearPicker.setMaxValue(MAX_YEAR);
            yearPicker.setValue(year);

            builder.setView(dialog)
                    // Add action buttons
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            listener.onDateSet(null, yearPicker.getValue(), 0, 0);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MonthYearPickerDialog.this.getDialog().cancel();
                            EditQualificationDetailOrAddQualificationsDetails.isVisible = true;

                        }
                    });
            return builder.create();
        }
    }

    public boolean validate() {
        if (clgEt.getText().toString().length() == 0) {
            Toast.makeText(EditQualificationDetailOrAddQualificationsDetails.this, "Enter College name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (degree_name.getText().toString().length() == 0) {
            Toast.makeText(EditQualificationDetailOrAddQualificationsDetails.this, "Enter Degree name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sepcialization.getText().toString().length() == 0) {
            Toast.makeText(EditQualificationDetailOrAddQualificationsDetails.this, "Select Specialization.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (yearEditText.getText().toString().length() == 0) {
            Toast.makeText(EditQualificationDetailOrAddQualificationsDetails.this, "Select Year.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
