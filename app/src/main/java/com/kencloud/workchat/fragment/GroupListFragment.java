package com.kencloud.workchat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.kencloud.workchat.GroupActivity;
import com.kencloud.workchat.NewGroupActivity;
import com.kencloud.workchat.R;

public class GroupListFragment extends Fragment {
    private View rootView;
    private RecyclerView mRecyclerView;
private LinearLayout layout,newlayout;
    public GroupListFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_group, container, false);
        layout=(LinearLayout)rootView.findViewById(R.id.group);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GroupActivity.class));
            }
        });
        newlayout=(LinearLayout)rootView.findViewById(R.id.groupNew);
        newlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewGroupActivity.class));
            }
        });
        //recycle view
//        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.chat_list);
//        mRecyclerView.setHasFixedSize(true);
//
//        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
//        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mLinearLayoutManager.scrollToPosition(0);
//        mRecyclerView.setLayoutManager(mLinearLayoutManager);
//        BlogsAdapter adapter = new BlogsAdapter();
//        mRecyclerView.setAdapter(adapter);
        return rootView;
    }
}
