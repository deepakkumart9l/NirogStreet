package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.app.nirogstreet.model.SpecializationModel;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ImageLoader;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

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
 * Created by Preeti on 26-12-2017.
 */

public class SelectSingleCity extends Activity

{
    private RecyclerView recyclerViewsearchData;
    ImageView imageViewSearch;
    ImageLoader imageLoader;
    private static final int RESULT_CODE = 1;
    TextView addQualificationTextView;
    ImageView backImageView;
    private ArrayList<SpecializationModel> list;
    SearchAdapterMultiSelect searchAdapterMultiSelect;
    EditText searchET;
    SesstionManager sessionManager;
    ArrayList<SpecializationModel> searchModels = new ArrayList<>();

    CircularProgressBar circularProgressBar;
    // CollapsingToolbarLayout collapsingToolbarLayout;
    TextView textViewDone, specilization;
    private String text = "";
    private SearchAsync searchAsync;
    private String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_select_search);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
        addQualificationTextView = (TextView) findViewById(R.id.addQualification);
        specilization = (TextView) findViewById(R.id.specilization);
        specilization.setText("Select your City");
        sessionManager = new SesstionManager(SelectSingleCity.this);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        //  collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Intent intent = getIntent();
        if (intent.hasExtra("list")) {
            list = (ArrayList<SpecializationModel>) intent.getSerializableExtra("list");
            // collapsingToolbarLayout.setVisibility(View.VISIBLE);

        } else {
            list = new ArrayList<>();

        }
        imageLoader = new ImageLoader(SelectSingleCity.this);
        searchET = (EditText) findViewById(R.id.searchET);
        searchET.setHint("Search City");
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
                    Toast.makeText(SelectSingleCity.this, "Select friends.", Toast.LENGTH_SHORT).show();
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
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SelectSingleCity.this, LinearLayoutManager.HORIZONTAL, false);


        searchET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                addQualificationTextView.setVisibility(View.GONE);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


        searchET.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
