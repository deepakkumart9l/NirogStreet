package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.AllClinicModel;
import com.app.nirogstreet.model.SpecializationModel;
import com.app.nirogstreet.parser.ServicesParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ImageLoader;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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
 * Created by Preeti on 23-11-2017.
 */

public class AllClinicSearch extends Activity {
    private RecyclerView recyclerViewsearchData;
    ImageView imageViewSearch;
    ImageLoader imageLoader;
    ArrayList<AllClinicModel> allClinicModels;
    private static final int RESULT_CODE = 1;
    TextView addQualificationTextView;
    ImageView backImageView;
    private ArrayList<AllClinicModel> list;
    SearchAdapterMultiSelect searchAdapterMultiSelect;
    EditText searchET;
    SearchAsync searchAsync;
    SesstionManager sessionManager;
     ArrayList<AllClinicModel> allClinicModels1 = new ArrayList<>();

    CircularProgressBar circularProgressBar;
    // CollapsingToolbarLayout collapsingToolbarLayout;
    TextView textViewDone, specilization;
    private String text = "";
    private String query = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_select_search);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }


       /* scoresRef.orderByValue().limitToLast(4).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                System.out.println("The " + snapshot.getKey() + " dinosaur's score is " + snapshot.getValue());
            }
        });
       */
        addQualificationTextView = (TextView) findViewById(R.id.addQualification);
        specilization = (TextView) findViewById(R.id.specilization);
        specilization.setText("Select your Clinic");
        sessionManager = new SesstionManager(AllClinicSearch.this);

        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        //  collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        final Intent intent = getIntent();
        if (intent.hasExtra("list")) {
            list = (ArrayList<AllClinicModel>) intent.getSerializableExtra("list");
            // collapsingToolbarLayout.setVisibility(View.VISIBLE);

        } else {
            list = new ArrayList<>();

        }
        searchET = (EditText) findViewById(R.id.searchET);
        searchET.setHint("Search Clinic");
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        recyclerViewsearchData = (RecyclerView) findViewById(R.id.recyclerview);
        textViewDone = (TextView) findViewById(R.id.done);

        textViewDone.setVisibility(View.GONE);
        textViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = getSelectedNameCsv();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchET.getWindowToken(), 0);
                if (s.equalsIgnoreCase("")) {
                    Toast.makeText(AllClinicSearch.this, "Select Clinic.", Toast.LENGTH_SHORT).show();
                } else {

                    Intent return_intent = new Intent();
                    return_intent.putExtra("friendsCsv", s);
                    return_intent.putExtra("list", (Serializable) list);
                    setResult(RESULT_CODE, return_intent);
                    finish();
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewsearchData.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AllClinicSearch.this, LinearLayoutManager.HORIZONTAL, false);


       /* searchET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                addQualificationTextView.setVisibility(View.GONE);
                text = s.toString();
                query = text;


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub


            }
            Timer timer = new Timer();
            final long delay = 2000;
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub


                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                // Whatever you want to do
                                (AllClinicSearch.this).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (searchET.getText().toString().length() == 0) {
                                            if (NetworkUtill.isNetworkAvailable(AllClinicSearch.this)) {
                                                searchAsync = new SearchAsync("");
                                                searchAsync.execute();
                                            } else {
                                                NetworkUtill.showNoInternetDialog(AllClinicSearch.this);
                                            }
                                        } else {
                                            if (NetworkUtill.isNetworkAvailable(AllClinicSearch.this)) {
                                                searchAsync = new SearchAsync(searchET.getText().toString());
                                                searchAsync.execute();
                                            } else {
                                                NetworkUtill.showNoInternetDialog(AllClinicSearch.this);
                                            }

                                        }
                                    }
                                });
                            }
                        }, delay
                );
            }
        });
*/

        searchET.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        addQualificationTextView.setVisibility(View.GONE);
                        text = s.toString();
                        query = text;
                        if (allClinicModels1 != null) {
                            recyclerViewsearchData.setAdapter(null);
                        }
                        if (searchAsync != null)
                            searchAsync.cancel(true);
                        // setVisibilty(0);
                        if (NetworkUtill.isNetworkAvailable(AllClinicSearch.this)) {
                            if (searchET.getText().toString().length() == 0) {
                                searchAsync = new SearchAsync("");
                                searchAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                searchAsync = new SearchAsync(searchET.getText().toString());
                                searchAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }
                        } else {
                            NetworkUtill.showNoInternetDialog(AllClinicSearch.this);
                        }


                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    private Timer timer = new Timer();
                    private final long delay = 2000; // time delay in milliseconds

                    @Override
                    public void afterTextChanged(final Editable s) {

                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        // Whatever you want to do
                                        (AllClinicSearch.this).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                            }
                                        });
                                    }
                                }, delay
                        );
                    }
                }
        );

      /*  if (NetworkUtill.isNetworkAvailable(SingleSelectQualifications.this)) {
            if (searchET.getText().toString().length() == 0) {
                searchAsync = new SearchAsync("");
                searchAsync.execute();
            } else {
                searchAsync = new SearchAsync(searchET.getText().toString());
                searchAsync.execute();
            }
        } else {
            NetworkUtill.showNoInternetDialog(SingleSelectQualifications.this);
        }
      */
        if (NetworkUtill.isNetworkAvailable(AllClinicSearch.this)) {
            if (searchET.getText().toString().length() == 0) {
                searchAsync = new SearchAsync("");
                searchAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                searchAsync = new SearchAsync(searchET.getText().toString());
                searchAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } else {
            NetworkUtill.showNoInternetDialog(AllClinicSearch.this);
        }
        addQualificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = searchET.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchET.getWindowToken(), 0);
                if (s.length() == 0 && s.equalsIgnoreCase(" ")) {
                    Toast.makeText(AllClinicSearch.this, "Select Clinic", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent1 = new Intent(AllClinicSearch.this, AddOrEditClinicDetail.class);
                    intent1.putExtra("clinic_Name", s);
                    startActivity(intent1);
                    finish();
                }
            }
        });
        searchAdapterMultiSelect = new SearchAdapterMultiSelect(AllClinicSearch.this, allClinicModels1);
        recyclerViewsearchData.setAdapter(searchAdapterMultiSelect);
        recyclerViewsearchData.setVisibility(View.VISIBLE);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AllClinicSearch Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    public class SearchAsync extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String email, password;
        CircularProgressBar bar;
        //PlayServiceHelper regId;
        String strTobeSearch;
        JSONObject jo;
        HttpClient client;

        public SearchAsync(String str) {
            strTobeSearch = str;
        }

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);
            //  bar = (ProgressBar) findViewById(R.id.progressBar);
            //   bar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/all-clinics";
                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();

                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;

                String credentials = email + ":" + password;

                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));

                pairs.add(new BasicNameValuePair("searchKey", strTobeSearch));

                httppost.setEntity(new UrlEncodedFormEntity(pairs));
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
      allClinicModels1    = new ArrayList<>();

            try {
                if (jo != null) {
                    String nextUri = null, authToken = null, userName = null, profileUrl = null, sepcializationName = null;
                    JSONObject jsonObject;
                    if (jo.has("data") && !jo.isNull("data"))

                    {

                        jsonObject = jo.getJSONObject("data");

                        if (jsonObject.has("allClinics") && !jsonObject.isNull("allClinics")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("allClinics");
recyclerViewsearchData.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ArrayList<SpecializationModel> specializationModels = new ArrayList<>();
                                String fname = "", state = "", slug = "", profile_pic = "",created_by="", department = "", id = "", address = "", pincode = "", city = "", locality = "";
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if (jsonObject1.has("clinic_name") && !jsonObject1.isNull("clinic_name")) {
                                    fname = jsonObject1.getString("clinic_name");
                                }
                                if (jsonObject1.has("id") && !jsonObject1.isNull("id")) {
                                    id = jsonObject1.getString("id");
                                }

                                if (jsonObject1.has("address") && !jsonObject1.isNull("address")) {
                                    address = jsonObject1.getString("address");
                                }
                                if (jsonObject1.has("locality") && !jsonObject1.isNull("locality")) {
                                    locality = jsonObject1.getString("locality");
                                }
                                if (jsonObject1.has("city") && !jsonObject1.isNull("city")) {
                                    city = jsonObject1.getString("city");
                                }
                                if (jsonObject1.has("state") && !jsonObject1.isNull("state")) {
                                    state = jsonObject1.getString("state");
                                }
                                if (jsonObject1.has("pincode") && !jsonObject1.isNull("pincode")) {
                                    pincode = jsonObject1.getString("pincode");
                                }
                                if(jsonObject1.has("created_by")&&!jsonObject1.isNull("created_by"))
                                {
                                    created_by=jsonObject1.getString("created_by");
                                }
                                specializationModels = ServicesParser.serviceParser(jsonObject1);
                                allClinicModels1.add(new AllClinicModel(fname, id, address, state, locality, city, pincode, specializationModels, "false","",created_by));

                            }

                        }else {

                            addQualificationTextView.setText("+Add "+"("+searchET.getText().toString()+")");
                            addQualificationTextView.setVisibility(View.VISIBLE);
                        }


                        if (allClinicModels1 != null && allClinicModels1.size() > 0) {
                            if (list != null && list.size() > 0) {
                                for (int k = 0; k < list.size(); k++) {

                                    for (int j = 0; j < allClinicModels1.size(); j++) {
                                        if (list.get(k).getId().equalsIgnoreCase(allClinicModels1.get(j).getId())) {
                                            allClinicModels1.get(j).setSelected("true");
                                        }
                                    }
                                }
                            }
                            //    mRecyclerView.setAdapter(new SearchAdapter(Multi_Select_Search.this, searchModels));

                            searchAdapterMultiSelect = new SearchAdapterMultiSelect(AllClinicSearch.this, allClinicModels1);
                            recyclerViewsearchData.setAdapter(searchAdapterMultiSelect);
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //  bar.setVisibility(View.GONE);

        }
    }

    public String getSelectedIdCsv() {
        String languageCSV = "";
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String language = list.get(i).getId();
                if (language != null && !language.trim().isEmpty()
                        && languageCSV != null && !languageCSV.trim().isEmpty())
                    languageCSV = languageCSV + ", ";
                languageCSV = languageCSV + language;

            }
        }
        return languageCSV;
    }

    public ArrayList<String> userIds() {
        ArrayList<String> strings = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            strings.add(list.get(i).getId());
        }
        return strings;
    }

    public String getSelectedNameCsv() {
        String languageCSV = "";
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String language = list.get(i).getClinic_name();
                if (language != null && !language.trim().isEmpty()
                        && languageCSV != null && !languageCSV.trim().isEmpty())
                    languageCSV = languageCSV + ", ";
                languageCSV = languageCSV + language;

            }
        }
        return languageCSV;
    }

    public class SearchAdapterMultiSelect extends RecyclerView.Adapter<SearchAdapterMultiSelect.MyHolderView> {
        Context context;
        ArrayList<AllClinicModel> multipleSelectedItemModels;

        public void updateList(ArrayList<AllClinicModel> list) {
            multipleSelectedItemModels = list;
            notifyDataSetChanged();
        }

        public SearchAdapterMultiSelect(Context context, ArrayList<AllClinicModel> allClinicModels) {
            this.multipleSelectedItemModels = allClinicModels;
            this.context = context;
        }

        @Override
        public MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_selected, parent, false);
            return new MyHolderView(v);
        }

        @Override
        public void onBindViewHolder(final MyHolderView holder, final int position) {
            final AllClinicModel multipleSelectedItemModel = multipleSelectedItemModels.get(position);
            if (multipleSelectedItemModel.getSelected().equalsIgnoreCase("true")) {
                holder.textViewInvite.setVisibility(View.VISIBLE);

            } else {
                holder.textViewInvite.setVisibility(View.GONE);

            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // done.setVisibility(View.VISIBLE);
                    if (multipleSelectedItemModel.getSelected().equalsIgnoreCase("true")) {
                        multipleSelectedItemModels.get(position).setSelected("false");
                        holder.textViewInvite.setVisibility(View.GONE);
                        if (list.size() > 0)
                            removeFromSelected(multipleSelectedItemModel);
                        //done.setText("Invite ("+selected.size()+")");

                    } else {
                        multipleSelectedItemModels.get(position).setSelected("true");
                        list.add(multipleSelectedItemModels.get(position));
                        // done.setText("Invite ("+selected.size()+")");

                        holder.textViewInvite.setVisibility(View.VISIBLE);
                        String s = getSelectedNameCsv();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchET.getWindowToken(), 0);
                        if (s.equalsIgnoreCase("")) {
                            Toast.makeText(AllClinicSearch.this, "Select Clinic.", Toast.LENGTH_SHORT).show();
                        } else {

                            Intent intent = new Intent(AllClinicSearch.this, AddOrEditClinicDetail.class);
                            intent.putExtra("item", (Serializable) list.get(0));
                            startActivity(intent);
                            finish();
                        }

                    }
                }

            });
            holder.textViewName.setText(multipleSelectedItemModel.getClinic_name());
        }

        @Override
        public int getItemCount() {
            return multipleSelectedItemModels.size();
        }

        public class MyHolderView extends RecyclerView.ViewHolder {
            TextView textViewName, textViewDepartment;
            ImageView imageViewPic, textViewInvite;
            RelativeLayout relativeLayoutview;

            public MyHolderView(View itemView) {
                super(itemView);
                relativeLayoutview = (RelativeLayout) itemView.findViewById(R.id.view);
                textViewName = (TextView) itemView.findViewById(R.id.name);
                textViewInvite = (ImageView) itemView.findViewById(R.id.invite);
            }
        }

        private void delete(int position) {
            multipleSelectedItemModels.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, multipleSelectedItemModels.size());
            if (multipleSelectedItemModels.size() == 0) {
                // collapsingToolbarLayout.setVisibility(View.GONE);
            }
        }

    }

    public void removeFromSelected(AllClinicModel inviteModel) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(inviteModel.getId())) {
                list.remove(i);
                break;
            }
        }
    }

