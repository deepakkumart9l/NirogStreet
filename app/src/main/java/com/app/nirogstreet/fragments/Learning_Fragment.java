package com.app.nirogstreet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.nirogstreet.BharamTool.BharmTool_Cat;
import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.CoursesListing;
import com.app.nirogstreet.activites.Journals;
import com.app.nirogstreet.activites.KnowledgeListing;

/**
 * Created by as on 2/22/2018.
 */

public class Learning_Fragment extends Fragment implements View.OnClickListener {
    LinearLayout mBharmtool_layout,coursesLinearLayout,journalsLinearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.learning_frag_lay, container, false);
        mBharmtool_layout = (LinearLayout) v.findViewById(R.id.bharmtool_layout);
        coursesLinearLayout=(LinearLayout)v.findViewById(R.id.courses);
        journalsLinearLayout=(LinearLayout)v.findViewById(R.id.journals) ;
        journalsLinearLayout.setOnClickListener(this);
        coursesLinearLayout.setOnClickListener(this);
        mBharmtool_layout.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bharmtool_layout:
                Intent intent = new Intent(getActivity(), BharmTool_Cat.class);
                startActivity(intent);
                break;
            case R.id.courses:
                Intent intent1 = new Intent(getActivity(), CoursesListing.class);
                startActivity(intent1);
                break;
            case R.id.journals:
                Intent intent2 = new Intent(getActivity(), Journals.class);
                startActivity(intent2);
                break;
        }
    }
}
