package com.kencloud.workchat.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kencloud.workchat.ChatActivity;
import com.kencloud.workchat.R;
import com.kencloud.workchat.app_adapter.ContactsListAdapter;

/**
 * Fragment that holds the RecyclerView
 */
public class ContactsFragment extends Fragment  {

    private static final String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    };

    // TODO: Implement a more advanced example that makes use of this
    private static final String SELECTION = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + ContactsContract.Contacts._ID;

    // Defines a variable for the search string
    private String mSearchString = "";
    // Defines the array to hold values that replace the ?
    private String[] mSelectionArgs = { mSearchString };

    private RecyclerView mContactListView;


    // Empty public constructor, required by the system
    public ContactsFragment() {}

    // A UI Fragment must inflate its View
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        View root = inflater.inflate(R.layout.contacts_list, container, false);
        mContactListView = (RecyclerView) root.findViewById(R.id.rv_contact_list);
        mContactListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mContactListView.setItemAnimator(new DefaultItemAnimator());

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initializes a loader for loading the contacts
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                /*
                 * Makes search string into pattern and
                 * stores it in the selection array
                 */
                Uri contentUri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_URI,
                        Uri.encode(mSearchString));
                // Starts the query
                return new CursorLoader(
                        getActivity(),
                        contentUri,
                        PROJECTION,
                        null,
                        null,
                        null);
            }


            @Override
            public void onLoadFinished(Loader<Cursor> objectLoader, Cursor c) {

                // Put the result Cursor in the adapter for the ListView
                mContactListView.setAdapter(new ContactsListAdapter(c));

            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {

                // TODO do I need to do anything here?
            }
        });
        mContactListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChatActivity.class));
            }
        });
    }


}
