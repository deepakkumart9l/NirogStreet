package com.app.nirogstreet.activites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import java.util.Calendar;

/**
 * Created by Preeti on 28-08-2017.
 */

public class EditRegistrationAndDocuments extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static int year;
    static boolean isVisible = true;
    EditText yearEditText, clgEt, degree_name, sepcialization;
    private int STORAGE_PERMISSION_CODE_DOCUMENT = 3;
    int REQUEST_CODE = 4;


    TextView title_side_left, saveTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_registration);
        title_side_left = (TextView) findViewById(R.id.title_side_left);
        yearEditText = (EditText) findViewById(R.id.year);
        degree_name = (EditText) findViewById(R.id.degree_name);
        clgEt = (EditText) findViewById(R.id.clgEt);
        sepcialization = (EditText) findViewById(R.id.sepcialization);
        saveTv = (TextView) findViewById(R.id.saveTv);
        TypeFaceMethods.setRegularTypeFaceEditText(sepcialization, EditRegistrationAndDocuments.this);
        TypeFaceMethods.setRegularTypeFaceEditText(yearEditText, EditRegistrationAndDocuments.this);

        TypeFaceMethods.setRegularTypeFaceEditText(degree_name, EditRegistrationAndDocuments.this);

        TypeFaceMethods.setRegularTypeFaceEditText(clgEt, EditRegistrationAndDocuments.this);

        TypeFaceMethods.setRegularTypeFaceForTextView(title_side_left, EditRegistrationAndDocuments.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(saveTv, EditRegistrationAndDocuments.this);

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
                if (NetworkUtill.isNetworkAvailable(EditRegistrationAndDocuments.this)) {
                    if (validate()) {

                    }

                } else {
                    NetworkUtill.showNoInternetDialog(EditRegistrationAndDocuments.this);
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

    }

    public void show() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

    /*MonthYearPickerDialog pd = new MonthYearPickerDialog();
    pd.setListener((View.OnClickListener) this);
    pd.show(getFragmentManager(), "MonthYearPickerDialog");*/
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        EditQualificationDetailOrAddQualificationsDetails.MonthYearPickerDialog newFragment = new EditQualificationDetailOrAddQualificationsDetails.MonthYearPickerDialog();
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
        EditQualificationDetailOrAddQualificationsDetails.TimePickerFragment newFragment = new EditQualificationDetailOrAddQualificationsDetails.TimePickerFragment(this);

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
                            EditRegistrationAndDocuments.MonthYearPickerDialog.this.getDialog().cancel();
                            EditQualificationDetailOrAddQualificationsDetails.isVisible = true;

                        }
                    });
            return builder.create();
        }
    }

    public boolean validate() {
        if (clgEt.getText().toString().length() == 0) {
            Toast.makeText(EditRegistrationAndDocuments.this, "Enter Council Registration Number.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (degree_name.getText().toString().length() == 0) {
            Toast.makeText(EditRegistrationAndDocuments.this, "Enter Council name.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (yearEditText.getText().toString().length() == 0) {
            Toast.makeText(EditRegistrationAndDocuments.this, "Select Year.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
