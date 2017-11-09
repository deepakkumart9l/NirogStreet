package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.MultipleSelectedItemModel;
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
import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipDeletedListener;

/**
 * Created by Preeti on 08-11-2017.
 */
public class Multi_Select_Search extends Activity

{
    private RecyclerView recyclerViewsearchData;
    ImageView imageViewSearch;
    private ArrayList<MultipleSelectedItemModel> totalFeeds;

    ImageLoader imageLoader;
    private static final int RESULT_CODE = 1;
    private static final int RESULT_CODE_TAGS = 8;
    int page = 1;

    ImageView backImageView;
    private ArrayList<MultipleSelectedItemModel> list;
    SearchAdapterMultiSelect searchAdapterMultiSelect;
    EditText searchET;
    SesstionManager sessionManager;
    ArrayList<MultipleSelectedItemModel> searchModels = new ArrayList<>();
    boolean isTags = false;
    CircularProgressBar circularProgressBar;
    TextView textViewDone, specilization;
    private String text = "";
    LinearLayoutManager linearLayoutManager;
    private SearchAsync searchAsync;
    private String query = "";
    ChipCloud horizontalChipCloud;
    LinearLayout horizontalScroll;
    private TextView addQualificationTextView;
    boolean isService = false;
    private int totalPageCount;
    private boolean isLoading = false;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_select_search);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbarcolor));
        }
        specilization = (TextView) findViewById(R.id.specilization);
        searchET = (EditText) findViewById(R.id.searchET);

        specilization.setText("Select People");
        searchET.setHint("Search People");

        addQualificationTextView = (TextView) findViewById(R.id.addQualification);

        TypeFaceMethods.setRegularTypeFaceForTextView(addQualificationTextView, Multi_Select_Search.this);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/ubuntu.regular.ttf");
        ChipCloudConfig deleteableConfig = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.multi)
                .checkedChipColor(Color.parseColor("#ddaa00"))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#e0e0e0"))
                .showClose(Color.parseColor("#a6a6a6"))
                .useInsetPadding(true)
                .typeface(tf)
                .uncheckedTextColor(Color.parseColor("#000000"));

        horizontalScroll = (LinearLayout) findViewById(R.id.horizontal_layout);
        horizontalChipCloud = new ChipCloud(this, horizontalScroll, deleteableConfig);
        horizontalChipCloud.setDeleteListener(new ChipDeletedListener() {
            @Override
            public void chipDeleted(int index, String label) {
                removeByChip(label);
            }
        });

        sessionManager = new SesstionManager(Multi_Select_Search.this);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        Intent intent = getIntent();
        if (intent.hasExtra("list")) {
            list = (ArrayList<MultipleSelectedItemModel>) intent.getSerializableExtra("list");
            for (int i = 0; i < list.size(); i++) {
                horizontalChipCloud.addChip(list.get(i).getUserName());

            }

        } else {
            list = new ArrayList<>();

        }
        totalFeeds = new ArrayList<>();

        imageLoader = new ImageLoader(Multi_Select_Search.this);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        recyclerViewsearchData = (RecyclerView) findViewById(R.id.recyclerview);
        textViewDone = (TextView) findViewById(R.id.done);
        TypeFaceMethods.setRegularTypeFaceEditText(searchET, Multi_Select_Search.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(specilization, Multi_Select_Search.this);

        TypeFaceMethods.setRegularTypeFaceForTextView(textViewDone, Multi_Select_Search.this);
        textViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = getSelectedNameCsv();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchET.getWindowToken(), 0);


                Intent return_intent = new Intent();
                return_intent.putExtra("friendsCsv", s);
                return_intent.putExtra("list", (Serializable) list);
                setResult(RESULT_CODE, return_intent);

                finish();

            }
        });


        linearLayoutManager = new LinearLayoutManager(Multi_Select_Search.this);
        recyclerViewsearchData.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewsearchData.setLayoutManager(linearLayoutManager);


        searchET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                addQualificationTextView.setVisibility(View.GONE);

                text = s.toString();
                query = text;
                if (searchModels != null) {
                    recyclerViewsearchData.setAdapter(null);
                }
                if (searchAsync != null)
                    searchAsync.cancel(true);
                totalFeeds = new ArrayList<>();
                totalFeeds = new ArrayList<>();

                // setVisibilty(0);
                if (NetworkUtill.isNetworkAvailable(Multi_Select_Search.this)) {
                    if (searchET.getText().toString().length() == 0) {
searchAdapterMultiSelect=null;
                        searchAsync = new SearchAsync("");
                        searchAsync.execute();
                    } else {
                        searchAdapterMultiSelect=null;

                        searchAsync = new SearchAsync(searchET.getText().toString());
                        searchAsync.execute();
                    }
                } else {
                    NetworkUtill.showNoInternetDialog(Multi_Select_Search.this);
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


        searchET.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

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
                                        (Multi_Select_Search.this).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                totalFeeds = new ArrayList<>();

                                                if (searchET.getText().toString().length() == 0) {
                                                    if (NetworkUtill.isNetworkAvailable(Multi_Select_Search.this)) {
                                                        searchAdapterMultiSelect=null;

                                                        searchAsync = new SearchAsync("");

                                                        searchAsync.execute();
                                                    } else {
                                                        NetworkUtill.showNoInternetDialog(Multi_Select_Search.this);
                                                    }
                                                } else {
                                                    if (NetworkUtill.isNetworkAvailable(Multi_Select_Search.this)) {
                                                        searchAdapterMultiSelect=null;

                                                        searchAsync = new SearchAsync(searchET.getText().toString());
                                                        searchAsync.execute();
                                                    } else {
                                                        NetworkUtill.showNoInternetDialog(Multi_Select_Search.this);
                                                    }

                                                }
                                            }
                                        });
                                    }
                                }, delay
                        );
                    }
                }
        );

        if (NetworkUtill.isNetworkAvailable(Multi_Select_Search.this)) {
            if (searchET.getText().toString().length() == 0) {
                searchAsync = new SearchAsync("");
                searchAsync.execute();
            } else {
                searchAsync = new SearchAsync(searchET.getText().toString());
                searchAsync.execute();
            }
        } else {
            NetworkUtill.showNoInternetDialog(Multi_Select_Search.this);
        }


    }


    public class SearchAsync extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String email, password;
        CircularProgressBar bar;
        //PlayServiceHelper regId;
        String strTobeSearch = "";
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
                pairs.add(new BasicNameValuePair("searchkey", strTobeSearch));
                pairs.add(new BasicNameValuePair("userID", sessionManager.getUserDetails().get(SesstionManager.USER_ID)));
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        pairs.add(new BasicNameValuePair("selectedUsers", list.get(i).getUserId()));
                    }
                }
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
                    if (jo.has("response") && !jo.isNull("response"))

                    {
                        jsonObject = jo.getJSONObject("response");
                        if (jo.has("totalpage") && !jo.isNull("totalpage")) {
                            totalPageCount = jo.getInt("totalpage");
                        }
                        if (jsonObject.has("users") && !jsonObject.isNull("users")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("users");
                            for (int i = 0; i < jsonArray.length(); i++) {
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
                                searchModels.add(new MultipleSelectedItemModel(id, fname, "", profile_pic));
                            }
                        }

                        if (searchModels != null && searchModels.size() > 0) {
                            if (list != null && list.size() > 0) {
                                for (int k = 0; k < list.size(); k++) {

                                    for (int j = 0; j < searchModels.size(); j++) {
                                        if (list.get(k).getUserId().equalsIgnoreCase(searchModels.get(j).getUserId())) {
                                            searchModels.get(j).setSelected(true);
                                        }
                                    }
                                }
                            }
                            totalFeeds.addAll(searchModels);

                            isLoading = false;
                            if (searchAdapterMultiSelect == null && totalFeeds.size() > 0) {

                                searchAdapterMultiSelect = new SearchAdapterMultiSelect(Multi_Select_Search.this, totalFeeds);
                                recyclerViewsearchData.setAdapter(searchAdapterMultiSelect);
                                recyclerViewsearchData.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                                                    searchAsync = new SearchAsync(strTobeSearch);
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
                                searchAdapterMultiSelect.notifyDataSetChanged();

                            }
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public String getSelectedIdCsv() {
        String languageCSV = "";
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String language = list.get(i).getUserId();
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
            strings.add(list.get(i).getUserId());
        }
        return strings;
    }

    public String getSelectedNameCsv() {
        String languageCSV = "";
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String language = list.get(i).getUserName();
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
        ArrayList<MultipleSelectedItemModel> multipleSelectedItemModels;

        public SearchAdapterMultiSelect(Context context, ArrayList<MultipleSelectedItemModel> multipleSelectedItemModels) {
            this.multipleSelectedItemModels = multipleSelectedItemModels;
            this.context = context;
        }

        @Override
        public SearchAdapterMultiSelect.MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_selected, parent, false);
            return new MyHolderView(v);
        }

        @Override
        public void onBindViewHolder(final SearchAdapterMultiSelect.MyHolderView holder, final int position) {
            final MultipleSelectedItemModel multipleSelectedItemModel = multipleSelectedItemModels.get(position);
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
                        horizontalChipCloud.delchip(position, multipleSelectedItemModel.getUserName());

                    } else {
                        multipleSelectedItemModels.get(position).setSelected(true);
                        list.add(multipleSelectedItemModels.get(position));
                        horizontalChipCloud.addChip(multipleSelectedItemModel.getUserName());
                        holder.textViewInvite.setVisibility(View.VISIBLE);

                    }

                }
            });
            TypeFaceMethods.setRegularTypeFaceForTextView(holder.textViewName, context);
            holder.textViewName.setText(multipleSelectedItemModel.getUserName());
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
            }
        }

    }

    public void removeByChip(String label) {
        for (int i = 0; i < searchModels.size(); i++) {
            if (searchModels.get(i).getUserName().equals(label)) {
                searchModels.get(i).setSelected(false);
                searchAdapterMultiSelect.notifyItemChanged(i);

                break;

            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserName().equals(label)) {
                list.remove(i);
                break;
            }
        }

    }

    public void removeFromSelected(MultipleSelectedItemModel inviteModel) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserId().equals(inviteModel.getUserId())) {
                list.remove(i);
                break;
            }
        }
    }

}


