package com.app.nirogstreet.activites;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.app.nirogstreet.adapter.MemberListingAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.SearchModel;
import com.app.nirogstreet.model.UserList;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Preeti on 30-11-2017.
 */

public class MemberListing extends Activity {

    ArrayList<UserList> searchModels;
    EditText searchET;
    private RecyclerView mRecyclerView;
    ImageView imageViewSearch;
    ImageView backImageView;
    CircularProgressBar circularProgressBar;
    private LinearLayoutManager mLinearLayoutManager;
    private String text = "";
    private String query = "";
    SesstionManager sessionManager;
    private  String authToken, userId;
MemberListingAdapter memberListingAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.members_list);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
        searchModels=new ArrayList<>();
        if(getIntent().hasExtra("userList"))
        {
            searchModels= (ArrayList<UserList>) getIntent().getSerializableExtra("userList");
        }
        sessionManager = new SesstionManager(MemberListing.this);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(MemberListing.this);
        mRecyclerView.setLayoutManager(llm);
        llm.setOrientation(LinearLayoutManager.VERTICAL);        HashMap<String, String> userDetails = sessionManager.getUserDetails();
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
        if(searchModels.size()>0) {
            memberListingAdapter = new MemberListingAdapter(MemberListing.this, searchModels);
            mRecyclerView.setAdapter(memberListingAdapter);
        }
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


    }

}
