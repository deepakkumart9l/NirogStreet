package com.app.nirogstreet.activites;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.ExperinceModel;
import com.app.nirogstreet.model.QualificationModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.parser.ExpericenceParser;
import com.app.nirogstreet.parser.QualificationParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.Event_For_Firebase;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
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

/**
 * Created by Preeti on 30-08-2017.
 */

public class AddOrEditExperience extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static boolean isVisible = true;
    public static int year;
    TextView text_to;
    CheckBox checkBox;

    ImageView backImageView;

    private SesstionManager sesstionManager;
    UserDetailModel userDetailModel;
    int position = -1;
    boolean isFromDate = false;
    ImageView deleteImageView;
    EditText fromEt, toEt, cityEt, clinicOrhospital;
    TextView title_side_left;
    LinearLayout saveTv;
    AddOrUpdateQualificationAsynctask addOrUpdateQualificationAsynctask;
    DeleteQualificationAsynctask deleteQualificationAsynctask;
    CircularProgressBar circularProgressBar;
    private String authToken, userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra("userModel")) {
            userDetailModel = (UserDetailModel) getIntent().getSerializableExtra("userModel");
        }
        setContentView(R.layout.add_edit_experience);
        checkBox = (CheckBox) findViewById(R.id.checkbox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if (isChecked) {
                                                        toEt.setVisibility(View.GONE);
                                                        text_to.setVisibility(View.GONE);
                                                    } else {
                                                        toEt.setVisibility(View.VISIBLE);
                                                        text_to.setVisibility(View.VISIBLE);


                                                    }
                                                }
                                            }
        );
        deleteImageView = (ImageView) findViewById(R.id.delete);
        text_to = (TextView) findViewById(R.id.text_to);
        sesstionManager = new SesstionManager(AddOrEditExperience.this);
        if (sesstionManager.isUserLoggedIn()) {
            authToken = sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN);
            userId = sesstionManager.getUserDetails().get(SesstionManager.USER_ID);
        }
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sesstionManager = new SesstionManager(AddOrEditExperience.this);
        title_side_left = (TextView) findViewById(R.id.title_side_left);
        fromEt = (EditText) findViewById(R.id.from);
        toEt = (EditText) findViewById(R.id.to);
        cityEt = (EditText) findViewById(R.id.city);
        clinicOrhospital = (EditText) findViewById(R.id.hospital);
        saveTv = (LinearLayout) findViewById(R.id.saveTv);
        fromEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isFromDate = true;
                if (isVisible)
                    show();
                isVisible = false;

                return false;
            }
        });
        toEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isFromDate = false;
                if (isVisible)
                    show();
                isVisible = false;
                return false;
            }
        });

        if (getIntent().hasExtra("pos")) {
            position = getIntent().getIntExtra("pos", -1);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userDetailModel != null && userDetailModel.getExperinceModels() != null && userDetailModel.getExperinceModels().size() > 0 && position != -1)

        {

            final ExperinceModel experinceModel = userDetailModel.getExperinceModels().get(position);

            clinicOrhospital.setText(experinceModel.getOrganizationName());
            fromEt.setText(experinceModel.getStart_time());
            toEt.setText(experinceModel.getEnd_time());
            cityEt.setText(experinceModel.getAddress());
            if (experinceModel.getEnd_time() == null) {
                checkBox.setChecked(true);
                toEt.setVisibility(View.GONE);
                text_to.setVisibility(View.GONE);
            }

            saveTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtill.isNetworkAvailable(AddOrEditExperience.this)) {
                        if (validate()) {
                            Event_For_Firebase.getEventCount(AddOrEditExperience.this, "Feed_Profile_UserProfile_Experiance_Save_Click");
                            Event_For_Firebase.logAppEventsLoggerEvent(AddOrEditExperience.this,"Feed_Profile_UserProfile_Experiance_Save_Click");
                            addOrUpdateQualificationAsynctask = new AddOrUpdateQualificationAsynctask(fromEt.getText().toString(), toEt.getText().toString(), cityEt.getText().toString(), clinicOrhospital.getText().toString(), experinceModel.getId());
                            addOrUpdateQualificationAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    } else {
                        NetworkUtill.showNoInternetDialog(AddOrEditExperience.this);
                    }
                }
            });
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtill.isNetworkAvailable(AddOrEditExperience.this)) {
                        deleteQualificationAsynctask = new DeleteQualificationAsynctask(experinceModel.getId());
                        deleteQualificationAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            });

        } else {
            title_side_left.setText("Add Experience");
            deleteImageView.setVisibility(View.GONE);
            saveTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtill.isNetworkAvailable(AddOrEditExperience.this)) {
                        if (validate()) {
                            addOrUpdateQualificationAsynctask = new AddOrUpdateQualificationAsynctask(fromEt.getText().toString(), toEt.getText().toString(), cityEt.getText().toString(), clinicOrhospital.getText().toString(), "");
                            addOrUpdateQualificationAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    } else {
                        NetworkUtill.showNoInternetDialog(AddOrEditExperience.this);
                    }
                }
            });
        }
    }

    public void show() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

    /*MonthYearPickerDialog pd = new MonthYearPickerDialog();
    pd.setListener((View.OnClickListener) this);
    pd.show(getFragmentManager(), "MonthYearPickerDialog");*/
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        MonthYearPickerDialog newFragment = new MonthYearPickerDialog();
        newFragment.setCancelable(false);

        newFragment.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                System.out.print(year);
                isVisible = true;
                if (isFromDate)
                    fromEt.setText(year + "");
                else
                    toEt.setText(year + "");

                // yearEditText.setText(year + "");
            }
        });
        if (isVisible) {
            isVisible = false;
            newFragment.show(fm, "date");
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
                            isVisible = true;

                        }
                    });
            return builder.create();
        }
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

    public class AddOrUpdateQualificationAsynctask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String startTime, endTime, city, qrganisation_name, year, id;
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

        public AddOrUpdateQualificationAsynctask(String startTime, String endTime, String city, String organisation_name, String id) {

            this.startTime = startTime;
            this.id = id;
            this.endTime = endTime;
            this.qrganisation_name = organisation_name;
            this.city = city;


        }

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/experiences";
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
                pairs.add(new BasicNameValuePair("Experience[start_time]", startTime));
                pairs.add(new BasicNameValuePair("Experience[address]", city));
                pairs.add(new BasicNameValuePair("Experience[org_name]", qrganisation_name));
                if (checkBox.isChecked()) {
                    pairs.add(new BasicNameValuePair("Experience[currently_working]", "1"));
                } else {
                    pairs.add(new BasicNameValuePair("Experience[end_time]", endTime));

                }
                if (position != -1)
                    pairs.add(new BasicNameValuePair("Experience[id]", id));
                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                httppost.setHeader("Authorization", "Basic " + authToken);

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
                                        Toast.makeText(AddOrEditExperience.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (dataJsonObject.has("profile_complete") && !dataJsonObject.isNull("profile_complete")) {
                                    ApplicationSingleton.getUserDetailModel().setProfile_complete(dataJsonObject.getInt("profile_complete"));
                                }
                                if (dataJsonObject.has("experiences") && !dataJsonObject.isNull("experiences")) {
                                    UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                                    ArrayList<ExperinceModel> experinceModels = ExpericenceParser.experienceParser(dataJsonObject);

                                    userDetailModel.setExperinceModels(experinceModels);
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    ApplicationSingleton.setIsExperinceUpdated(true);
                                }
                                if (dataJsonObject.has("status_message") && !dataJsonObject.isNull("status_message")) {
                                    Toast.makeText(AddOrEditExperience.this, dataJsonObject.getString("status_message"), Toast.LENGTH_SHORT).show();

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
        if (fromEt.getText().toString().length() == 0) {
            Toast.makeText(AddOrEditExperience.this, R.string.start_year, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!checkBox.isChecked() && toEt.getText().toString().length() == 0) {
            Toast.makeText(AddOrEditExperience.this, R.string.end_year, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cityEt.getText().toString().length() == 0) {
            Toast.makeText(AddOrEditExperience.this, R.string.address, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!checkBox.isChecked() && Integer.parseInt(fromEt.getText().toString()) > Integer.parseInt(toEt.getText().toString())) {
            Toast.makeText(AddOrEditExperience.this, "Invalid Year", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (clinicOrhospital.getText().toString().length() == 0) {
            Toast.makeText(AddOrEditExperience.this, R.string.HospitalorClinic, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public class DeleteQualificationAsynctask extends AsyncTask<Void, Void, Void> {
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

        public DeleteQualificationAsynctask(String id) {

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

                String url = AppUrl.AppBaseUrl + "user/delete-experience";
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
                pairs.add(new BasicNameValuePair("Experience[id]", id));
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
                                        Toast.makeText(AddOrEditExperience.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (dataJsonObject.has("profile_complete") && !dataJsonObject.isNull("profile_complete")) {
                                    ApplicationSingleton.getUserDetailModel().setProfile_complete(dataJsonObject.getInt("profile_complete"));
                                }
                                if (dataJsonObject.has("qualifications") && !dataJsonObject.isNull("qualifications")) {
                                    UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                                    ArrayList<ExperinceModel> experinceModels = ExpericenceParser.experienceParser(dataJsonObject);

                                    userDetailModel.setExperinceModels(experinceModels);
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    ApplicationSingleton.setIsExperinceUpdated(true);
                                } else {
                                    UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                                    ArrayList<ExperinceModel> experinceModels = null;

                                    userDetailModel.setExperinceModels(experinceModels);
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    ApplicationSingleton.setIsExperinceUpdated(true);
                                }

                                if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                                    Toast.makeText(AddOrEditExperience.this, dataJsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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

}
