package com.app.nirogstreet.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.QualificationModel;
import com.app.nirogstreet.model.RegistrationAndDocumenModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.parser.QualificationParser;
import com.app.nirogstreet.parser.RegistrationParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.PathUtil;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.SSLContext;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by Preeti on 28-08-2017.
 */

public class EditRegistrationAndDocuments extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static int year;
    static boolean isVisible = true;
    EditText yearEditText, clgEt, degree_name, sepcialization;
    private int STORAGE_PERMISSION_CODE_DOCUMENT = 3;
    LinearLayout updateDocLinearLayout;
    RelativeLayout EditDocRelativeLayout;
    CircularProgressBar circularProgressBar;
    private String authToken, userId;
    Spinner spinnerCouncilType;
    private static final int RESULT_CODE = 1;

    int REQUEST_CODE = 4;
    String type="-1";
    private static final String[] councilType = {"Center", "State"};

    ImageView backImageView;
    EditText council_typeEt;
    TextView title_side_left, saveTv;
    TextView docNameTv, uploadDoctv, add;

    private UserDetailModel userDetailModel;
    AddOrUpdateRegistrationAsynctask addOrUpdateRegistrationAsynctask;
    private int position = -1;
    private String docpath;
    private SesstionManager sesstionManager;
    private DeleteRegistrationAsynctask deleteQualificationAsynctask;
    private ImageView deleteImageView;
    private boolean isQualificationTouched=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_registration);
        if (getIntent().hasExtra("userModel")) {
            userDetailModel = (UserDetailModel) getIntent().getSerializableExtra("userModel");
        }
        deleteImageView = (ImageView) findViewById(R.id.delete);

        sesstionManager = new SesstionManager(EditRegistrationAndDocuments.this);
        if (sesstionManager.isUserLoggedIn()) {
            authToken = sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN);
            userId = sesstionManager.getUserDetails().get(SesstionManager.USER_ID);
        }
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        updateDocLinearLayout = (LinearLayout) findViewById(R.id.uploadDoc);
        EditDocRelativeLayout = (RelativeLayout) findViewById(R.id.EditDoc);
        docNameTv = (TextView) findViewById(R.id.docNameTv);
        uploadDoctv = (TextView) findViewById(R.id.uploadDoctv);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        spinnerCouncilType = (Spinner) findViewById(R.id.titleLay);

        title_side_left = (TextView) findViewById(R.id.title_side_left);
        yearEditText = (EditText) findViewById(R.id.year);
        degree_name = (EditText) findViewById(R.id.degree_name);
        clgEt = (EditText) findViewById(R.id.clgEt);
        sepcialization = (EditText) findViewById(R.id.sepcialization);
        saveTv = (TextView) findViewById(R.id.saveTv);
        updateDocLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionForDoc();
            }
        });
        EditDocRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionForDoc();

            }
        });
        yearEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(yearEditText.getWindowToken(), 0);
                show();
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
        initSpinnerScrollingCategory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.print(requestCode);
        if (requestCode == 3) {
            openFile();
        }
    }

    private void initSpinnerScrollingCategory() {
        spinnerCouncilType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1)
                    type = position + 1 + "";
                else
                    type = "-1";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spiner_item, councilType) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);


                return v;
            }


            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);


                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerCouncilType.setAdapter(adapter);

