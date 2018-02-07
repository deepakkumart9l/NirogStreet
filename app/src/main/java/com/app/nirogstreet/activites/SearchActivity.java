package com.app.nirogstreet.activites;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.SearchAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.SearchModel;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
 * Created by Preeti on 28-10-2017.
 */
public class SearchActivity extends AppCompatActivity {
    SearchAsync searchAsync;
    ArrayList<SearchModel> searchModels;
    int totalPageCount;
    int page = 1;
    SearchAdapter searchAdapter;
    private boolean isLoading = false;


    EditText searchET;
    private LinearLayoutManager linearLayoutManager;

    private RecyclerView mRecyclerView;
    ImageView imageViewSearch;
    ImageView backImageView;
    CircularProgressBar circularProgressBar;
    private LinearLayoutManager mLinearLayoutManager;
    private String text = "";
    private String query = "";
    SesstionManager sessionManager;
    private String authToken, userId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
        sessionManager = new SesstionManager(SearchActivity.this);

        mRecyclerView = (RecyclerView) findViewById(R.id.lv);
        mRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        authToken = userDetails.get(SesstionManager.AUTH_TOKEN);
        userId = userDetails.get(SesstionManager.USER_ID);
        searchET = (EditText) findViewById(R.id.searchET);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.scroll);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageViewSearch = (ImageView) findViewById(R.id.searchButton);
        searchModels = new ArrayList<>();
        imageViewSearch.setVisibility(View.GONE);
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (searchET.getText().toString().length() != 0) {
                    if (NetworkUtill.isNetworkAvailable(SearchActivity.this)) {
                        searchAsync = new SearchAsync(searchET.getText().toString());
                        searchAsync.execute();
                    } else {
                        NetworkUtill.showNoInternetDialog(SearchActivity.this);
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "enter name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                searchModels = new ArrayList<>();
                page = 1;
                searchAdapter = null;
                text = s.toString();
                query = text;
                if (searchModels != null) {
                    mRecyclerView.setAdapter(null);
                }
                if (searchAsync != null)
                    searchAsync.cancel(true);
                // setVisibilty(0);
                if (NetworkUtill.isNetworkAvailable(SearchActivity.this)) {
                    if (searchET.getText().toString().length() == 0) {
                        searchAsync = new SearchAsync("");
                        searchAsync.execute();
                    } else {
                        searchAsync = new SearchAsync(searchET.getText().toString());
                        searchAsync.execute();
                    }
                } else {
                    NetworkUtill.showNoInternetDialog(SearchActivity.this);
                }


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


     /*   mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent resultIntent = new Intent(SearchActivity.this, ProfileActivity.class);
                SearchModel searchModel = searchModels.get(position);
                resultIntent.putExtra("userId", searchModel.getId());
                startActivity(resultIntent);
            }

        });
*/
        searchAsync = new SearchAsync("");
        searchAsync.execute();

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

                String url = AppUrl.BaseUrl + "feed/search-users";
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
                pairs.add(new BasicNameValuePair("pageNo", page + ""));

                pairs.add(new BasicNameValuePair("searchKey", strTobeSearch));
                pairs.add(new BasicNameValuePair("userID", userId));
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
            try {
                if (jo != null) {
                    String nextUri = null, authToken = null, userName = null, profileUrl = null;
                    JSONObject jsonObject;
                    if (jo.has("totalpage") && !jo.isNull("totalpage")) {
                        totalPageCount = jo.getInt("totalpage");
                    }
                    if (jo.has("response") && !jo.isNull("response")) {
                        jsonObject = jo.getJSONObject("response");
                        if (jsonObject.has("users") && !jsonObject.isNull("users")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("users");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String user_type=null,title=null;
                                String fname = "", lname = "", slug = "", profile_pic = "", department = "", id = "";
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if (jsonObject1.has("name") && !jsonObject1.isNull("name")) {
                                    fname = jsonObject1.getString("name");
                                }
                                if (jsonObject1.has("id") && !jsonObject1.isNull("id")) {
                                    id = jsonObject1.getString("id");
                                }
                                if (jsonObject1.has("lname") && !jsonObject1.isNull("lname")) {
                                    lname = jsonObject1.getString("lname");
                                }
                                if(jsonObject1.has("user_type")&&!jsonObject1.isNull("user_type"))
                                {
                                    user_type=jsonObject1.getString("user_type");
                                }
                                if(jsonObject1.has("Title")&&!jsonObject1.isNull("Title"))
                                {
                                 title=jsonObject1.getString("Title");
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
                                searchModels.add(new SearchModel(fname, slug, lname, department, profile_pic, id,user_type,title));
                            }
                            isLoading = false;
                            if (searchAdapter == null && searchModels != null && searchModels.size() > 0) {
                                searchAdapter = new SearchAdapter(SearchActivity.this, searchModels);
                                mRecyclerView.setAdapter(searchAdapter);
                                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);

                                        int totalItemCount = linearLayoutManager.getItemCount();
                                        int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                                        if (!isLoading && (totalItemCount - 1) <= (lastVisibleItem)) {
                                            try {
                                                String has_more = "";
                                                if (page < totalPageCount) {
                                                    page++;

                                                    searchAsync = new SearchAsync("");
                                                    searchAsync.execute();

                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            isLoading = true;
                                        }
                                    }
                                });

                            } else {
                                searchAdapter.notifyDataSetChanged();
                            }
                        }

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //  bar.setVisibility(View.GONE);

        }
    }

}