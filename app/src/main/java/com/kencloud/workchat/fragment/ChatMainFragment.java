package com.kencloud.workchat.fragment;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.kencloud.workchat.ChatMessage;
import com.kencloud.workchat.R;
import com.kencloud.workchat.app_adapter.ChatArrayAdapter;

/**
 * Created by suchismita.p on 8/9/2016.
 */
public class ChatMainFragment extends Fragment {
    private View rootView;
    private RecyclerView mRecyclerView;
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;

    public ChatMainFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.activity_chat, container, false);
//        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.chat_list);
//        mRecyclerView.setHasFixedSize(true);

//        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
//        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mLinearLayoutManager.scrollToPosition(0);
//        mRecyclerView.setLayoutManager(mLinearLayoutManager);
//        buttonSend = (Button) rootView.findViewById(R.id.send);

        listView = (ListView) rootView.findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) rootView.findViewById(R.id.messageInput);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
        return rootView;
    }


    private boolean sendChatMessage() {
        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
        chatText.setText("");
        side = !side;
        return true;
    }
        //recycle view

//        BlogsAdapter adapter = new BlogsAdapter();
//        mRecyclerView.setAdapter(adapter);

    }



