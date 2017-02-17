package com.kencloud.workchat;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.kencloud.workchat.app_adapter.GroupListAdapter;
import com.kencloud.workchat.app_model.GruopMessage;
import com.kencloud.workchat.util.Utils;

import java.util.Random;

public class GroupActivity extends ListActivity  {

    // TODO: change this to your own Firebase URL
    private static final String FIREBASE_URL = "https://kenchat-a1275.firebaseio.com/";
    //https://kenchatmaster.firebaseio.com  //https://android-chat.firebaseio-demo.com
    private String mUsername;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private GroupListAdapter mChatListAdapter;
    private EditText inputText;
    private TextView isTyping;
    private boolean typingStarted;

    private static final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
//        inputText.addTextChangedListener(new TextWatcher() {
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//            public void afterTextChanged(Editable s) {
//                if (!TextUtils.isEmpty(s.toString()) && s.toString().trim().length() == 1) {
//                    //Log.i(TAG, “typing started event…”);
//                    typingStarted = true;
//                    //send typing started status
//                } else if (s.toString().trim().length() == 0 && typingStarted) {
//                    //Log.i(TAG, “typing stopped event…”);
//                    typingStarted = false;
//                    //send typing stopped status
//                }
//            }
//        });
        // Make sure we have a mUsername
        setupUsername();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("KenChat");
        //setSupportActionBar(toolbar);

        // Setup our Firebase mFirebaseRef
        mFirebaseRef = new Firebase(FIREBASE_URL).child("chat");

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new GroupListAdapter(mFirebaseRef.limit(50), this, R.layout.group_list_message_parent, mUsername);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
//        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                boolean connected = (Boolean) dataSnapshot.getValue();
//                if (connected) {
//                    Toast.makeText(GroupActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(GroupActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                // No-op
//            }
//        });
    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
//        mChatListAdapter.cleanup();
//    }

    private void setupUsername() {
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        mUsername = prefs.getString("username", null);
        if (mUsername == null) {
            Random r = new Random();
            // Assign a random user name if we don't have one saved.
            //mUsername = "KenChatUser" + r.nextInt(100000);
            //mUsername = "User" + r.nextInt(100);
            int permissionCheckForReadPhoneState = ContextCompat.checkSelfPermission(GroupActivity.this,
                    android.Manifest.permission.READ_PHONE_STATE);

            // READ_PHONE_STATE
            if(permissionCheckForReadPhoneState !=  PackageManager.PERMISSION_GRANTED
                    || permissionCheckForReadPhoneState !=  PackageManager.PERMISSION_GRANTED){
                // Ask Permission for READ_PHONE_STATE
                ActivityCompat.requestPermissions(GroupActivity.this,
                        new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        REQUEST_CODE_PERMISSION_READ_PHONE_STATE);
            } else {
                // Read Contacts Granted
                getDeviceIMEINumberAndSaveAsUserName();
            }
        }
    }

    private void getDeviceIMEINumberAndSaveAsUserName(){
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        mUsername = Utils.getDeviceIMEINumber(GroupActivity.this);
        prefs.edit().putString("username", mUsername).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    getDeviceIMEINumberAndSaveAsUserName();
                } else {
                    // permission denied
                }
                return;
            }
        }
    }

    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a GruopMessage object
            GruopMessage gruopMessage = new GruopMessage(input, mUsername, "", Constants.MESSAGE_TYPE_TEXT);
            // Create a new, auto-generated child of that gruopMessage location, and save our gruopMessage data there
            mFirebaseRef.push().setValue(gruopMessage);
            inputText.setText("");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present

        getMenuInflater().inflate(R.menu.chat_menu, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("menu", String.valueOf(item));
        switch (item.getItemId()) {
            case R.id.attachment:
//                Toast.makeText(getApplicationContext(), "Search Selected", Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.action_contact:
//                startActivity(new Intent(MainActivity.this,GroupActivity.class));
//                Toast.makeText(getApplicationContext(), "New Contact Selected", Toast.LENGTH_LONG).show();
//                return true;
            case R.id.new_group:
                Toast.makeText(getApplicationContext(), "New Group Selected", Toast.LENGTH_LONG)
                        .show();
                return true;
            case R.id.change_status:
                Toast.makeText(getApplicationContext(), "Status Selected", Toast.LENGTH_LONG)
                        .show();
                return true;
            case R.id.chat_settings:
                Toast.makeText(getApplicationContext(), "Setting Selected", Toast.LENGTH_LONG)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