/*
    public class UserSevicesAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String fname, lname, email, password, mobile, otp, title, category, city, gender, yearOfExperince, dob, website, about;
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

*/
/*
        public UserSevicesAsyncTask(String fname, String email, String mobile, String title, String category, String city, String gender, String yearOfExperince, String dob, String website, String about) {
            this.email = email;
            this.title = title;
            this.yearOfExperince = yearOfExperince;
            this.about = about;
            this.gender = gender;
            this.dob = dob;
            this.website = website;
            this.fname = fname;
            this.city = city;
            this.lname = lname;
            this.mobile = mobile;
            this.category = category;
        }
*//*


        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/services";
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
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                pairs.add(new BasicNameValuePair("type", "1"));
                pairs.add(new BasicNameValuePair("UserId", sessionManager.getUserDetails().get(SesstionManager.USER_ID)));

                httppost.setHeader("Authorization", "Basic " + sessionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN));
                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                response = client.execute(httppost);
                responseBody = EntityUtils.toString(response.getEntity());
                jo = new JSONObject(responseBody);

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
                                        Toast.makeText(Multi_Select_Search_specialization.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (dataJsonObject.has("specialities") && !dataJsonObject.isNull("specialities")) {
                                    ArrayList<SpecializationModel> specializationModels = new ArrayList<>();
                                    JSONArray jsonArray = dataJsonObject.getJSONArray("specialities");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        String id = "", name = "";
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        if (jsonObject.has("id") && !jsonObject.isNull("id")) {
                                            id = jsonObject.getString("id");
                                        }
                                        if (jsonObject.has("name") && !jsonObject.isNull("name")) {
                                            name = jsonObject.getString("name");
                                        }
                                        specializationModels.add(new SpecializationModel(name, id));
                                    }
                                }

                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
*/

}

