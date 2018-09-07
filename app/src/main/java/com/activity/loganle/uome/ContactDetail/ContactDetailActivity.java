package com.activity.loganle.uome.ContactDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.activity.loganle.uome.R;

/**
 * Created by LoganLe on 11/2/16.
 */

//THIS CLASS IS ONLY USED FOR SINGLE PANE MODE ON PHONES
public class ContactDetailActivity extends AppCompatActivity {
    public static final String ContactBundle = "Contact_Bundle";
    private static String LOGTAG = "LOGTAG";
    public static final String PROFILEPIC = "PROFILE_PIC";
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        Bundle bundle = getIntent().getBundleExtra(ContactBundle);
        // Add detail fragment here
        ContactDetailFragment contactDetailFragment = new ContactDetailFragment();
        contactDetailFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(R.id.detailContainer,contactDetailFragment)
                .commit();

    }

}
