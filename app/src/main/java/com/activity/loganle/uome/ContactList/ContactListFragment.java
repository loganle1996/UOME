package com.activity.loganle.uome.ContactList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.activity.loganle.uome.other.Contact;
import com.activity.loganle.uome.ContactAdapter.ContactListAdapter;
import com.activity.loganle.uome.R;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import com.activity.loganle.uome.Data.DataSource;

/**
 * Created by LoganLe on 11/2/16.
 */

public class ContactListFragment extends ListFragment {
    private static String LOGTAG = "LOGTAG";
    List<Contact> contacts;
    //Create a reference to the ContactListActivity
    private CallBacks contactListActivity;
    private DataSource dataSource;
    private ContactListAdapter contactListAdapter;
    private EditText searchView;
    private Boolean editmode = false;
    private CheckBox checkBoxItem;
    //This is to for avoiding concurrentmodification expcetion.
    List<Contact> contactsCopy;

    //No argument constructor required
    public ContactListFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new DataSource(getActivity());
        Log.i(LOGTAG, "DATASOURCE has been created");

        // make sure the database is open, you can open it as many times as you want.
        dataSource.open();
        //get all contacts from database
        contacts = dataSource.getAllContactsFromDatabase();
        //create default database
        if(contacts.size() == 0){
            createData();
            contacts = dataSource.getAllContactsFromDatabase();
        }
        //set list ContactAdapter here
        contactListAdapter = new ContactListAdapter(getActivity(), R.layout.row_layout, contacts);
        // Get reference to the list view which displays all the items
        setListAdapter(contactListAdapter);
    }

    public void createData() {
        Contact contact = new Contact("Denis", "RMIT vietnam", "0902401566", "denis@gmail.com", 2000, "20/10/2017");
        dataSource.createContact(contact);
        contact = new Contact("Hanah", "RMIT vietnam", "0902401566", "hanah@gmail.com", 100, "20/10/2017");
        dataSource.createContact(contact);
        contact = new Contact("Hai", "RMIT vietnam", "012321378", "hai@gmail.com", 100, "12/3/2018");
        dataSource.createContact(contact);
        contact = new Contact("Dat", "RMIT vietnam", "123213321", "Dat@gmail.com", 4400, "12/06/2017");
        dataSource.createContact(contact);
        contact = new Contact("Minh", "RMIT vietnam", "012321378", "Minh@gmail.com", 200, "12/3/2017");
        dataSource.createContact(contact);
        contact = new Contact("Vu", "RMIT vietnam", "012321378", "Vu@gmail.com", 400, "12/3/2017");
        dataSource.createContact(contact);
        contact = new Contact("Khoi", "RMIT vietnam", "012321378", "khoi@gmail.com", 8000, "12/3/2017");
        dataSource.createContact(contact);

    }

    @Override
    public void onResume() {
        super.onResume();
        //Create a connection to the database
        dataSource.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_contact_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        searchView = (EditText) view.findViewById(R.id.searchViewContact);
        //Enabling search filter
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final Button deleteButton = (Button) view.findViewById(R.id.deleteListItems);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contactListAdapter.getContactsEdit().size() > 0) { // if there are items to delete
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Are you sure you want to delete contacts ?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    contactsCopy = new LinkedList<Contact>(contactListAdapter.getContactsEdit());// make a copy of orginal list(contactListAdapter.getContactsEdit()).
                                    for (Contact contact : contactsCopy) {
                                        contactListAdapter.getContactList().remove(contact);
                                        contactListAdapter.getOrigData().remove(contact);
                                        contactListAdapter.getContactsEdit().clear();
                                        dataSource.deleteContact(contact);
                                    }
                                    Toast.makeText(getActivity(), "Delete contact(s) successfully", Toast.LENGTH_SHORT).show();
                                    contactListAdapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }
                            ).show();
                }
                else {
                    Toast.makeText(getActivity(), "Nothing to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Edit mode floating button
        FloatingActionButton editButton = (FloatingActionButton) view.findViewById(R.id.fabEditList);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contactListAdapter.getEditModeEnabled() == true) {
                    deleteButton.setVisibility(View.GONE);
                    contactListAdapter.setEditModeEnabled(false);
                    searchView.setVisibility(View.VISIBLE);
                } else {
                    deleteButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    deleteButton.setVisibility(View.VISIBLE);
                    contactListAdapter.setEditModeEnabled(true);
                    searchView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        //Close the database
        dataSource.close();
    }

    //Create an interface callbacks
    public interface CallBacks {
        //ContactListActivity must implement this abstract method
        void onItemSelected(Contact contact);
    }
    //

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Create a contact object from contacts list using the position parameter
        Contact contact = contacts.get(position);
        // Passing the contact object to the ContactListActivity activity
        contactListActivity.onItemSelected(contact);
    }

    //This method is automatically called when this fragment is added to the system
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            this.contactListActivity = (CallBacks) context; // get the reference to ContactListActivity
        } catch (Exception e) {
        }
    }

}