degree_name.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       /* if (type.equalsIgnoreCase("-1")) {
            Toast.makeText(EditRegistrationAndDocuments.this, "Select Council Type first.", Toast.LENGTH_LONG).show();
        } else {
            if (type.equalsIgnoreCase("1")) {
                if (!isQualificationTouched) {
                    isQualificationTouched = true;
                    Intent intent = new Intent(EditRegistrationAndDocuments.this, SelectCouncilName.class);
                    intent.putExtra("type","central");

                    startActivityForResult(intent, RESULT_CODE);
                }
            } else if(type.equalsIgnoreCase("2")){
                if (!isQualificationTouched) {
                    isQualificationTouched = true;
                    Intent intent = new Intent(EditRegistrationAndDocuments.this, SelectCouncilName.class);
                    intent.putExtra("type","state");
                    startActivityForResult(intent, RESULT_CODE);
                }
            }
        }
*/    }
});
    }

    @Override
    protected void onResume() {
        isQualificationTouched=false;
        super.onResume();
        if (userDetailModel != null && userDetailModel.getRegistrationAndDocumenModels() != null && userDetailModel.getRegistrationAndDocumenModels().size() > 0 && position != -1)

        {

            final RegistrationAndDocumenModel registrationAndDocumenModel = userDetailModel.getRegistrationAndDocumenModels().get(position);

            degree_name.setText(registrationAndDocumenModel.getCouncil_registration_number());
            clgEt.setText(registrationAndDocumenModel.getCouncil_name());
            yearEditText.setText(registrationAndDocumenModel.getCouncil_year());
            if (registrationAndDocumenModel.getCouncilType() != null) {
                if (registrationAndDocumenModel.getCouncilType().equalsIgnoreCase("1") && !userDetailModel.getCategory().equalsIgnoreCase("")) {
                    spinnerCouncilType.setSelection(0);
                    type = "1";
                } else {
                    spinnerCouncilType.setSelection(1);
                    type = "2";
                }
            }
            if (registrationAndDocumenModel.getUploadedDoc() != null) {
                EditDocRelativeLayout.setVisibility(View.VISIBLE);
                updateDocLinearLayout.setVisibility(View.GONE);
                docNameTv.setText(registrationAndDocumenModel.getUploadedDoc());
            } else {
                EditDocRelativeLayout.setVisibility(View.GONE);
                updateDocLinearLayout.setVisibility(View.VISIBLE);
            }
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtill.isNetworkAvailable(EditRegistrationAndDocuments.this)) {


                        deleteQualificationAsynctask = new DeleteRegistrationAsynctask(registrationAndDocumenModel.getId());
                        deleteQualificationAsynctask.execute();


                    } else {
                        NetworkUtill.showNoInternetDialog(EditRegistrationAndDocuments.this);
                    }
                }
            });
            saveTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtill.isNetworkAvailable(EditRegistrationAndDocuments.this)) {
                        if (validate()) {
                            addOrUpdateRegistrationAsynctask = new AddOrUpdateRegistrationAsynctask(type, degree_name.getText().toString(), clgEt.getText().toString(), yearEditText.getText().toString(), registrationAndDocumenModel.getId());
                            addOrUpdateRegistrationAsynctask.execute();
                        }

                    } else {
                        NetworkUtill.showNoInternetDialog(EditRegistrationAndDocuments.this);
                    }
                }
            });
        } else {
            title_side_left.setText("Add Registartion ");
            deleteImageView.setVisibility(View.GONE);
            saveTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtill.isNetworkAvailable(EditRegistrationAndDocuments.this)) {
                        if (validate()) {
                            addOrUpdateRegistrationAsynctask = new AddOrUpdateRegistrationAsynctask(type, degree_name.getText().toString(), clgEt.getText().toString(), yearEditText.getText().toString(), "");
                            addOrUpdateRegistrationAsynctask.execute();
                        }

                    } else {
                        NetworkUtill.showNoInternetDialog(EditRegistrationAndDocuments.this);
                    }
                }
            });
        }
    }

    public void show() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        MonthYearPickerDialog newFragment = new MonthYearPickerDialog();
        newFragment.setCancelable(false);
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
                            EditRegistrationAndDocuments.isVisible = true;

                        }
                    });
            return builder.create();
        }
    }

    public class AddOrUpdateRegistrationAsynctask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String type, councilname, registraionNumber, id, year;
        CircularProgressBar bar;
        //PlayServiceHelper regId;
        //PlayServiceHelper regId;

        JSONObject jo;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public AddOrUpdateRegistrationAsynctask(String type, String councilname, String registrationnumber, String year, String id) {

            this.type = type;
            this.id = id;
            this.councilname = councilname;
            this.year = year;
            this.registraionNumber = registrationnumber;

        }

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/registration-documents";
                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();

                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;

                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder
                        .create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("Content-Type", "applicaion/json");
                entityBuilder.addTextBody(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST);
                entityBuilder.addTextBody("userID", userId);
                entityBuilder.addTextBody("Registration[council_type]", type);
                entityBuilder.addTextBody("Registration[council]", councilname);
                entityBuilder.addTextBody("Registration[registration_number]", registraionNumber);
                entityBuilder.addTextBody("Registration[year]", year);
                if (position != -1)
                    entityBuilder.addTextBody("Registration[id]", id);

                if (docpath != null && docpath.toString().trim().length() > 0) {
                    File file = new File(docpath);
                    FileBody encFile = new FileBody(file);
                    entityBuilder.addPart("Registration[uploadFile]", encFile);
                }
                httppost.setHeader("Authorization", "Basic " + authToken);

                HttpEntity entity = entityBuilder.build();
                httppost.setEntity(entity);
                response = client.execute(httppost);

                responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                jo = new JSONObject(responseBody);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            circularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    JSONArray errorArray;
                    JSONObject dataJsonObject;
                    boolean status = false;
                    String auth_token = "", createdOn = "", id = "", email = "", mobile = "", user_type = "", lname = "", fname = "";
                    if (jo.has("data") && !jo.isNull("data")) {
                        dataJsonObject = jo.getJSONObject("data");

                        if (dataJsonObject.has("status") && !dataJsonObject.isNull("status"))

                        {
                            status = dataJsonObject.getBoolean("status");
                            if (!status) {
                                if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                                    errorArray = dataJsonObject.getJSONArray("message");
                                    for (int i = 0; i < errorArray.length(); i++) {
                                        String error = errorArray.getJSONObject(i).getString("error");
                                        Toast.makeText(EditRegistrationAndDocuments.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (dataJsonObject.has("profile_complete") && !dataJsonObject.isNull("profile_complete")) {
                                    ApplicationSingleton.getUserDetailModel().setProfile_complete(dataJsonObject.getInt("profile_complete"));
                                }
                                if (dataJsonObject.has("registrations") && !dataJsonObject.isNull("registrations")) {
                                    UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                                    ArrayList<RegistrationAndDocumenModel> registrationAndDocumenModels
                                            = RegistrationParser.registrationParser(dataJsonObject);

                                    userDetailModel.setRegistrationAndDocumenModels(registrationAndDocumenModels);
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    ApplicationSingleton.setRegistrationUpdated(true);
                                }
                                if (dataJsonObject.has("status_message") && !dataJsonObject.isNull("status_message")) {
                                    Toast.makeText(EditRegistrationAndDocuments.this, dataJsonObject.getString("status_message"), Toast.LENGTH_SHORT).show();

                                }
                                finish();
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public boolean validate() {
        if (clgEt.getText().toString().length() == 0) {
            Toast.makeText(EditRegistrationAndDocuments.this, R.string.council, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (degree_name.getText().toString().length() == 0) {
            Toast.makeText(EditRegistrationAndDocuments.this, R.string.council_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (type.equalsIgnoreCase("-1")) {
            Toast.makeText(EditRegistrationAndDocuments.this, R.string.council_type, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (yearEditText.getText().toString().length() == 0) {
            Toast.makeText(EditRegistrationAndDocuments.this, R.string.year, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                Uri data1 = data.getData();
                // String pathe = data1.getPath();
                // path = getRealPathFromURI_API19(getActivity(), data1);
                try {
                    String path = PathUtil.getPath(EditRegistrationAndDocuments.this, data1);

                    if (path != null) {
                        if (path.contains(".")) {
                            String extension = path.substring(path.lastIndexOf("."));

                            if (extension.equalsIgnoreCase(".doc") || extension.equalsIgnoreCase(".pdf") || extension.equalsIgnoreCase(".docx") || extension.equalsIgnoreCase(".xlsx") || extension.equalsIgnoreCase(".xls") || extension.equalsIgnoreCase(".ppt") || extension.equalsIgnoreCase(".PPTX")) {
                                docpath = path;
                                updateDocLinearLayout.setVisibility(View.GONE);
                                EditDocRelativeLayout.setVisibility(View.VISIBLE);
                                File f = new File("" + data1);
                                docNameTv.setText(f.getName());


                            } else {
                                Toast.makeText(EditRegistrationAndDocuments.this, "Not Supported", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(EditRegistrationAndDocuments.this, "Not Supported", Toast.LENGTH_LONG).show();
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }

        }
        if (requestCode == RESULT_CODE) {
            if (data != null) {
                String s = data.getStringExtra("friendsCsv");

                degree_name.setText(s);
                System.out.print(s);
            }
        }

    }

    public class DeleteRegistrationAsynctask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String university, year, qualification, id;
        CircularProgressBar bar;
        //PlayServiceHelper regId;

        JSONObject jo;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public DeleteRegistrationAsynctask(String id) {

            this.id = id;


        }

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/delete-registration-document";
                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();

                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;

                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));
                pairs.add(new BasicNameValuePair("userID", userId));
                pairs.add(new BasicNameValuePair("Registration[id]", id));
                httppost.setHeader("Authorization", "Basic " + authToken);

                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                response = client.execute(httppost);
                responseBody = EntityUtils.toString(response.getEntity());
                jo = new JSONObject(responseBody);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            circularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    JSONArray errorArray;
                    JSONObject dataJsonObject;
                    boolean status = false;
                    String auth_token = "", createdOn = "", id = "", email = "", mobile = "", user_type = "", lname = "", fname = "";
                    if (jo.has("data") && !jo.isNull("data")) {
                        dataJsonObject = jo.getJSONObject("data");

                        if (dataJsonObject.has("status") && !dataJsonObject.isNull("status"))

                        {
                            status = dataJsonObject.getBoolean("status");
                            if (!status) {
                                if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                                    errorArray = dataJsonObject.getJSONArray("message");
                                    for (int i = 0; i < errorArray.length(); i++) {
                                        String error = errorArray.getJSONObject(i).getString("error");
                                        Toast.makeText(EditRegistrationAndDocuments.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (dataJsonObject.has("profile_complete") && !dataJsonObject.isNull("profile_complete")) {
                                    ApplicationSingleton.getUserDetailModel().setProfile_complete(dataJsonObject.getInt("profile_complete"));
                                }
                                if (dataJsonObject.has("registrations") && !dataJsonObject.isNull("registrations")) {
                                    UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                                    ArrayList<RegistrationAndDocumenModel> registrationAndDocumenModels = RegistrationParser.registrationParser(dataJsonObject);

                                    userDetailModel.setRegistrationAndDocumenModels(registrationAndDocumenModels);
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    ApplicationSingleton.setRegistrationUpdated(true);
                                } else {
                                    UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                                    ArrayList<RegistrationAndDocumenModel> registrationAndDocumenModels = null;

                                    userDetailModel.setRegistrationAndDocumenModels(registrationAndDocumenModels);
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    ApplicationSingleton.setRegistrationUpdated(true);
                                }

                                if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                                    Toast.makeText(EditRegistrationAndDocuments.this, dataJsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                }
                                finish();
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public void checkPermissionForDoc() {
        if (
                ContextCompat.checkSelfPermission(EditRegistrationAndDocuments.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(EditRegistrationAndDocuments.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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

}
