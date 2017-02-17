package com.kencloud.workchat.app_adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Query;
import com.kencloud.workchat.Constants;
import com.kencloud.workchat.app_model.GruopMessage;
import com.kencloud.workchat.FirebaseListAdapter;
import com.kencloud.workchat.R;
import com.kencloud.workchat.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author greg
 * @since 6/21/13
 *
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>GruopMessage</code> class to encapsulate the
 * data for each individual chat message
 */
public class GroupListAdapter extends FirebaseListAdapter<GruopMessage> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;
    private TextView chatText;
    private Context context;
    Activity activity;
    LayoutInflater inflater;

    public GroupListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, GruopMessage.class, layout, activity);
        this.mUsername = mUsername;
        this.activity = activity;
        this.context = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * Bind an instance of the <code>GruopMessage</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>GruopMessage</code> instance that represents the current data to bind.
     *
     * @param view         A view instance corresponding to the layout we passed to the constructor.
     * @param groupMessage An instance representing the current state of a groupMessage message
     */
    @Override
    protected void populateView(View view, GruopMessage groupMessage) {

        LinearLayout layoutTextMessage = (LinearLayout) view.findViewById(R.id.layout_file_type_message);
        LinearLayout layoutImage = (LinearLayout) view.findViewById(R.id.layout_file_type_image);

        int message_type = groupMessage.getType();




























        
        switch (message_type){
            case Constants.MESSAGE_TYPE_AUDIO:
                layoutTextMessage.setVisibility(View.GONE);
                layoutImage.setVisibility(View.GONE);
                break;
            case Constants.MESSAGE_TYPE_IMAGE:
                layoutTextMessage.setVisibility(View.GONE);
                layoutImage.setVisibility(View.VISIBLE);

                LinearLayout img_chat_parent_ll = (LinearLayout) view.findViewById(R.id.img_chat_parent_ll);
                String sender_name = groupMessage.getAuthor();
                if(sender_name != null && sender_name.trim().length() > 0
                        && sender_name.equalsIgnoreCase(Utils.getDeviceIMEINumber(context))){
                    img_chat_parent_ll.setGravity(Gravity.RIGHT);
                } else {
                    img_chat_parent_ll.setGravity(Gravity.LEFT);
                }

                String base64 = groupMessage.getMessage();
                if(base64 != null && base64.trim().length() > 0) {
                    String file_name = groupMessage.getFilename();
                    try {
                        File file = getFileFromBase64(base64, file_name);
                        if (file != null && file.exists()) {
                            int image_width_height = Utils.dpToPx(context,
                                    context.getResources().getInteger(R.integer.chat_message_image_width));
                            Bitmap bitmap = Utils.getScaledBitmap(file.getPath(), image_width_height, image_width_height);
                            ImageView imgViewPicture = (ImageView) view.findViewById(R.id.imgViewPicture);
                            imgViewPicture.setImageBitmap(bitmap);
                        }
                    } catch(IOException exception){
                        Log.e("GroupListAdapter", exception.getMessage(), exception);
                    }
                }

                TextView img_sender_user_name = (TextView) view.findViewById(R.id.img_sender_user_name);
                img_sender_user_name.setText(sender_name+" :");
                break;
            case Constants.MESSAGE_TYPE_VIDEO:
                layoutTextMessage.setVisibility(View.GONE);
                layoutImage.setVisibility(View.GONE);
                break;
            case Constants.MESSAGE_TYPE_DOC:
                layoutTextMessage.setVisibility(View.GONE);
                layoutImage.setVisibility(View.GONE);
                break;
            case Constants.MESSAGE_TYPE_MAP:
                layoutTextMessage.setVisibility(View.GONE);
                layoutImage.setVisibility(View.GONE);
                break;
            case Constants.MESSAGE_TYPE_TEXT:
                LinearLayout sdfg = layoutTextMessage;
                layoutTextMessage.setVisibility(View.VISIBLE);
                layoutImage.setVisibility(View.GONE);

                // Map a GruopMessage object to an entry in our listview
                LinearLayout chat_text_msg_parent_ll = (LinearLayout) view.findViewById(R.id.chat_text_msg_parent_ll);
                String author = groupMessage.getAuthor();
                if(author != null && author.trim().length() > 0
                        && author.equalsIgnoreCase(Utils.getDeviceIMEINumber(context))){
                    chat_text_msg_parent_ll.setGravity(Gravity.RIGHT);
                } else {
                    chat_text_msg_parent_ll.setGravity(Gravity.LEFT);
                }
                TextView authorText = (TextView) view.findViewById(R.id.author);
                TextView messageText = (TextView) view.findViewById(R.id.message);
                authorText.setText(author + ": ");
                // If the message was sent by this user, color it differently
                if (author != null && author.equals(mUsername)) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
                    params.weight = 1.0f;
                    params.gravity = Gravity.RIGHT;

                    authorText.setTextColor(view.getResources().getColor(R.color.red));
                    authorText.setLayoutParams(params);

                    messageText.setText(groupMessage.getMessage());
                    messageText.setBackground(view.getResources().getDrawable(R.drawable.bg_msg_you));
                    messageText.setLayoutParams(params);

                } else {
                    authorText.setTextColor(view.getResources().getColor(R.color.blue));
                    authorText.setGravity(Gravity.LEFT | Gravity.START);

                    messageText.setText(groupMessage.getMessage());
                    messageText.setGravity(Gravity.LEFT);
                    messageText.setBackground(view.getResources().getDrawable(R.drawable.bg_msg_from));
                }
                break;
        }
    }

    private File getFileFromBase64(String base64ImageData, String file_name) throws IOException {
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + Constants.APP_IMAGE_STORAGE_DIRECTORY_NAME + File.separator);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(folder.getAbsolutePath(), file_name);
        try {
            // Make sure the Pictures directory exists.
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos = null;
        try {
            if (base64ImageData != null) {
                fos = new FileOutputStream(file);//context.openFileOutput("imageName.png", Context.MODE_PRIVATE);
                byte[] decodedString = android.util.Base64.decode(base64ImageData, android.util.Base64.DEFAULT);
                fos.write(decodedString);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {

        } finally {
            if (fos != null) {
                fos = null;
            }
        }
        return file;
    }
}
