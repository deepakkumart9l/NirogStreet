package com.app.nirogstreet.activites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.AwardsModel;
import com.app.nirogstreet.model.MemberShipModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.parser.AwardsParser;
import com.app.nirogstreet.parser.MemberShipParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import org.json.JSONArray;
import org.json.JSONObject;

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
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by Preeti on 07-09-2017.
 */
public class AddOrEditMemberShip extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText yearEditText;
    public static int year;
    ImageView backImageView, deleteImageView;
    int position = -1;
    CircularProgressBar circularProgressBar;
    static boolean isVisible = true;
    TextView title_side_left, saveTv;
    private SesstionManager sesstionManager;
    private String authToken, userId;
    DeleteMemberShipAsynctask deleteMemberShipAsynctask;
    private UserDetailModel userDetailModel;
    AddOrUpdateMembershipAsynctask addOrUpdateMembershipAsynctask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_membership);
        sesstionManager = new SesstionManager(AddOrEditMemberShip.this);
        if (sesstionManager.isUserLoggedIn()) {
            authToken = sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN);
            userId = sesstionManager.getUserDetails().get(SesstionManager.USER_ID);
        }
        deleteImageView = (ImageView) findViewById(R.id.delete);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        title_side_left = (TextView) findViewById(R.id.title_side_left);

        if (getIntent().hasExtra("userModel")) {
            userDetailModel = (UserDetailModel) getIntent().getSerializableExtra("userModel");
        }
        saveTv = (TextView) findViewById(R.id.saveTv);
        yearEditText = (EditText) findViewById(R.id.year);
        backImageView = (ImageView) findViewById(R.id.back);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        yearEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
              /*  InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(yearEditText.getWindowToken(), 0);
                show();*/
                return false;
            }
        });
        if (getIntent().hasExtra("pos")) {
            position = getIntent().getIntExtra("pos", -1);

        }
        if (userDetailModel != null && userDetailModel.getMemberShipModels() != null && userDetailModel.getMemberShipModels().size() > 0 && position != -1)

        {

            final MemberShipModel memberShipModel = userDetailModel.getMemberShipModels().get(position);

            yearEditText.setText(memberShipModel.getMembership());
            saveTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtill.isNetworkAvailable(AddOrEditMemberShip.this)) {
                        if(validate()) {

                            addOrUpdateMembershipAsynctask = new AddOrUpdateMembershipAsynctask(yearEditText.getText().toString(), memberShipModel.getId());
                            addOrUpdateMembershipAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    } else {
                        NetworkUtill.showNoInternetDialog(AddOrEditMemberShip.this);
                    }
                }
            });
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtill.isNetworkAvailable(AddOrEditMemberShip.this)) {
                        deleteMemberShipAsynctask = new DeleteMemberShipAsynctask(memberShipModel.getId());
                        deleteMemberShipAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        NetworkUtill.showNoInternetDialog(AddOrEditMemberShip.this);
                    }
                }
            });

        } else {
            title_side_left.setText("Add Membership");
            saveTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtill.isNetworkAvailable(AddOrEditMemberShip.this)) {
                        if(validate()) {
                            addOrUpdateMembershipAsynctask = new AddOrUpdateMembershipAsynctask(yearEditText.getText().toString(), "");
                            addOrUpdateMembershipAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    } else {
                        NetworkUtill.showNoInternetDialog(AddOrEditMemberShip.this);
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

    public boolean validate() {


        if (yearEditText.getText().toString().length() == 0) {
            Toast.makeText(AddOrEditMemberShip.this, R.string.membership, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public class AddOrUpdateMembershipAsynctask extends AsyncTask<Void, Void, Void> {
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

        public AddOrUpdateMembershipAsynctask(String year, String id) {

            this.id = id;
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

                String url = AppUrl.AppBaseUrl + "user/membership";
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
                entityBuilder.addTextBody("Membership[membership]", year);
                if (position != -1)
                    entityBuilder.addTextBody("Membership[id]", id);


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
                                        Toast.makeText(AddOrEditMemberShip.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if(dataJsonObject.has("profile_complete")&&!dataJsonObject.isNull("profile_complete"))
                                {
                                    ApplicationSingleton.getUserDetailModel().setProfile_complete(dataJsonObject.getInt("profile_complete"));
                                }
                                if (dataJsonObject.has("membership") && !dataJsonObject.isNull("membership")) {
                                    UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                                    ArrayList<MemberShipModel> memberShips
                                            = MemberShipParser.memberShipParser(dataJsonObject);

                                    userDetailModel.setMemberShipModels(memberShips);
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    ApplicationSingleton.setIsMemberShipUpdated(true);
                                }
                                if (dataJsonObject.has("status_message") && !dataJsonObject.isNull("status_message")) {
                                    Toast.makeText(AddOrEditMemberShip.this, dataJsonObject.getString("status_message"), Toast.LENGTH_SHORT).show();

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

    public class DeleteMemberShipAsynctask extends AsyncTask<Void, Void, Void> {
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

        public DeleteMemberShipAsynctask(String id) {

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

                String url = AppUrl.AppBaseUrl + "user/delete-membership";
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
                pairs.add(new BasicNameValuePair("Membership[id]", id));
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
                                        Toast.makeText(AddOrEditMemberShip.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if(dataJsonObject.has("profile_complete")&&!dataJsonObject.isNull("profile_complete"))
                                {
                                    ApplicationSingleton.getUserDetailModel().setProfile_complete(dataJsonObject.getInt("profile_complete"));
                                }
                                if (dataJsonObject.has("membership") && !dataJsonObject.isNull("membership")) {
                                    UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                                    ArrayList<MemberShipModel> memberShipModels = MemberShipParser.memberShipParser(dataJsonObject);

                                    userDetailModel.setMemberShipModels(memberShipModels);
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    ApplicationSingleton.setIsMemberShipUpdated(true);
                                } else {
                                    UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                                    ArrayList<MemberShipModel> memberShipModels = MemberShipParser.memberShipParser(dataJsonObject);;

                                    userDetailModel.setMemberShipModels(memberShipModels);
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    ApplicationSingleton.setIsMemberShipUpdated(true);
                                }

                                if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                                    Toast.makeText(AddOrEditMemberShip.this, dataJsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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
