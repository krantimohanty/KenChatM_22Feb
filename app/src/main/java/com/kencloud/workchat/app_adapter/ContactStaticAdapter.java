package com.kencloud.workchat.app_adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;


import com.kencloud.workchat.ChatActivity;
import com.kencloud.workchat.R;
import com.kencloud.workchat.app_model.ContactListModel;
import com.kencloud.workchat.app_widget.CircularImageView;

import java.util.List;

/**
 * Created by suchismita.p on 8/8/2016.
 */
public class ContactStaticAdapter extends RecyclerView.Adapter {
    private List<ContactListModel> listModels;
    Context context;
    RecyclerView mRecyclerView;

    public ContactStaticAdapter(Context context, List<ContactListModel> listModels, RecyclerView mRecyclerView) {

        this.context = context;
        this.listModels = listModels;
        this.mRecyclerView = mRecyclerView;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list, parent, false);


        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final ImageViewHolder holder = (ImageViewHolder) viewHolder;

        /*holder.head_name.setText(photoModels.get(position).getTitle());
        holder.head_name.setTextColor(Color.WHITE);*/
//        try {
//           Picasso.with(context)
//        .load(enquiryModels.get(position).getProfilePic())
//                .error(R.drawable.com_facebook_profile_picture_blank_portrait)
//                .into( holder.profile_pic);
//
//        }catch (Exception e){
//
//        }

        holder.contact_name.setText(listModels.get(position).getContactName());
        holder.contact_no.setText(listModels.get(position).getContactNo());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatActivity.class);
//                intent.putExtra("person_name", listModels.get(position).getContactName());
//                intent.putExtra("enquiry_for", enquiryModels.get(position).getEnquiryType());
//                intent.putExtra("enquiry_query", enquiryModels.get(position).getQueries());
//                intent.putExtra("enquiry_phno", enquiryModels.get(position).getPhoneNo());
//                intent.putExtra("enquiry_area", enquiryModels.get(position).getArea());
//                intent.putExtra("bulletin_image", enquiryModels.get(position).getBulletinImage());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listModels==null?0:listModels.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        private ScrollView scrlvw;
        private AppCompatTextView contact_name;
        private AppCompatTextView contact_no;
        private LinearLayout event_row;
        private CardView cardview;
        private CircularImageView profile_pic;
        // endregion

        // region Constructors
        public ImageViewHolder(final View view) {
            super(view);


            //mPhotoView = (ImageView) view.findViewById(R.id.photos);
            //head_name=(TextView)view.findViewById(R.id.caption);
            //mFrameLayout = (FrameLayout) view.findViewById(R.id.fl);
            scrlvw = (ScrollView) view.findViewById(R.id.scrollview);
//            cardview = (CardView) itemView.findViewById(R.id.card_view);
            contact_name = (AppCompatTextView) itemView.findViewById(R.id.contact_name);

            contact_no = (AppCompatTextView) itemView.findViewById(R.id.contact_no);
//
//            profile_pic = (CircularImageView) itemView.findViewById(R.id.circleImage);
////            profile_pic.setBorderColor(getResources().getColor(R.color.grey));
//            profile_pic.setBorderWidth(10);

        }

        // endregion

    }

}
