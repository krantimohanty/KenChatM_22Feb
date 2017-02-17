package com.kencloud.workchat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kencloud.workchat.ChatActivity;
import com.kencloud.workchat.R;
import com.kencloud.workchat.app_adapter.ContactStaticAdapter;
import com.kencloud.workchat.app_model.ContactListModel;

import java.util.List;


public class ContactStaticFragment extends Fragment {
//    private ListView listView;
//    private List<ContactBean> list = new ArrayList<ContactBean>();

   private RecyclerView mRecyclerView;
    private View rootView;
    List<ContactListModel> contactModel;
    ContactStaticAdapter contactAdapter;
LinearLayout layout,layout1,layout2,layout3,layout4,layout5,layout6;
    LinearLayoutManager mLinearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contact, container, false);
//        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });
        layout = (LinearLayout) rootView.findViewById(R.id.linearLayout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChatActivity.class));
            }
        });

        layout1 = (LinearLayout) rootView.findViewById(R.id.linearLayout1);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChatActivity.class));
            }
        });
        layout2 = (LinearLayout) rootView.findViewById(R.id.linearLayout2);
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChatActivity.class));
            }
        });
        layout3 = (LinearLayout) rootView.findViewById(R.id.linearLayout3);
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChatActivity.class));
            }
        });
        layout4 = (LinearLayout) rootView.findViewById(R.id.linearLayout4);
        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChatActivity.class));
            }
        });

        layout5 = (LinearLayout) rootView.findViewById(R.id.linearLayout5);
        layout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChatActivity.class));
            }
        });




//        contactModel = new ArrayList<>();
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.contact_list);
//
//        mLinearLayoutManager = new LinearLayoutManager(getActivity());
//        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mLinearLayoutManager.scrollToPosition(0);
//        mRecyclerView.setLayoutManager(mLinearLayoutManager);
//        contactAdapter = new ContactStaticAdapter(getContext(), contactModel, mRecyclerView);
//        mRecyclerView.setAdapter(contactAdapter);
//
        return rootView;
    }
}


