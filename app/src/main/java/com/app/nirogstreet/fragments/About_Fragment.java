package com.app.nirogstreet.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.CommentsActivity;
import com.app.nirogstreet.activites.CommunitiesDetails;
import com.app.nirogstreet.activites.CreateCommunity;
import com.app.nirogstreet.activites.MainActivity;
import com.app.nirogstreet.activites.Multiple_select_invite_search;
import com.app.nirogstreet.activites.UpdateCommunity;
import com.app.nirogstreet.adapter.CommentsRecyclerAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.LikesModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.model.UserList;
import com.app.nirogstreet.parser.CommentsParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Preeti on 02-11-2017.
 */

public class About_Fragment extends Fragment {
    View view;
    TextView infoTextView;
    boolean isMemberOfGroup = false;
    String privacyCheck;
    AcceptDeclineJoinAsyncTask acceptDeclineJoinAsyncTask;
    ArrayList<LikesModel> membersModel = new ArrayList<>();
    Context context;
    boolean createdBy = false;
    CircleImageView circleImageView;
    CircularProgressBar circularProgressBar;
    String groupId, authToken;
    TextView andminTextView;
    GetCommunityDetailAsyncTask getCommunityDetailAsyncTask;
    SesstionManager sesstionManager;
    String str = "is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Bundle bundle = getArguments();
        groupId = bundle.getString("groupId");
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.statusbarcolor));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ApplicationSingleton.isGroupUpdated()) {
            if (NetworkUtill.isNetworkAvailable(context)) {
                getCommunityDetailAsyncTask = new GetCommunityDetailAsyncTask(groupId);
                getCommunityDetailAsyncTask.execute();
            } else {
                NetworkUtill.showNoInternetDialog(context);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getCommunityDetailAsyncTask != null && !getCommunityDetailAsyncTask.isCancelled())
            getCommunityDetailAsyncTask.cancelAsyncTask();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.about_communities, container, false);
        infoTextView = (TextView) view.findViewById(R.id.info);
        sesstionManager = new SesstionManager(context);
        circleImageView = (CircleImageView) view.findViewById(R.id.AdminImage);
        authToken = sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN);
        circularProgressBar = (CircularProgressBar) view.findViewById(R.id.circularProgressBar);
        TypeFaceMethods.setRegularTypeFaceForTextView(infoTextView, context);
        andminTextView = (TextView) view.findViewById(R.id.adminname);
        TypeFaceMethods.setRegularTypeFaceForTextView(andminTextView, context);


        if (NetworkUtill.isNetworkAvailable(context)) {
            getCommunityDetailAsyncTask = new GetCommunityDetailAsyncTask(groupId);
            getCommunityDetailAsyncTask.execute();
        } else {
            NetworkUtill.showNoInternetDialog(context);
        }
        return view;
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                try {


                    ViewTreeObserver obs = tv.getViewTreeObserver();
                    obs.removeGlobalOnLayoutListener(this);
                    if (maxLine == 0) {
                        int lineEndIndex = tv.getLayout().getLineEnd(0);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE);
                    } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                        int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE);
                    } else {
                        int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "view less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "view more", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    public class GetCommunityDetailAsyncTask extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        String groupId;


        private String responseBody;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public GetCommunityDetailAsyncTask(String groupId) {
            this.groupId = groupId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            circularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObject = jo.getJSONObject("response");
                        if (jsonObject.has("groupDetail") && !jsonObject.isNull("groupDetail")) {
                            String name = null, invite_note = null, description = null, banner = null, privacy = null, created_profile = null, createdBy_id = null, createdBy_name = null;
                            JSONObject groupDetailJsonObject = jsonObject.getJSONObject("groupDetail");
                            if (groupDetailJsonObject.has("name") && !groupDetailJsonObject.isNull("name")) {
                                name = groupDetailJsonObject.getString("name");
                                CommunitiesDetails.setNameAndCoverPic(name, "");

                            }
                            if (groupDetailJsonObject.has("invite_note") && !groupDetailJsonObject.isNull("invite_note")) {
                                invite_note = groupDetailJsonObject.getString("invite_note");
                            }
                            if (groupDetailJsonObject.has("description") && !groupDetailJsonObject.isNull("description")) {
                                description = groupDetailJsonObject.getString("description");
                                infoTextView.setText(description);

                                if (description.length() > 170)
                                    makeTextViewResizable(infoTextView, 3, "view more", true);
                                else {
                                    infoTextView.setText(description);
                                }
                            }
                            if (groupDetailJsonObject.has("banner") && !groupDetailJsonObject.isNull("banner")) {
                                banner = groupDetailJsonObject.getString("banner");
                                Glide.with(context)
                                        .load(banner) // Uri of the picture
                                        .centerCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .crossFade()
                                        .override(100, 100)
                                        .into(CommunitiesDetails.circleImageView);

                            }

                            if (groupDetailJsonObject.has("privacy") && !groupDetailJsonObject.isNull("privacy")) {
                                privacy = groupDetailJsonObject.getString("privacy");
                                privacyCheck = privacy;
                            }
                            if (name != null && banner != null && !banner.contains("tempimages")) {
                                CommunitiesDetails.setNameAndCoverPic(name, banner);
                            }
                            if (groupDetailJsonObject.has("created_by") && !groupDetailJsonObject.isNull("created_by")) {
                                JSONObject created_ByObject = groupDetailJsonObject.getJSONObject("created_by");
                                if (created_ByObject.has("id") && !created_ByObject.isNull("id")) {
                                    createdBy_id = created_ByObject.getString("id");
                                }
                                if (created_ByObject.has("name") && !created_ByObject.isNull("name")) {
                                    createdBy_name = created_ByObject.getString("name");
                                    andminTextView.setText(createdBy_name);
                                }
                                if (created_ByObject.has("profile_pic") && !created_ByObject.isNull("profile_pic")) {
                                    created_profile = created_ByObject.getString("profile_pic");
                                    Glide.with(context)
                                            .load(created_profile) // Uri of the picture
                                            .centerCrop()
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .crossFade()
                                            .override(100, 100)
                                            .into(circleImageView);
                                }

                            }
                            ArrayList<UserList> userDetailModels = new ArrayList<>();
                            if (groupDetailJsonObject.has("members") && !groupDetailJsonObject.isNull("members")) {
                                JSONArray jsonArray = groupDetailJsonObject.getJSONArray("members");
                                if (jsonArray != null && jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        String userId = null, userName = null, profile_pic = null;
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        if (object.has("user_id") && !object.isNull("user_id")) {
                                            JSONObject userDetail = object.getJSONObject("user_id");
                                            if (userDetail.has("id") && !userDetail.isNull("id")) {
                                                userId = userDetail.getString("id");

                                            }
                                            if (userDetail.has("name") && !userDetail.isNull("name")) {
                                                userName = userDetail.getString("name");

                                            }
                                            if (userDetail.has("profile_pic") && !userDetail.isNull("profile_pic")) {
                                                profile_pic = userDetail.getString("profile_pic");
                                            }
                                            userDetailModels.add(new UserList(userId, userName, profile_pic));
                                        }
                                    }
                                    LinearLayout llGallery = (LinearLayout) view.findViewById(R.id.llGallery);

                                    if (userDetailModels.size() > 0)
                                        for (int i = 0; i < userDetailModels.size() + 1; i++) {
                                            View view = ((CommunitiesDetails) context).getLayoutInflater().inflate(R.layout.member_item_communities, null, false);
                                            if (i == 4)
                                                break;
                                            TextView nameTv = (TextView) view.findViewById(R.id.adminname);
                                            TypeFaceMethods.setRegularTypeFaceForTextView(nameTv, context);
                                            if (i == 3) {
                                                nameTv.setText("View More");
                                            } else {

                                                nameTv.setText(userDetailModels.get(i).getName());
                                                TypeFaceMethods.setRegularTypeFaceForTextView(nameTv, context);
                                                CircleImageView imageView = (CircleImageView) view.findViewById(R.id.cir);
                                                Glide.with(context)
                                                        .load(userDetailModels.get(i).getProfile_pic()) // Uri of the picture
                                                        .centerCrop()
                                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                        .crossFade()
                                                        .override(100, 100)
                                                        .into(imageView);
                                            }
                                            llGallery.addView(view);
                                        }

                                }

                            }
                            if (userDetailModels != null && userDetailModels.size() > 0) {
                                for (int i = 0; i < userDetailModels.size(); i++) {
                                    if (userDetailModels.get(i).getId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID))) {
                                        isMemberOfGroup = true;
                                        break;

                                    }
                                }
                            }
                            if (createdBy_id.equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID))) {
                                createdBy = true;
                                CommunitiesDetails.moreImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setMoreMenu(1);

                                    }
                                });
                            } else if (isMemberOfGroup) {
                                CommunitiesDetails.moreImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setMoreMenu(2);

                                    }
                                });
                            } else {
                                CommunitiesDetails.moreImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setMoreMenu(3);

                                    }
                                });
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {


                String url = AppUrl.BaseUrl + "group/detail";
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
                pairs.add(new BasicNameValuePair("groupID", groupId));
                httppost.setHeader("Authorization", "Basic " + authToken);
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
        protected void onPreExecute() {
            super.onPreExecute();
            circularProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public static About_Fragment getInstance(String groupId) {
        About_Fragment artistDetailFragment = new About_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("groupId", groupId);

        artistDetailFragment.setArguments(bundle);
        return artistDetailFragment;
    }

    private void setMoreMenu(int i) {
        PopupMenu popup = new PopupMenu(context, view);

        switch (i) {
            case 1:
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                Intent intent = new Intent(context, UpdateCommunity.class);
                                intent.putExtra("update", true);
                                intent.putExtra("groupId", groupId);
                                context.startActivity(intent);
                                break;
                            case R.id.invite:
                                Intent intent1 = new Intent(context, Multiple_select_invite_search.class);
                                intent1.putExtra("groupId", groupId);
                                context.startActivity(intent1);
                                break;
                            case R.id.leave:
                                if (NetworkUtill.isNetworkAvailable(context)) {
                                    acceptDeclineJoinAsyncTask = new AcceptDeclineJoinAsyncTask(groupId, sesstionManager.getUserDetails().get(SesstionManager.USER_ID), authToken, 2, 0);
                                    acceptDeclineJoinAsyncTask.execute();
                                } else {
                                    NetworkUtill.showNoInternetDialog(context);
                                }
                                break;

                        }
                        return false;
                    }
                });
                popup.show();

                break;
            case 2:
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.getMenu().findItem(R.id.edit).setVisible(false);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.invite:
                                Intent intent1 = new Intent(context, Multiple_select_invite_search.class);
                                intent1.putExtra("groupId", groupId);
                                context.startActivity(intent1);
                                break;
                            case R.id.leave:
                                if (NetworkUtill.isNetworkAvailable(context)) {
                                    acceptDeclineJoinAsyncTask = new AcceptDeclineJoinAsyncTask(groupId, sesstionManager.getUserDetails().get(SesstionManager.USER_ID), authToken, 2, 0);
                                    acceptDeclineJoinAsyncTask.execute();
                                } else {
                                    NetworkUtill.showNoInternetDialog(context);
                                }
                                break;

                        }
                        return false;
                    }
                });
                popup.show();
                break;
            case 3:
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.getMenu().findItem(R.id.edit).setVisible(false);
                popup.getMenu().findItem(R.id.invite).setVisible(false);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.leave:
                                if (NetworkUtill.isNetworkAvailable(context)) {
                                    acceptDeclineJoinAsyncTask = new AcceptDeclineJoinAsyncTask(groupId, sesstionManager.getUserDetails().get(SesstionManager.USER_ID), authToken, 1, 0);
                                    acceptDeclineJoinAsyncTask.execute();
                                } else {
                                    NetworkUtill.showNoInternetDialog(context);
                                }
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
                break;
        }
    }

    public class AcceptDeclineJoinAsyncTask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String groupId, userId;

        int status1;
        HttpClient client;
        int pos;
        private String responseBody;

        public AcceptDeclineJoinAsyncTask(String groupId, String userId, String authToken, int status, int pos) {
            this.groupId = groupId;
            this.status1 = status;
            this.authToken = authToken;
            this.pos = pos;
            this.userId = userId;
        }

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                if (jo != null) {


                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObjectresponse = jo.getJSONObject("response");
                        if (jsonObjectresponse.has("message") && !jsonObjectresponse.isNull("message")) {
                            JSONObject jsonObject = jsonObjectresponse.getJSONObject("message");
                            if (jsonObject.has("message") && !jsonObject.isNull("message")) {
                                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                if (privacyCheck.equalsIgnoreCase("0") && status1 == 1) {
                                    setMoreMenu(2);
                                }
                                if (privacyCheck.equalsIgnoreCase("1") && status1 == 2) {
                                    getActivity().finish();
                                }
                                if (privacyCheck.equalsIgnoreCase("0") && status1 == 2) {
                                    setMoreMenu(3);
                                }


                            }


                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {


                String url = AppUrl.BaseUrl + "group/accept-decline";
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
                pairs.add(new BasicNameValuePair("groupID", groupId));
                pairs.add(new BasicNameValuePair("status", status1 + ""));
                httppost.setHeader("Authorization", "Basic " + authToken);

                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                response = client.execute(httppost);

                responseBody = EntityUtils
                        .toString(response.getEntity());
                jo = new JSONObject(responseBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
