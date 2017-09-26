package com.app.nirogstreet.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.QualificationModel;
import com.app.nirogstreet.model.SpecializationModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.parser.QualificationParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.PathUtil;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.google.firebase.iid.FirebaseInstanceId;

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

/**
 * Created by Preeti on 25-08-2017.
 */

public class EditQualificationDetailOrAddQualificationsDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static int year;
    static boolean isVisible = true;
    int position = -1;
    ImageView backImageView;
    String docpath = null;
    boolean isQualificationTouched=false;
    SesstionManager sesstionManager;
    AddOrUpdateQualificationAsynctask addOrUpdateQualificationAsynctask;
    DeleteQualificationAsynctask deleteQualificationAsynctask;

    LinearLayout updateDocLinearLayout;
    RelativeLayout EditDocRelativeLayout;
    private static final int RESULT_CODE = 1;
    ImageView deleteImageView;
    TextView docNameTv, uploadDoctv, add;
    EditText yearEditText, clgEt, degree_name, sepcialization;
    private int STORAGE_PERMISSION_CODE_DOCUMENT = 3;
    int REQUEST_CODE = 4;
    UserDetailModel userDetailModel;

    CircularProgressBar circularProgressBar;
    TextView title_side_left, saveTv;
    private ArrayList<SpecializationModel> multipleSelectedItemModels;
    private String authToken, userId;

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
        deleteImageView = (ImageView) findViewById(R.id.delete);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        updateDocLinearLayout = (LinearLayout) findViewById(R.id.uploadDoc);
        EditDocRelativeLayout = (RelativeLayout) findViewById(R.id.EditDoc);
        docNameTv = (TextView) findViewById(R.id.docNameTv);
        uploadDoctv = (TextView) findViewById(R.id.uploadDoctv);
        add = (TextView) findViewById(R.id.add);
        sesstionManager = new SesstionManager(EditQualificationDetailOrAddQualificationsDetails.this);
        if (sesstionManager.isUserLoggedIn()) {
            authToken = sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN);
            userId = sesstionManager.getUserDetails().get(SesstionManager.USER_ID);
        }
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
        sepcialization = (EditText) findViewById(R.id.sepcialization);

        saveTv = (TextView) findViewById(R.id.saveTv);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(uploadDoctv, EditQualificationDetailOrAddQualificationsDetails.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(docNameTv, EditQualificationDetailOrAddQualificationsDetails.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(add, EditQualificationDetailOrAddQualificationsDetails.this);

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
clgEt.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(!isQualificationTouched) {
            isQualificationTouched=true;
            Intent intent = new Intent(EditQualificationDetailOrAddQualificationsDetails.this, SingleSelectQualifications.class);
            startActivityForResult(intent, RESULT_CODE);
        }
        return false;
    }
});

        if (userDetailModel != null && userDetailModel.getQualificationModels() != null && userDetailModel.getQualificationModels().size() > 0 && position != -1)

        {
            if (userDetailModel.getSpecializationModels() != null && userDetailModel.getSpecializationModels().size() > 0) {
                sepcialization.setText(getSelectedNameCsv());
            }
            final QualificationModel qualificationModel = userDetailModel.getQualificationModels().get(position);

            degree_name.setText(qualificationModel.getClgName());
            clgEt.setText(qualificationModel.getDegreeName());
            yearEditText.setText(qualificationModel.getPassingYear());
            if (qualificationModel.getUpladedDoc() != null) {
                EditDocRelativeLayout.setVisibility(View.VISIBLE);
                updateDocLinearLayout.setVisibility(View.GONE);
                docNameTv.setText(qualificationModel.getUpladedDoc());
            } else {
                EditDocRelativeLayout.setVisibility(View.GONE);
                updateDocLinearLayout.setVisibility(View.VISIBLE);
            }
            title_side_left.setText("Edit Qualification");
            sepcialization.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditQualificationDetailOrAddQualificationsDetails.this, Multi_Select_Search_specialization.class);
                    if (multipleSelectedItemModels != null && multipleSelectedItemModels.size() > 0)
                        intent.putExtra("list", multipleSelectedItemModels);

                    else if (userDetailModel.getSpecializationModels() != null && userDetailModel.getSpecializationModels().size() > 0)
                        intent.putExtra("list", userDetailModel.getSpecializationModels());

                    startActivityForResult(intent, RESULT_CODE);
                }
            });
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtill.isNetworkAvailable(EditQualificationDetailOrAddQualificationsDetails.this)) {


                        deleteQualificationAsynctask= new DeleteQualificationAsynctask( qualificationModel.getId());
                        deleteQualificationAsynctask.execute();


                    } else {
                        NetworkUtill.showNoInternetDialog(EditQualificationDetailOrAddQualificationsDetails.this);
                    }
                }
            });
            saveTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtill.isNetworkAvailable(EditQualificationDetailOrAddQualificationsDetails.this)) {
                        if (validate()) {
                            String id = "";

                            id = qualificationModel.getId();

                            AddOrUpdateQualificationAsynctask addOrUpdateQualificationAsynctask = new AddOrUpdateQualificationAsynctask(degree_name.getText().toString(), yearEditText.getText().toString(), clgEt.getText().toString(), id);
                            addOrUpdateQualificationAsynctask.execute();
                        }

                    } else {
                        NetworkUtill.showNoInternetDialog(EditQualificationDetailOrAddQualificationsDetails.this);
                    }
                }
            });
        } else {
            title_side_left.setText("Add Qualification");
            clgEt.setText(" ");
            deleteImageView.setVisibility(View.GONE);
            EditDocRelativeLayout.setVisibility(View.GONE);
            sepcialization.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditQualificationDetailOrAddQualificationsDetails.this, Multi_Select_Search_specialization.class);
                    startActivityForResult(intent, RESULT_CODE);

                }
            });
            saveTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtill.isNetworkAvailable(EditQualificationDetailOrAddQualificationsDetails.this)) {
                        if (validate()) {


                            AddOrUpdateQualificationAsynctask addOrUpdateQualificationAsynctask = new AddOrUpdateQualificationAsynctask(degree_name.getText().toString(), yearEditText.getText().toString(), clgEt.getText().toString(), "");
                            addOrUpdateQualificationAsynctask.execute();
                        }

                    } else {
                        NetworkUtill.showNoInternetDialog(EditQualificationDetailOrAddQualificationsDetails.this);
                    }
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        isQualificationTouched=false;

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
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                Uri data1 = data.getData();
                // String pathe = data1.getPath();
                // path = getRealPathFromURI_API19(getActivity(), data1);
                try {
                    String path = PathUtil.getPath(EditQualificationDetailOrAddQualificationsDetails.this, data1);

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
                                Toast.makeText(EditQualificationDetailOrAddQualificationsDetails.this, "Not Supported", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(EditQualificationDetailOrAddQualificationsDetails.this, "Not Supported", Toast.LENGTH_LONG).show();
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }

        }
        if (requestCode == RESULT_CODE) {
            if (data != null) {
                String s = data.getStringExtra("friendsCsv");
                clgEt.setText(s);
                System.out.print(s);
                multipleSelectedItemModels = (ArrayList<SpecializationModel>) data.getSerializableExtra("list");
            }
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
            Toast.makeText(EditQualificationDetailOrAddQualificationsDetails.this, "Select Qualification.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (degree_name.getText().toString().length() == 0) {
            Toast.makeText(EditQualificationDetailOrAddQualificationsDetails.this, "Enter University name.", Toast.LENGTH_SHORT).show();
            return false;
        }
       /* if (sepcialization.getText().toString().length() == 0) {
            Toast.makeText(EditQualificationDetailOrAddQualificationsDetails.this, "Select Specialization.", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        if (yearEditText.getText().toString().length() == 0) {
            Toast.makeText(EditQualificationDetailOrAddQualificationsDetails.this, "Select Year.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public class AddOrUpdateQualificationAsynctask extends AsyncTask<Void, Void, Void> {
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

        public AddOrUpdateQualificationAsynctask(String university, String year, String qualification, String id) {

            this.university = university;
            this.id = id;
            this.qualification = qualification;
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

                String url = AppUrl.AppBaseUrl + "user/education";
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
                entityBuilder.addTextBody("Qualification[college]", university);
                entityBuilder.addTextBody("Qualification[year]", year);
                entityBuilder.addTextBody("Qualification[degree]", qualification);
                if (position != -1)
                    entityBuilder.addTextBody("Qualification[id]", id);
                if (docpath != null && docpath.toString().trim().length() > 0) {
                    File file = new File(docpath);
                    FileBody encFile = new FileBody(file);
                    entityBuilder.addPart("Qualification[uploadFile]", encFile);
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
                                        Toast.makeText(EditQualificationDetailOrAddQualificationsDetails.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (dataJsonObject.has("qualifications") && !dataJsonObject.isNull("qualifications")) {
                                    UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                                    ArrayList<QualificationModel> qualificationModels = QualificationParser.qualificationParser(dataJsonObject);

                                    userDetailModel.setQualificationModels(qualificationModels);
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    ApplicationSingleton.setIsQualificationUpdated(true);
                                }
                                if (dataJsonObject.has("status_message") && !dataJsonObject.isNull("status_message")) {
                                    Toast.makeText(EditQualificationDetailOrAddQualificationsDetails.this, dataJsonObject.getString("status_message"), Toast.LENGTH_SHORT).show();

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

                String url = AppUrl.AppBaseUrl + "user/delete-qualification";
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
                pairs.add(new BasicNameValuePair("Qualification[id]", id));
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
                                        Toast.makeText(EditQualificationDetailOrAddQualificationsDetails.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (dataJsonObject.has("qualifications") && !dataJsonObject.isNull("qualifications")) {
                                    UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                                    ArrayList<QualificationModel> qualificationModels = QualificationParser.qualificationParser(dataJsonObject);

                                    userDetailModel.setQualificationModels(qualificationModels);
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    ApplicationSingleton.setIsQualificationUpdated(true);
                                }else {
                                    UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();

                                    ArrayList<QualificationModel> qualificationModels = null;

                                    userDetailModel.setQualificationModels(qualificationModels);
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    ApplicationSingleton.setIsQualificationUpdated(true);
                                }

                                if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                                    Toast.makeText(EditQualificationDetailOrAddQualificationsDetails.this, dataJsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onPause() {
        super.onPause();
        if(addOrUpdateQualificationAsynctask!=null&&!addOrUpdateQualificationAsynctask.isCancelled())
            addOrUpdateQualificationAsynctask.cancelAsyncTask();
        if(deleteQualificationAsynctask!=null&&!deleteQualificationAsynctask.isCancelled())
            deleteQualificationAsynctask.cancelAsyncTask();
    }
}
