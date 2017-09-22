package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.app.nirogstreet.R;
import com.app.nirogstreet.model.SpecializationModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import java.util.ArrayList;

/**
 * Created by Preeti on 21-09-2017.
 */
public class SpecilizationAndService extends Activity {
    EditText Services_name, sepcialization;
    UserDetailModel userDetailModel;
    private static final int RESULT_CODE = 1;

    ArrayList<SpecializationModel> multipleSelectedItemModels=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specilization_services);
        Services_name = (EditText) findViewById(R.id.Services_name);
        sepcialization = (EditText) findViewById(R.id.sepcialization);
        TypeFaceMethods.setRegularTypeFaceEditText(Services_name, SpecilizationAndService.this);
        TypeFaceMethods.setRegularTypeFaceEditText(sepcialization, SpecilizationAndService.this);
        if (getIntent().hasExtra("userModel")) {
            userDetailModel = (UserDetailModel) getIntent().getSerializableExtra("userModel");
            sepcialization.setText(getSelectedNameCsv());
            if(userDetailModel.getSpecializationModels()!=null&&userDetailModel.getSpecializationModels().size()!=0)
            {
                multipleSelectedItemModels=userDetailModel.getSpecializationModels();
            }
        }
        sepcialization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecilizationAndService.this, Multi_Select_Search_specialization.class);
                if (multipleSelectedItemModels != null && multipleSelectedItemModels.size() > 0)
                    intent.putExtra("list", multipleSelectedItemModels);

                else if (userDetailModel.getSpecializationModels() != null && userDetailModel.getSpecializationModels().size() > 0)
                    intent.putExtra("list", userDetailModel.getSpecializationModels());

                startActivityForResult(intent, RESULT_CODE);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE) {
            if (data != null) {
                String s = data.getStringExtra("friendsCsv");
                sepcialization.setText(s);
                System.out.print(s);
                multipleSelectedItemModels = (ArrayList<SpecializationModel>) data.getSerializableExtra("list");
            }
        }
    }
}
