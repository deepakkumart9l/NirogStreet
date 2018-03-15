package com.app.nirogstreet.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.CommentsActivity;
import com.app.nirogstreet.activites.CommunitiesDetails;
import com.app.nirogstreet.activites.CreateCommunity;
import com.app.nirogstreet.activites.Dr_Profile;
import com.app.nirogstreet.activites.MainActivity;
import com.app.nirogstreet.activites.MemberListing;
import com.app.nirogstreet.activites.Multiple_select_invite_search;
import com.app.nirogstreet.activites.UpdateCommunity;
import com.app.nirogstreet.adapter.CommentsRecyclerAdapter;
import com.app.nirogstreet.adapter.MemberListingAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.LikesModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.model.UserList;
import com.app.nirogstreet.parser.CommentsParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.LetterTileProvider;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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
    ArrayList<UserList> userLists = new ArrayList<>();
    boolean isMemberOfGroup = false;
    int totalCount;
    private LetterTileProvider mLetterTileProvider;
    MemberListingAdapter memberListingAdapter;
    String description="";
    TextView privacyTextView;
    // NestedScrollView scrollView;
    String privacyCheck;
    String privacytext = "";
    boolean isLogedInUser_Admin = false;
    AcceptDeclineJoinAsyncTask acceptDeclineJoinAsyncTask;
    String statusData = "";
    final ArrayList<UserList> userDetailModels = new ArrayList<>();

    ArrayList<LikesModel> membersModel = new ArrayList<>();
    Context context;
    boolean createdBy = false;
    LinearLayoutManager llm;
    private boolean isLoading = false;

    RecyclerView mRecyclerView;
    CircularProgressBar circularProgressBar;
    String groupId, authToken, userId;
    GetCommunityDetailAsyncTask getCommunityDetailAsyncTask;
    SesstionManager sesstionManager;
    String str = "is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n";
    private int page = 2;

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
            userLists = new ArrayList<>();

            if (NetworkUtill.isNetworkAvailable(context)) {
                getCommunityDetailAsyncTask = new GetCommunityDetailAsyncTask(groupId);
                getCommunityDetailAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        mLetterTileProvider = new LetterTileProvider(context);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        //  scrollView = (NestedScrollView) view.findViewById(R.id.scrol);
        privacyTextView = (TextView) view.findViewById(R.id.privacy);
        llm = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(llm);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        authToken = sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN);
        userId = sesstionManager.getUserDetails().get(SesstionManager.USER_ID);
        circularProgressBar = (CircularProgressBar) view.findViewById(R.id.circularProgressBar);
        // TypeFaceMethods.setRegularTypeFaceForTextView(infoTextView, context);
        // andminTextView = (TextView) view.findViewById(R.id.adminname);
        //TypeFaceMethods.setRegularTypeFaceForTextView(andminTextView, context);

        mRecyclerView.setNestedScrollingEnabled(false);
        if (NetworkUtill.isNetworkAvailable(context)) {
            getCommunityDetailAsyncTask = new GetCommunityDetailAsyncTask(groupId);
            getCommunityDetailAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                isLoading = false;

                if (jo != null) {
                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObject = jo.getJSONObject("response");
                        if (jsonObject.has("groupDetail") && !jsonObject.isNull("groupDetail")) {
                            String user_type_created_by = null, title_created_by = null;
                            if(jsonObject.has("totalpage")&&!jsonObject.isNull("totalpage"))
                            {
                                totalCount=jsonObject.getInt("totalpage");
                            }
                            String name = null, invite_note = null, banner = null, privacy = null, created_profile = null, createdBy_id = null, createdBy_name = null;
                            JSONObject groupDetailJsonObject = jsonObject.getJSONObject("groupDetail");
                            if (groupDetailJsonObject.has("name") && !groupDetailJsonObject.isNull("name")) {
                                name = groupDetailJsonObject.getString("name");
                                CommunitiesDetails.setNameAndCoverPic(name, "");

                            }
                            if (groupDetailJsonObject.has("statusdata") && !groupDetailJsonObject.isNull("statusdata")) {
                                statusData = groupDetailJsonObject.getString("statusdata");
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
                                if (banner != null && !banner.equalsIgnoreCase("")) {


                                    Transformation transformation = new RoundedTransformationBuilder()
                                            .borderColor(Color.BLACK)
                                            .borderWidthDp(3)
                                            .cornerRadiusDp(30)
                                            .oval(false)
                                            .build();


                                    Picasso.with(context)
                                            .load(banner).transform(transformation)
                                            .placeholder(R.drawable.default_)
                                            .error(R.drawable.default_)
                                            .into(CommunitiesDetails.proo);
                                    CommunitiesDetails.proo.setVisibility(View.VISIBLE);
                                    CommunitiesDetails.circleImageView.setVisibility(View.GONE);


                                    // imageLoader1.getInstance().displayImage(groupModel.getGroupBanner(),  holder.groupIconImageView, defaultOptions);
                                } else {
                                    try {
                                        CommunitiesDetails.proo.setVisibility(View.GONE);
                                        CommunitiesDetails.circleImageView.setVisibility(View.VISIBLE);
                                        CommunitiesDetails.circleImageView.setImageBitmap(mLetterTileProvider.getLetterTile(name));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else {
                                try {
                                    CommunitiesDetails.proo.setVisibility(View.GONE);
                                    CommunitiesDetails.circleImageView.setVisibility(View.VISIBLE);
                                    CommunitiesDetails.circleImageView.setImageBitmap(mLetterTileProvider.getLetterTile(name));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if (groupDetailJsonObject.has("privacy") && !groupDetailJsonObject.isNull("privacy")) {
                                privacy = groupDetailJsonObject.getString("privacy");
                                privacyCheck = privacy;
                                if (privacyCheck.equalsIgnoreCase("0")) {
                                    privacyTextView.setText("PUBLIC GROUP");
                                    privacytext = "PUBLIC GROUP";
                                }
                                if (privacyCheck.equalsIgnoreCase("1")) {
                                    privacyTextView.setText("PRIVATE GROUP");
                                    privacytext = "PRIVATE GROUP";
                                }
                            }
                            if (name != null && banner != null && !banner.contains("tempimages")) {
                                CommunitiesDetails.setNameAndCoverPic(name, banner);
                            }
                            userLists.add(new UserList("","","","","",""));

                            if (groupDetailJsonObject.has("created_by") && !groupDetailJsonObject.isNull("created_by")) {
                                JSONObject created_ByObject = groupDetailJsonObject.getJSONObject("created_by");
                                if (created_ByObject.has("id") && !created_ByObject.isNull("id")) {
                                    createdBy_id = created_ByObject.getString("id");
                                    final String finalCreatedBy_id = createdBy_id;
                                    final String finalCreatedBy_id1 = createdBy_id;

                                }
                                if (created_ByObject.has("name") && !created_ByObject.isNull("name")) {
                                    createdBy_name = created_ByObject.getString("name");
                                }
                                if (created_ByObject.has("profile_pic") && !created_ByObject.isNull("profile_pic")) {
                                    created_profile = created_ByObject.getString("profile_pic");


                                }
                                if (created_ByObject.has("user_type") && !created_ByObject.isNull("user_type")) {
                                    user_type_created_by = created_ByObject.getString("user_type");
                                }
                                if (created_ByObject.has("Title") && !created_ByObject.isNull("Title")) {
                                    title_created_by = created_ByObject.getString("Title");
                                }
                                userLists.add(new UserList(createdBy_id, createdBy_name, created_profile, user_type_created_by, title_created_by, "1"));

                            }

                            if (groupDetailJsonObject.has("members") && !groupDetailJsonObject.isNull("members")) {
                                JSONArray jsonArray = groupDetailJsonObject.getJSONArray("members");
                                if (jsonArray != null && jsonArray.length() > 0) {

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        String is_admin = null;
                                        String userId = null, userName = null, profile_pic = null, user_Type = null, title = null;
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        if (object.has("user_id") && !object.isNull("user_id")) {
                                            JSONObject userDetail = object.getJSONObject("user_id");
                                            if (userDetail.has("id") && !userDetail.isNull("id")) {
                                                userId = userDetail.getString("id");

                                            }
                                            if (userDetail.has("user_type") && !userDetail.isNull("user_type")) {
                                                user_Type = userDetail.getString("user_type");
                                            }
                                            if (userDetail.has("Title") && !userDetail.isNull("Title")) {
                                                title = userDetail.getString("Title");
                                            }
                                            if (userDetail.has("name") && !userDetail.isNull("name")) {
                                                userName = userDetail.getString("name");

                                            }
                                            if (userDetail.has("profile_pic") && !userDetail.isNull("profile_pic")) {
                                                profile_pic = userDetail.getString("profile_pic");
                                            }
                                            if (object.has("is_admin") && !object.isNull("is_admin")) {
                                                is_admin = object.getString("is_admin");
                                            }
                                            if (!userId.equalsIgnoreCase(createdBy_id))
                                                userLists.add(new UserList(userId, userName, profile_pic, user_Type, title, is_admin));

                                            userDetailModels.add(new UserList(userId, userName, profile_pic, user_Type, title, is_admin));
                                        }
                                    }
                                    for (int k = 0; k < userLists.size(); k++) {
                                        if (userLists.get(k).getId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID))) {
                                            if(k!=0)
                                            if (userLists.get(k).getIs_admin() != null && userLists.get(k).getIs_admin().equalsIgnoreCase("1")) {
                                                isLogedInUser_Admin = true;
                                            }
                                        }
                                    }
                                    if (userLists.size() > 0) {
                                        memberListingAdapter = new MemberListingAdapter(context, userLists, groupId, isLogedInUser_Admin, description, privacytext);
                                        mRecyclerView.setAdapter(memberListingAdapter);
                                        mRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                                            @Override
                                            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                                int totalItemCount = llm.getItemCount();
                                                int lastVisibleItem = llm.findLastVisibleItemPosition();

                                                if (!isLoading && (totalItemCount - 1) <= (lastVisibleItem)) {
                                                    try {
                                                        String has_more = "";
                                                        if (page < totalItemCount) {
                                                            page++;

                                                            String url = AppUrl.BaseUrl + "feed/home";
                                                            GetMemberListingAsynctask getMemberListingAsynctask = new GetMemberListingAsynctask();
                                                            getMemberListingAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    isLoading = true;
                                                }

                                            }
                                        });

                                    }

                                        /*for (int i = 0; i < userDetailModels.size() + 1; i++) {
                                            if (i == 3)
                                                break;

                                            if (i == 2) {
                                                TextView nameTv = (TextView) view.findViewById(R.id.memberthreename);
                                                TypeFaceMethods.setRegularTypeFaceForTextView(nameTv, context);
                                                nameTv.setText("View All");
                                                CircleImageView imageView = (CircleImageView) view.findViewById(R.id.memthreeimg);
                                                imageView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(context, MemberListing.class);
                                                        intent.putExtra("userList", userDetailModels);
                                                        context.startActivity(intent);
                                                    }
                                                });
                                                nameTv.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(context, MemberListing.class);
                                                        intent.putExtra("userList", userDetailModels);
                                                        context.startActivity(intent);
                                                    }
                                                });

                                            } else {
                                                try {
                                                    if (i == 0) {
                                                        TextView nameTv = (TextView) view.findViewById(R.id.memberonename);

                                                        nameTv.setText(userDetailModels.get(i).getName());
                                                        TypeFaceMethods.setRegularTypeFaceForTextView(nameTv, context);
                                                        final int finalI = i;
                                                        nameTv.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent intent = new Intent(context, Dr_Profile.class);
                                                                if (!userDetailModels.get(finalI).getId().equalsIgnoreCase(userId))

                                                                    intent.putExtra("UserId", userDetailModels.get(finalI).getId());
                                                                context.startActivity(intent);
                                                            }
                                                        });
                                                        CircleImageView imageView = (CircleImageView) view.findViewById(R.id.memoneimg);
                                                        imageView.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent intent = new Intent(context, Dr_Profile.class);
                                                                if (!userDetailModels.get(finalI).getId().equalsIgnoreCase(userId))

                                                                    intent.putExtra("UserId", userDetailModels.get(finalI).getId());
                                                                context.startActivity(intent);
                                                            }
                                                        });
                                                        if (userDetailModels.get(i).getProfile_pic() != null && !userDetailModels.get(i).getProfile_pic().equalsIgnoreCase("")) {
                                                            Transformation transformation = new RoundedTransformationBuilder()
                                                                    .borderColor(Color.BLACK)
                                                                    .borderWidthDp(3)
                                                                    .cornerRadiusDp(30)
                                                                    .oval(false)
                                                                    .build();


                                                            Picasso.with(context)
                                                                    .load(userDetailModels.get(i).getProfile_pic()).transform(transformation)
                                                                    .placeholder(R.drawable.user)
                                                                    .error(R.drawable.user)
                                                                    .into(imageView);

                                                        } }
                                                    if (i == 1) {
                                                        TextView nameTv = (TextView) view.findViewById(R.id.membertwoname);

                                                        nameTv.setText(userDetailModels.get(i).getName());
                                                        TypeFaceMethods.setRegularTypeFaceForTextView(nameTv, context);
                                                        final int finalI = i;
                                                        nameTv.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent intent = new Intent(context, Dr_Profile.class);
                                                                if (!userDetailModels.get(finalI).getId().equalsIgnoreCase(userId))

                                                                    intent.putExtra("UserId", userDetailModels.get(finalI).getId());
                                                                context.startActivity(intent);
                                                            }
                                                        });
                                                        CircleImageView imageView = (CircleImageView) view.findViewById(R.id.memtwoimg);
                                                        imageView.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent intent = new Intent(context, Dr_Profile.class);
                                                                if (!userDetailModels.get(finalI).getId().equalsIgnoreCase(userId))

                                                                    intent.putExtra("UserId", userDetailModels.get(finalI).getId());
                                                                context.startActivity(intent);
                                                            }
                                                        });
                                                        if (userDetailModels.get(i).getProfile_pic() != null && !userDetailModels.get(i).getProfile_pic().equalsIgnoreCase(""))

                                                            Picasso.with(context)
                                                                    .load(userDetailModels.get(i).getProfile_pic())
                                                                    .placeholder(R.drawable.user)
                                                                    .error(R.drawable.user)
                                                                    .into(imageView);

                                                    }
                                                    if (userDetailModels.size() == 1) {
                                                        CircleImageView imageView = (CircleImageView) view.findViewById(R.id.memtwoimg);
                                                        TextView nameTv = (TextView) view.findViewById(R.id.membertwoname);
                                                        imageView.setVisibility(View.GONE);
                                                        nameTv.setVisibility(View.GONE);
                                                        TextView nameTv1 = (TextView) view.findViewById(R.id.memberthreename);
                                                        nameTv1.setVisibility(View.GONE);
                                                        CircleImageView imageView1 = (CircleImageView) view.findViewById(R.id.memthreeimg);
                                                        imageView1.setVisibility(View.GONE);

                                                    }
                                                    if (userDetailModels.size() == 2) {

                                                        TextView nameTv1 = (TextView) view.findViewById(R.id.memberthreename);
                                                        nameTv1.setVisibility(View.GONE);
                                                        CircleImageView imageView1 = (CircleImageView) view.findViewById(R.id.memthreeimg);
                                                        imageView1.setVisibility(View.GONE);

                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }*/

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
                            if (isLogedInUser_Admin) {
                                //  if (createdBy_id.equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID))) {

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
                pairs.add(new BasicNameValuePair("userID", userId));
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
        try {
            PopupMenu popup = new PopupMenu(context, view, Gravity.END);
            switch (i) {
                case 1:
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                    popup.getMenu().findItem(R.id.leave).setVisible(false);
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
                                    setDialog();

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
                                    setDialog();

                               /* if (NetworkUtill.isNetworkAvailable(context)) {
                                    acceptDeclineJoinAsyncTask = new AcceptDeclineJoinAsyncTask(groupId, sesstionManager.getUserDetails().get(SesstionManager.USER_ID), authToken, 2, 0);
                                    acceptDeclineJoinAsyncTask.execute();
                                } else {
                                    NetworkUtill.showNoInternetDialog(context);
                                }*/
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
                    popup.getMenu().findItem(R.id.leave).setTitle("Join");
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {

                                case R.id.leave:
                                    if (NetworkUtill.isNetworkAvailable(context)) {
                                        acceptDeclineJoinAsyncTask = new AcceptDeclineJoinAsyncTask(groupId, sesstionManager.getUserDetails().get(SesstionManager.USER_ID), authToken, 1, 0);
                                        acceptDeclineJoinAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        } catch (Exception e) {
            e.printStackTrace();
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
                                    CommunitiesDetails.moreImageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            setMoreMenu(2);

                                        }
                                    });
                                }
                                if (privacyCheck.equalsIgnoreCase("1") && status1 == 2) {
                                    getActivity().finish();
                                }
                                if (privacyCheck.equalsIgnoreCase("0") && status1 == 2) {
                                    getActivity().finish();

                                    CommunitiesDetails.moreImageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            setMoreMenu(3);

                                        }
                                    });

                                }
                                if (statusData.equalsIgnoreCase("")) {
                                    statusData = "1";
                                }

                            }
                            ApplicationSingleton.setIsGroupUpdated(true);

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
                String url;
                if (statusData.equalsIgnoreCase("")) {
                    url = AppUrl.BaseUrl + "group/invite";

                } else {
                    url = AppUrl.BaseUrl + "group/accept-decline";
                }
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
                if (statusData.equalsIgnoreCase("")) {
                    pairs.add(new BasicNameValuePair("invited_to", userId));

                } else {
                    pairs.add(new BasicNameValuePair("userID", userId));
                }
                pairs.add(new BasicNameValuePair("groupID", groupId));
                if (status1 == 1) {
                    pairs.add(new BasicNameValuePair("addedType", 1 + ""));

                }
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

    public void setDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure you want to leave the community?");// Add the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                if (NetworkUtill.isNetworkAvailable(context)) {
                    acceptDeclineJoinAsyncTask = new AcceptDeclineJoinAsyncTask(groupId, sesstionManager.getUserDetails().get(SesstionManager.USER_ID), authToken, 2, 0);
                    acceptDeclineJoinAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    NetworkUtill.showNoInternetDialog(context);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });
        builder.show();
// Set other dialog properties

// Create the AlertDialog
        AlertDialog dialog = builder.create();
    }

    public class GetMemberListingAsynctask extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        String feedId;


        private String responseBody;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            circularProgressBar.setVisibility(View.GONE);
            try {
                isLoading=false;
                if (jo != null) {
                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObject = jo.getJSONObject("response");
                        if(jsonObject.has("totalpage")&&!jsonObject.isNull("totalpage"))
                        {
                            totalCount=jsonObject.getInt("totalpage");
                        }
                        if (jsonObject.has("members") && !jsonObject.isNull("members")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("members");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String is_admin = null;
                                    String userId = null, userName = null, profile_pic = null, user_Type = null, title = null;
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    if (object.has("user_id") && !object.isNull("user_id")) {
                                        JSONObject userDetail = object.getJSONObject("user_id");
                                        if (userDetail.has("id") && !userDetail.isNull("id")) {
                                            userId = userDetail.getString("id");

                                        }
                                        if (userDetail.has("user_type") && !userDetail.isNull("user_type")) {
                                            user_Type = userDetail.getString("user_type");
                                        }
                                        if (userDetail.has("Title") && !userDetail.isNull("Title")) {
                                            title = userDetail.getString("Title");
                                        }
                                        if (userDetail.has("name") && !userDetail.isNull("name")) {
                                            userName = userDetail.getString("name");

                                        }
                                        if (userDetail.has("profile_pic") && !userDetail.isNull("profile_pic")) {
                                            profile_pic = userDetail.getString("profile_pic");
                                        }
                                        if (object.has("is_admin") && !object.isNull("is_admin")) {
                                            is_admin = object.getString("is_admin");
                                        }


                                        userDetailModels.add(new UserList(userId, userName, profile_pic, user_Type, title, is_admin));
                                    }
                                }
                                if (page == 3) {
                                    memberListingAdapter = new MemberListingAdapter(context, userDetailModels, groupId, isLogedInUser_Admin, description, privacytext);
                                    mRecyclerView.setAdapter(memberListingAdapter);
                                    mRecyclerView.scrollToPosition(memberListingAdapter.getItemCount()-1);
                                    mRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                                        @Override
                                        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                            int totalItemCount = llm.getItemCount();
                                            int lastVisibleItem = llm.findLastVisibleItemPosition();

                                            if (!isLoading && (totalItemCount - 1) <= (lastVisibleItem)) {
                                                try {
                                                    String has_more = "";
                                                    if (page < totalCount) {
                                                        page++;

                                                        String url = AppUrl.BaseUrl + "feed/home";
                                                        GetMemberListingAsynctask getMemberListingAsynctask = new GetMemberListingAsynctask();
                                                        getMemberListingAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                isLoading = true;
                                            }

                                        }
                                    });

                                } else {
                                    synchronized (memberListingAdapter) {
                                        memberListingAdapter.notifyDataSetChanged();
                                    }
                                }

                                for (int k = 0; k < userLists.size(); k++) {
                                    if (userLists.get(k).getId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID))) {
                                        if (userLists.get(k).getIs_admin() != null && userLists.get(k).getIs_admin().equalsIgnoreCase("1")) {
                                            isLogedInUser_Admin = true;
                                        }
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

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = AppUrl.BaseUrl + "group/members";
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
                pairs.add(new BasicNameValuePair("pageNo", page + ""));
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

}
