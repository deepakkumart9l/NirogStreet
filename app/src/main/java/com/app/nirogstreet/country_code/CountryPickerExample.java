package com.app.nirogstreet.country_code;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.nirogstreet.R;


public class CountryPickerExample extends FragmentActivity {
    int country_picker_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_country_picker);
       /* bck_img = (ImageView) findViewById(R.id.back);
        bck_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        CountryPicker picker = new CountryPicker();
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode) {
                /*Toast.makeText(
                        CountryPickerExample.this,
                        "Country Name: " + name + " - Code: " + code
                                + " - Currency: "
                                + CountryPicker.getCurrencyCode(code) + " - Dial Code: " + dialCode,
                        Toast.LENGTH_SHORT).show();*/

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", dialCode);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        transaction.replace(R.id.home, picker);
        transaction.commit();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.country_picker, menu);
        MenuItem item = menu.findItem(R.id.show_dialog);
        item.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CountryPicker picker = CountryPicker.newInstance("SelectCountry");
               /* picker.setListener(new CountryPickerListener() {

                    @Override
                    public void onSelectCountry(String name, String code, String dialCode) {
                        Toast.makeText(
                                CountryPickerExample.this,
                                "Country Name: " + name + " - Code: " + code
                                        + " - Currency: "
                                        + CountryPicker.getCurrencyCode(code) + " - Dial Code: " + dialCode,
                                Toast.LENGTH_SHORT).show();
                    }
                });*/

                picker.show(getSupportFragmentManager(), "COUNTRY_CODE_PICKER");
                return false;
            }
        });
        return true;
    }
}
