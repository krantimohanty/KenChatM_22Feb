package com.kencloud.workchat.app_adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.kencloud.workchat.app_widget.CircularImageView;
import com.kencloud.workchat.R;
import com.kencloud.workchat.app_model.Contact;

/**
 * Contains a Contact List Item
 */
public class ContactViewHolder extends RecyclerView.ViewHolder {
    private CircularImageView mImage;
    private TextView mLabel,mNum;
    private Contact mBoundContact; // Can be null

    public ContactViewHolder(final View itemView) {
        super(itemView);
        mImage = (CircularImageView) itemView.findViewById(R.id.rounded_iv_profile);
        mLabel = (TextView) itemView.findViewById(R.id.tv_label);
        mNum = (TextView) itemView.findViewById(R.id.contact_no);
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mBoundContact != null) {
//                    Toast.makeText(
//                            itemView.getContext(),
//                            "Hi, I'm " + mBoundContact.name,
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    public void bind(Contact contact) {
        mBoundContact = contact;
        mLabel.setText(contact.name);
        mNum.setText(String.valueOf(contact.contactNo));
        Picasso.with(itemView.getContext())
                .load(contact.profilePic)
                .placeholder(R.drawable.profile_icon)
                .error(R.drawable.profile_icon)
                .into(mImage);
    }
}
