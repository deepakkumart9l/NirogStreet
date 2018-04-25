package com.app.nirogstreet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.MemberListingAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.LikesModel;
import com.app.nirogstreet.model.UserList;
import com.app.nirogstreet.uttil.LetterTileProvider;
import com.app.nirogstreet.uttil.SesstionManager;

import java.util.ArrayList;

/**
 * Created by as on 4/3/2018.
 */

public class MyAbout_Fragment extends Fragment {
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
    About_Fragment.AcceptDeclineJoinAsyncTask acceptDeclineJoinAsyncTask;
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
    About_Fragment.GetCommunityDetailAsyncTask getCommunityDetailAsyncTask;
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


}
