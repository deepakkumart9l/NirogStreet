package com.app.nirogstreet.activites;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.app.nirogstreet.model.AwardsModel;
import com.app.nirogstreet.model.RegistrationAndDocumenModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.parser.AwardsParser;
import com.app.nirogstreet.parser.RegistrationParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

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
 * Created by Preeti on 07-09-2017.
 */
public class AddOrEditAward extends AppCompatActivity {
    public static int year;
    static boolean isVisible = true;
    ImageView deleteImageView;
    EditText yearEditText, clgEt, degree_name, sepcialization;
    private int STORAGE_PERMISSION_CODE_DOCUMENT = 3;
    int REQUEST_CODE = 4;
    CircularProgressBar circularProgressBar;
    DeleteAwardAsynctask deleteAwardAsynctask;
    ImageView backImageView;
    AddOrUpdateAwardsAsynctask addOrUpdateAwardsAsynctask;
    TextView title_side_left, saveTv;
    private UserDetailModel userDetailModel;
    private int position = -1;
    private SesstionManager sesstionManager;
    private String authToken, userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_award);
        if (getIntent().hasExtra("userModel")) {
            userDetailModel = (UserDetailModel) getIntent().getSerializableExtra("userModel");
        }
        deleteImageView = (ImageView) findViewById(R.id.delete);
        sesstionManager = new SesstionManager(AddOrEditAward.this);
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
        title_side_left = (TextView) findViewById(R.id.title_side_left);
        yearEditText = (EditText) findViewById(R.id.year);
        degree_name = (EditText) findViewById(R.id.degree_name);
        clgEt = (EditText) findViewById(R.id.clgEt);
        saveTv = (TextView) findViewById(R.id.saveTv);
        TypeFaceMethods.setRegularTypeFaceEditText(yearEditText, AddOrEditAward.this);

        TypeFaceMethods.setRegularTypeFaceEditText(degree_name, AddOrEditAward.this);

        TypeFaceMethods.setRegularTypeFaceEditText(clgEt, AddOrEditAward.this);

        TypeFaceMethods.setRegularTypeFaceForTextView(title_side_left, AddOrEditAward.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(saveTv, AddOrEditAward.this);

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
        if (userDetailModel != null && userDetailModel.getAwardsModels() != null && userDetailModel.getAwardsModels().size() > 0 && position != -1)

        {

            final AwardsModel awardsModel = userDetailModel.getAwardsModels().get(position);

            degree_name.setText(awardsModel.getAwardName());
            yearEditText.setText(awardsModel.getYear());
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtill.isNetworkAvailable(AddOrEditAward.this)) {
                        deleteAwardAsynctask=new DeleteAwardAsynctask(awardsModel.getId());
                        deleteAwardAsynctask.execute();
                    } else {
                        NetworkUtill.showNoInternetDialog(AddOrEditAward.this);

                    }
                }
            });
            saveTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtill.isNetworkAvailable(AddOrEditAward.this)) {
                        if (validate()) {
                            addOrUpdateAwardsAsynctask = new AddOrUpdateAwardsAsynctask(degree_name.getText().toString(), yearEditText.getText().toString(), awardsModel.getId());
                            addOrUpdateAwardsAsynctask.execute();
                        }

                    } else {
                        NetworkUtill.showNoInternetDialog(AddOrEditAward.this);
                    }
                }
            });

        } else {
            saveTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtill.isNetworkAvailable(AddOrEditAward.this)) {
                        if (validate()) {
                            addOrUpdateAwardsAsynctask = new AddOrUpdateAwardsAsynctask(degree_name.getText().toString(), yearEditText.getText().toString(), "");
                            addOrUpdateAwardsAsynctask.execute();
                        }

                    } else {
                        NetworkUtill.showNoInternetDialog(AddOrEditAward.this);
                    }
                }
            });
            deleteImageView.setVisibility(View.GONE);
            title_side_left.setText("Add Award");
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

                yearEditText.setText(year + "");
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
            LayoutInflater inflater = getActivity().getLayoutInflater();

            Calendar cal = Calendar.getInstance();

            View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
            final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);



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
    public class DeleteAwardAsynctask extends AsyncTask<Void, Void, Void> {
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

        public DeleteAwardAsynctask(String id) {

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

                String url = AppUrl.AppBaseUrl + "user/delete-award";
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
                pairs.add(new BasicNameValuePair("Award[id]", id));
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
                                        Toast.makeText(AddOrEditAward.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if(dataJsonObject.has("profile_complete")&&!dataJsonObject.isNull("profile_complete"))
                                {
                                    ApplicationSingleton.getUserDetailModel().setProfile_complete(dataJsonObject.getInt("profile_complete"));
                                }
                                if (dataJsonObject.has("awards") && !dataJsonObject.isNull("awards")) {
                                    UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                                    ArrayList<AwardsModel> awardsModels = AwardsParser.awardsParser(dataJsonObject);

                                    userDetailModel.setAwardsModels(awardsModels);
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    ApplicationSingleton.setIsAwardUpdated(true);
                                }else {
                                    UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                                    ArrayList<AwardsModel> awardsModels = null;

                                    userDetailModel.setAwardsModels(awardsModels);
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    ApplicationSingleton.setIsAwardUpdated(true);
                                }

                                if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                                    Toast.makeText(AddOrEditAward.this, dataJsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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

    public class AddOrUpdateAwardsAsynctask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String type, awardname, registraionNumber, id, year;
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

        public AddOrUpdateAwardsAsynctask(String awardname, String year, String id) {

            this.type = type;
            this.id = id;
            this.awardname = awardname;
            this.year = year;

        }

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/awards";
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
                entityBuilder.addTextBody("Awards[award_name]", awardname);
                entityBuilder.addTextBody("Awards[year]", year);
                if (position != -1)
                    entityBuilder.addTextBody("Awards[id]", id);


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
                                        Toast.makeText(AddOrEditAward.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if(dataJsonObject.has("profile_complete")&&!dataJsonObject.isNull("profile_complete"))
                                {
                                    ApplicationSingleton.getUserDetailModel().setProfile_complete(dataJsonObject.getInt("profile_complete"));
                                }
                                if (dataJsonObject.has("awards") && !dataJsonObject.isNull("awards")) {
                                    UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                                    ArrayList<AwardsModel> awardsModels
                                            = AwardsParser.awardsParser(dataJsonObject);

                                    userDetailModel.setAwardsModels(awardsModels);
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    ApplicationSingleton.setIsAwardUpdated(true);
                                }
                                if (dataJsonObject.has("status_message") && !dataJsonObject.isNull("status_message")) {
                                    Toast.makeText(AddOrEditAward.this, dataJsonObject.getString("status_message"), Toast.LENGTH_SHORT).show();

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

        if (degree_name.getText().toString().length() == 0) {
            Toast.makeText(AddOrEditAward.this, R.string.Award_name, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (yearEditText.getText().toString().length() == 0) {
            Toast.makeText(AddOrEditAward.this, R.string.year, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
