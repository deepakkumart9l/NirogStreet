package com.app.nirogstreet.activites;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import java.util.Calendar;

/**
 * Created by Preeti on 30-08-2017.
 */

public class AddOrEditExperience extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static boolean isVisible = true;
    ImageView backImageView;
    private SesstionManager sesstionManager;
    UserDetailModel userDetailModel;
    boolean isFromDate = false;
    EditText fromEt, toEt, cityEt, clinicOrhospital;
    TextView title_side_left, saveTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_experience);
        backImageView = (ImageView) findViewById(R.id.back);
        sesstionManager = new SesstionManager(AddOrEditExperience.this);
        title_side_left = (TextView) findViewById(R.id.title_side_left);
        fromEt = (EditText) findViewById(R.id.from);
        toEt = (EditText) findViewById(R.id.to);
        cityEt = (EditText) findViewById(R.id.city);
        clinicOrhospital = (EditText) findViewById(R.id.hospital);
        saveTv = (TextView) findViewById(R.id.saveTv);
        fromEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isFromDate = true;
                if (isVisible)
                    showDateDialog();
                isVisible = false;

                return false;
            }
        });
        toEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isFromDate = false;
                if (isVisible)
                    showDateDialog();
                isVisible = false;
                return false;
            }
        });
        TypeFaceMethods.setRegularTypeFaceEditText(fromEt, AddOrEditExperience.this);
        TypeFaceMethods.setRegularTypeFaceEditText(toEt, AddOrEditExperience.this);

        TypeFaceMethods.setRegularTypeFaceEditText(cityEt, AddOrEditExperience.this);

        TypeFaceMethods.setRegularTypeFaceEditText(clinicOrhospital, AddOrEditExperience.this);

        TypeFaceMethods.setRegularTypeFaceForTextView(title_side_left, AddOrEditExperience.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(saveTv, AddOrEditExperience.this);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        isVisible = true;

        String dateStr = (new StringBuilder()

                // Month is 0 based, just add 1

                .append(dayOfMonth).append("-").append(month + 1).append("-")
                .append(year)).toString();
        if (isFromDate)
            fromEt.setText(dateStr);
        else
            toEt.setText(dateStr);
    }

    @SuppressLint("ValidFragment")
    public static class TimePickerFragment extends DialogFragment {


        private DatePickerDialog.OnDateSetListener listener;

        public TimePickerFragment(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public void onDestroyView() {
            AddOrEditExperience.isVisible = true;
            super.onDestroyView();

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of TimePickerDialog and return it
            return new DatePickerDialog(getActivity(), listener, year, month,
                    day);
        }


    }

    public void showDateDialog() {
       /* InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextAbout.getWindowToken(), 0);
        InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm1.hideSoftInputFromWindow(editTextWebsite.getWindowToken(), 0);
        imm1.hideSoftInputFromWindow(editTextDob.getWindowToken(), 0);
        imm1.hideSoftInputFromWindow(editTextCity.getWindowToken(), 0);
        imm1.hideSoftInputFromWindow(editTextYearOfExpeicence.getWindowToken(), 0);*/
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        TimePickerFragment newFragment = new TimePickerFragment(this);

        newFragment.show(fm, "date");


    }

}