filter(s.toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    private Timer timer = new Timer();
                    private final long delay = 2000; // time delay in milliseconds

                    @Override
                    public void afterTextChanged(final Editable s) {

                        }
                }
        );

        if (NetworkUtill.isNetworkAvailable(SelectSingleCity.this)) {
            if (searchET.getText().toString().length() == 0) {
                searchAsync = new SearchAsync("");
                searchAsync.execute();
            } else {
                searchAsync = new SearchAsync(searchET.getText().toString());
                searchAsync.execute();
            }
        } else {
            NetworkUtill.showNoInternetDialog(SelectSingleCity.this);
        }
        addQualificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = searchET.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchET.getWindowToken(), 0);
                if (s.length() == 0 && s.equalsIgnoreCase(" ")) {
                    Toast.makeText(SelectSingleCity.this, "Select City", Toast.LENGTH_SHORT).show();
                } else {

                    list = new ArrayList<SpecializationModel>();
                    list.add(new SpecializationModel(s, "", false));
                    Intent return_intent = new Intent();
                    return_intent.putExtra("friendsCsv", s);
                    return_intent.putExtra("list", (Serializable) list);
                    setResult(RESULT_CODE, return_intent);
                    finish();
                }
            }
        });

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
            recyclerViewsearchData.setVisibility(View.GONE);
            //  bar = (ProgressBar) findViewById(R.id.progressBar);
            //   bar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

             String   url = AppUrl.AppBaseUrl + "user/council-detail";

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

                pairs.add(new BasicNameValuePair("data_for", "2"));
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
            searchModels = new ArrayList<>();
            try {
                if (jo != null) {
                    recyclerViewsearchData.setVisibility(View.VISIBLE);
                    String nextUri = null, authToken = null, userName = null, profileUrl = null, sepcializationName = null;
                    JSONObject jsonObject;
                    if (jo.has("data") && !jo.isNull("data"))

                    {
                        jsonObject = jo.getJSONObject("data");
                        if (jsonObject.has("city") && !jsonObject.isNull("city")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("city");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String fname = "", lname = "", slug = "", profile_pic = "", department = "", id = "";
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if (jsonObject1.has("city") && !jsonObject1.isNull("city")) {
                                    fname = jsonObject1.getString("city");
                                }
                                if (jsonObject1.has("id") && !jsonObject1.isNull("id")) {
                                    id = jsonObject1.getString("id");
                                }
                                if (jsonObject1.has("lname") && !jsonObject1.isNull("lname")) {
                                    lname = jsonObject1.getString("lname");
                                }
                                if (jsonObject1.has("slug") && !jsonObject1.isNull("slug")) {
                                    slug = jsonObject1.getString("slug");
                                }
                                if (jsonObject1.has("profile_pic") && !jsonObject1.isNull("profile_pic")) {
                                    profile_pic = jsonObject1.getString("profile_pic");
                                }
                                if (jsonObject1.has("department") && !jsonObject1.isNull("department")) {
                                    department = jsonObject1.getString("department");
                                }
                                String username = null;
                                if (!fname.equals("")) {
                                    if (!lname.equals("")) {
                                        username = fname + " " + lname;
                                    } else
                                        username = fname;
                                }
                                searchModels.add(new SpecializationModel(fname, id, false));
                            }
                        } else {
                            addQualificationTextView.setText("+Add "+"("+searchET.getText().toString()+")");
                            addQualificationTextView.setVisibility(View.VISIBLE);
                        }


                        if (searchModels != null && searchModels.size() > 0) {
                            if (list != null && list.size() > 0) {
                                for (int k = 0; k < list.size(); k++) {

                                    for (int j = 0; j < searchModels.size(); j++) {
                                        if (list.get(k).getId().equalsIgnoreCase(searchModels.get(j).getId())) {
                                            searchModels.get(j).setSelected(true);
                                        }
                                    }
                                }
                            }
                            //    mRecyclerView.setAdapter(new SearchAdapter(Multi_Select_Search.this, searchModels));
                            searchAdapterMultiSelect = new SearchAdapterMultiSelect(SelectSingleCity.this, searchModels);
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
    void filter(String text) {
        try {
            ArrayList<SpecializationModel> temp = new ArrayList();
            for (SpecializationModel d : searchModels) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if (d.getSpecializationName().toLowerCase(Locale.getDefault()).contains(text.toLowerCase())) {
                    temp.add(d);
                }
            }
            //update recyclerview
            searchAdapterMultiSelect.updateList(temp);
            if (temp.size() == 0) {
                addQualificationTextView.setText("+Add " + "(" + searchET.getText().toString() + ")");
                addQualificationTextView.setVisibility(View.VISIBLE);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
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
                String language = list.get(i).getSpecializationName();
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
        ArrayList<SpecializationModel> multipleSelectedItemModels;
        public void updateList(ArrayList<SpecializationModel> list) {
            multipleSelectedItemModels = list;
            notifyDataSetChanged();
        }
        public SearchAdapterMultiSelect(Context context, ArrayList<SpecializationModel> multipleSelectedItemModels) {
            this.multipleSelectedItemModels = multipleSelectedItemModels;
            this.context = context;
        }

        @Override
        public SearchAdapterMultiSelect.MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_selected, parent, false);
            return new SearchAdapterMultiSelect.MyHolderView(v);
        }

        @Override
        public void onBindViewHolder(final SearchAdapterMultiSelect.MyHolderView holder, final int position) {
            final SpecializationModel multipleSelectedItemModel = multipleSelectedItemModels.get(position);
            if (multipleSelectedItemModel.isSelected()) {
                holder.textViewInvite.setVisibility(View.VISIBLE);

            } else {
                holder.textViewInvite.setVisibility(View.GONE);

            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // done.setVisibility(View.VISIBLE);
                    if (multipleSelectedItemModel.isSelected()) {
                        multipleSelectedItemModels.get(position).setSelected(false);
                        holder.textViewInvite.setVisibility(View.GONE);
                        if (list.size() > 0)
                            removeFromSelected(multipleSelectedItemModel);
                        //done.setText("Invite ("+selected.size()+")");

                    } else {
                        multipleSelectedItemModels.get(position).setSelected(true);
                        list.add(multipleSelectedItemModels.get(position));
                        // done.setText("Invite ("+selected.size()+")");

                        holder.textViewInvite.setVisibility(View.VISIBLE);
                        String s = getSelectedNameCsv();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchET.getWindowToken(), 0);
                        if (s.equalsIgnoreCase("")) {
                            Toast.makeText(SelectSingleCity.this, "Select friends.", Toast.LENGTH_SHORT).show();
                        } else {

                            Intent return_intent = new Intent();
                            return_intent.putExtra("friendsCsv", s);
                            return_intent.putExtra("list", (Serializable) list);
                            setResult(RESULT_CODE, return_intent);
                            finish();
                        }

                    }

                }
            });
            holder.textViewName.setText(multipleSelectedItemModel.getSpecializationName());
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

    public void removeFromSelected(SpecializationModel inviteModel) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(inviteModel.getId())) {
                list.remove(i);
                break;
            }
        }
    }

}
