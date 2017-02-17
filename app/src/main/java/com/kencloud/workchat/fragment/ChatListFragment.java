package com.kencloud.workchat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kencloud.workchat.ChatHistoryActivity;
import com.kencloud.workchat.R;

public class ChatListFragment extends Fragment  {
    private View rootView;
    private RecyclerView mRecyclerView;
    LinearLayout layout, layout1, layout2, layout3, layout4, layout5, layout6;

    public ChatListFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        layout = (LinearLayout) rootView.findViewById(R.id.chat_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChatHistoryActivity.class));
            }
        });

        layout1 = (LinearLayout) rootView.findViewById(R.id.chat_layout1);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChatHistoryActivity.class));
            }
        });
        layout2 = (LinearLayout) rootView.findViewById(R.id.chat_layout2);
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChatHistoryActivity.class));
            }
        });
        layout3 = (LinearLayout) rootView.findViewById(R.id.chat_layout3);
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ChatHistoryActivity.class));
            }
        });
        layout4 = (LinearLayout) rootView.findViewById(R.id.chat_layout4);
        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChatHistoryActivity.class));
            }
        });


//        //recycle view
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



