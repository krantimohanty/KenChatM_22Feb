package com.kencloud.workchat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.tubesock.Base64;
import com.kencloud.workchat.app_adapter.GroupListAdapter;
import com.kencloud.workchat.app_model.GruopMessage;
import com.kencloud.workchat.app_util.PopupHelper;
import com.kencloud.workchat.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class ChatHistoryActivity extends AppCompatActivity {

    // TODO: change this to your own Firebase URL
    private static final String FIREBASE_URL = "https://kenchat-a1275.firebaseio.com/sctlchats";
    //https://kenchatmaster.firebaseio.com  //https://android-chat.firebaseio-demo.com
    private String mUsername;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private GroupListAdapter mChatListAdapter;
    private EditText inputText;
    private TextView isTyping;
    private boolean typingStarted;

    private LinearLayout attachmentLayout, cameraLayout, galleryLayout, audioLayout,
            locLayout, contactLayout, videoLayout, contactAddLayout;

    private static final int RESULT_CODE_IMAGE = 2000;

    private static final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 122;
    private static final int REQUEST_CODE_PERMISSION_GALLARY = 123;
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);
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
        setSupportActionBar(toolbar);

        // Setup our Firebase mFirebaseRef
        mFirebaseRef = new Firebase(FIREBASE_URL).child("chat");

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(Constants.MESSAGE_TYPE_TEXT);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = (ListView) findViewById(R.id.listView);
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
            int permissionCheckForReadPhoneState = ContextCompat.checkSelfPermission(ChatHistoryActivity.this,
                    android.Manifest.permission.READ_PHONE_STATE);

            // READ_PHONE_STATE
            if(permissionCheckForReadPhoneState !=  PackageManager.PERMISSION_GRANTED
                    || permissionCheckForReadPhoneState !=  PackageManager.PERMISSION_GRANTED){
                // Ask Permission for READ_PHONE_STATE
                ActivityCompat.requestPermissions(ChatHistoryActivity.this,
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
        mUsername = Utils.getDeviceIMEINumber(ChatHistoryActivity.this);
        prefs.edit().putString("username", mUsername).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_PHONE_STATE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    getDeviceIMEINumberAndSaveAsUserName();
                } else {
                    // permission denied
                }
                break;
            case REQUEST_CODE_PERMISSION_GALLARY :
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    getGallaryImage();
                } else {
                    // permission denied
                }
                break;
            case REQUEST_CODE_PERMISSION_CAMERA :
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    getCameraImage();
                } else {
                    // permission denied
                }
                break;
        }
    }

    private void sendMessage(int messageType, String base64, String file_name) {
        switch (messageType){
            case Constants.MESSAGE_TYPE_AUDIO:

                break;
            case Constants.MESSAGE_TYPE_IMAGE:
                if(base64 != null && base64.trim().length() > 0){
                    GruopMessage gruopMessage = new GruopMessage(base64, mUsername, file_name,
                            Constants.MESSAGE_TYPE_IMAGE);
                    mFirebaseRef.push().setValue(gruopMessage, new Firebase.CompletionListener(){
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            mChatListAdapter.notifyDataSetChanged();
                        }
                    });
                }
                break;
            case Constants.MESSAGE_TYPE_VIDEO:

                break;
            case Constants.MESSAGE_TYPE_DOC:

                break;
            case Constants.MESSAGE_TYPE_MAP:

                break;
            case Constants.MESSAGE_TYPE_TEXT:
                EditText inputText = (EditText) findViewById(R.id.messageInput);
                String input = inputText.getText().toString();
                if (!input.equals("")) {
                    // Create our 'model', a GruopMessage object
                    GruopMessage gruopMessage = new GruopMessage(input, mUsername, "", Constants.MESSAGE_TYPE_TEXT);
                    // Create a new, auto-generated child of that gruopMessage location, and save our gruopMessage data there
                    mFirebaseRef.push().setValue(gruopMessage);
                    inputText.setText("");
                }
                break;
        }
    }

    private void sendMessage(int messageType) {
        switch (messageType){
            case Constants.MESSAGE_TYPE_TEXT:
                EditText inputText = (EditText) findViewById(R.id.messageInput);
                String input = inputText.getText().toString();
                if (!input.equals("")) {
                    // Create our 'model', a GruopMessage object
                    GruopMessage gruopMessage = new GruopMessage(input, mUsername, "", Constants.MESSAGE_TYPE_TEXT);
                    // Create a new, auto-generated child of that gruopMessage location, and save our gruopMessage data there
                    mFirebaseRef.push().setValue(gruopMessage);
                    inputText.setText("");
                }
                break;
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
                View view = findViewById(R.id.attachment);
                showPopup(view);
                return true;
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

    private void showPopup(View view) {
        final PopupWindow showPopup = PopupHelper
                .newBasicPopupWindow(getApplicationContext());
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.attachment_view, null);
        cameraLayout = (LinearLayout) popupView.findViewById(R.id.lin_camera);
        galleryLayout = (LinearLayout) popupView.findViewById(R.id.lin_gallery);
        audioLayout = (LinearLayout) popupView.findViewById(R.id.lin_audio);
        locLayout = (LinearLayout) popupView.findViewById(R.id.lin_location);
        contactLayout = (LinearLayout) popupView.findViewById(R.id.lin_contact);
        videoLayout = (LinearLayout) popupView.findViewById(R.id.lin_video);
        cameraLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showPopup.dismiss();
                boolean granted = checkPermissionForReadAndWriteExternalStorage(REQUEST_CODE_PERMISSION_CAMERA);
                if(granted) {
                    getCameraImage();
                }
            }
        });

        galleryLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPopup.dismiss();
                boolean granted = checkPermissionForReadAndWriteExternalStorage(REQUEST_CODE_PERMISSION_GALLARY);
                if(granted) {
                    getGallaryImage();
                }
            }
        });
        audioLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPopup.dismiss();
                chooseAudioFile();
            }
        });

        videoLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPopup.dismiss();
                chooseVideoFile();
            }
        });

        contactLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPopup.dismiss();
                getPhoneContact();
            }
        });

        showPopup.setContentView(popupView);
        showPopup.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        showPopup.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        showPopup.setAnimationStyle(R.style.Animations_GrowFromTop);
        showPopup.showAsDropDown(view);
    }

    private boolean checkPermissionForReadAndWriteExternalStorage(int request_code_permission){
        int permissionCheckForReadExternalStorage = ContextCompat.checkSelfPermission(ChatHistoryActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheckForWriteExternalStorage = ContextCompat.checkSelfPermission(ChatHistoryActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // READ_PHONE_STATE
        if(permissionCheckForReadExternalStorage !=  PackageManager.PERMISSION_GRANTED
                || permissionCheckForWriteExternalStorage !=  PackageManager.PERMISSION_GRANTED){
            // Ask Permission for READ_PHONE_STATE
            ActivityCompat.requestPermissions(ChatHistoryActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    request_code_permission);
            return false;
        } else {
            // Read Contacts Granted
            return true;
        }
    }

    protected void getCameraImage() {
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 3000);
    }

    protected void getPhoneContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, Constants.CONTACT_PICKER_RESULT);
    }

    protected void chooseVideoFile() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("video/*");
        startActivityForResult(i, 222);
    }

    protected void chooseAudioFile() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        startActivityForResult(i, 111);
    }
    protected void getGallaryImage() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_CODE_IMAGE);
    }

    private String getBase64StringFromFile(String picturePath, String file_name) throws FileNotFoundException {

        InputStream inputStream = new FileInputStream(picturePath);//You can get an inputStream using any IO API
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String base64 = Base64.encodeToString(bytes, false);

        return base64;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);
            cursor.close();

            final File file = new File(picturePath);
            if(file != null && file.exists()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatHistoryActivity.this);
                builder.setTitle("Send Picture");
                builder.setMessage("Are you sure want to upload this picture?");

                String positiveText = getString(android.R.string.ok);
                builder.setPositiveButton(positiveText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    String file_name = file.getName();
                                    String encodedString = getBase64StringFromFile(picturePath, file_name);
                                    sendMessage(Constants.MESSAGE_TYPE_IMAGE, encodedString, file_name);
                                } catch(FileNotFoundException exception){
                                    Log.e("ChatHistoryActivity", exception.getMessage(), exception);
                                }
                            }
                        });

                String negativeText = getString(android.R.string.cancel);
                builder.setNegativeButton(negativeText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatHistoryActivity.this);
                builder.setTitle("Send Picture");
                builder.setMessage("File is not exists.");

                String positiveText = getString(android.R.string.ok);
                builder.setPositiveButton(positiveText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}
