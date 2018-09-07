package com.activity.loganle.uome.ContactList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.activity.loganle.uome.AddingContact.AddingContactActivity;
import com.activity.loganle.uome.other.Contact;
import com.activity.loganle.uome.ContactDetail.ContactDetailActivity;
import com.activity.loganle.uome.ContactDetail.ContactDetailFragment;
import com.activity.loganle.uome.R;

public class ContactListActivity extends AppCompatActivity implements ContactListFragment.CallBacks {
    private static String LOGTAG = "LOGTAG";
    public static final String ContactBundle = "Contact_Bundle";
    private static final int REQUEST_CODE = 1001;
    // Make it false by default
    private boolean isTwoPaneMode = false;
    private boolean isMainState = true;
    private boolean isAddingState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Do not display title
        getSupportActionBar().setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        //Check if whether it is in two pane mode or single pande mode (Logan's work).
        // Notice: ContactDetailActivity adds a fragment to the container by default
        if (findViewById(R.id.detailContainer) != null) { // Because the activity's layout of single pane mode does not contain detail container, so if this container exits, the layout is in two pane mode
            isTwoPaneMode = true;
        }
    }

    @Override
    public void onItemSelected(Contact contact) {
        Bundle bundle = contact.toBundle();
        //if this is in two pane mode
        if (isTwoPaneMode) {
            try {
                //Add ContactDetailFragment to detail container
                ContactDetailFragment contactDetailFragment = new ContactDetailFragment();
                // Send bundle object to this fragment
                contactDetailFragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.detailContainer, contactDetailFragment, "detailFragment")
                        .commit();
                isMainState = true;
            } catch (Exception e) {
            }
        } else {
            //Start detail activity
            Intent intent = new Intent(this, ContactDetailActivity.class);
            intent.putExtra(ContactBundle, bundle);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_contact) {
            if (isAddingState == false) {
                if (isTwoPaneMode == true) {
                    Intent intent = new Intent(this,AddingContactActivity.class);
                    startActivity(intent);

                } else { // if it is in single pande mode
                    Intent intent = new Intent(this,AddingContactActivity.class);
                    startActivity(intent);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
    // The user taps outside the the search view to lose focus
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_detail, menu);
        return true;
    }
}
