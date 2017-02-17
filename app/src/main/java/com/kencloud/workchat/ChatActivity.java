package com.kencloud.workchat;

/**
 * Created by suchismita.p on 8/9/2016.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.kencloud.workchat.app_adapter.ChatArrayAdapter;
import com.kencloud.workchat.app_util.PopupHelper;

public class ChatActivity extends AppCompatActivity {
    //    private static final String TAG = "ChatActivity";
    private String mMessageId, userPhoneNumber;
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private ImageView img;
    private Button buttonSend;
    private boolean side = false;
    LinearLayout attachmentLayout, cameraLayout, galleryLayout, audioLayout,
            locLayout, contactLayout, videoLayout, contactAddLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("KenChat");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
       ImageView img = (ImageView) findViewById(R.id.btnSend);
        attachmentLayout = (LinearLayout) findViewById(R.id.lin);
        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
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
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.chat_menu, menu);
        return true;
    }
//    public boolean onContextItemSelected(MenuItem item) {
//        onOptionsItemSelected(item);
//        return true;
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("menu", String.valueOf(item));
        switch (item.getItemId()) {
            case android.R.id.home:
                // ProjectsActivity is my 'home' activity
                ChatActivity.this.finish();
                return true;

            case R.id.attachment:
                View menuItemView = findViewById(R.id.attachment);
                showPopup(menuItemView);
                return true;
            case R.id.new_group:
//                Toast.makeText(getApplicationContext(), "Item 2 Selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.change_status:
//                Toast.makeText(getApplicationContext(), "Item 3 Selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

    protected void addNewContact() {
		/*Intent intent = new Intent(Intent.ACTION_INSERT,
                ContactsContract.Contacts.CONTENT_URI);

        startActivity(intent);*/
        Intent addContactIntent = new Intent(Intent.ACTION_INSERT);
        addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, userPhoneNumber);
        startActivityForResult(addContactIntent, 11);

    }

    private boolean sendChatMessage() {
        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
        chatText.setText("");
        side = !side;
        return true;
    }

    protected void getGallaryImage() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, 2000);

    }

    protected void getCameraImage() {
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 3000);

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

    protected void getPhoneContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, Constants.CONTACT_PICKER_RESULT);

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
                getCameraImage();

            }
        });

        galleryLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPopup.dismiss();
                getGallaryImage();

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
        ;

        showPopup.setContentView(popupView);
        showPopup.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        showPopup.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        showPopup.setAnimationStyle(R.style.Animations_GrowFromTop);
        showPopup.showAsDropDown(view);
    }
}