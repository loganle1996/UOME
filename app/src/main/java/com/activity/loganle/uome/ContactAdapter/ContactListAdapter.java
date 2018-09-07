package com.activity.loganle.uome.ContactAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.loganle.uome.other.Contact;
import com.activity.loganle.uome.R;
import com.activity.loganle.uome.Tools.CircleImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LoganLe on 11/2/16.
 */

public class ContactListAdapter extends ArrayAdapter<Contact> implements Filterable {
    private static String LOGTAG = "LOGTAG";
    private Context context;
    private List<Contact> contactList, origData;
    private Boolean isEditModeEnabled = false;
    private CheckBox checkBox;
    private ArrayList<Contact> contactsEdit;
    //This is for storing state check purpose
    ArrayList<Boolean> positionArray;


    //Constructor
    public ContactListAdapter(Context context, int resource, List<Contact> contacts) {
        super(context, resource, contacts);
        this.context = context;
        this.contactList = contacts;
        origData = new ArrayList<>(contactList);
        // THis is created for edit purpose only
        contactsEdit = new ArrayList<>();
        positionArray = new ArrayList<Boolean>(contactList.size());
        for(int i =0;i<contactList.size();i++){
            positionArray.add(false);
        }
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //get the contact item that the user is selecting
        final Contact currentContact = contactList.get(position);
        Holder holder = null;
        if (convertView == null) {
            // Create inflater object
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            holder = new Holder();
            convertView = inflater.inflate(R.layout.row_layout, null);
            holder.ckbox =(CheckBox)convertView.findViewById(R.id.checkBoxListItem);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
            holder.ckbox.setOnCheckedChangeListener(null);
        }
        //Display images and values
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ImageViewRow);
        String imagePath = currentContact.getImagePath();
        try {
            if (imagePath != null) {
                displayImageFromStorage(imageView, imagePath);
            }
        } catch (FileNotFoundException e) {
        }
        //Display values
        TextView nameText = (TextView) convertView.findViewById(R.id.Name_Value_TexView);
        nameText.setText(currentContact.getName());

        TextView deadlineText = (TextView) convertView.findViewById(R.id.Deadline_value_TextView);
        deadlineText.setText(currentContact.getDeadlineDate());

        TextView owedMoney = (TextView) convertView.findViewById(R.id.OwnedMoney_ValueTextView);
        owedMoney.setText(("$" + String.valueOf(currentContact.getOwedMoney())));
        //Check box
        holder.ckbox.setFocusable(false);
        holder.ckbox.setChecked(positionArray.get(position));
        holder.ckbox.setVisibility(isEditModeEnabled ? View.VISIBLE : View.GONE);

        holder.ckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    contactsEdit.add(currentContact);
                    positionArray.set(position, true);

                } else {
                    contactsEdit.remove(currentContact);
                    positionArray.set(position, false);

                }
            }
        });
        return convertView;
    }

    public void displayImageFromStorage(ImageView imageView, String imagePath) throws FileNotFoundException {
        File f = new File(imagePath);
        FileInputStream fileInputStream = new FileInputStream(f);
        Bitmap b = BitmapFactory.decodeStream(fileInputStream);
        if (b != null) { // if the image stored in text file is not null
            imageView.setImageBitmap(new CircleImage(context).transform(b));
        } else { // if it is null, display the default image
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.userprofile);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            imageView.setImageBitmap(new CircleImage(context).transform(bitmap));
        }
    }

    public Boolean getEditModeEnabled() {
        return isEditModeEnabled;
    }

    public void setEditModeEnabled(Boolean editModeEnabled) {
        isEditModeEnabled = editModeEnabled;
        notifyDataSetChanged();
    }

    public List<Contact> getOrigData() {
        return origData;
    }

    public void setOrigData(List<Contact> origData) {
        this.origData = origData;
    }

    public ArrayList<Contact> getContactsEdit() {
        return contactsEdit;
    }

    public void setContactsEdit(ArrayList<Contact> contactsEdit) {
        this.contactsEdit = contactsEdit;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();

                if (constraint != null && constraint.toString().length() > 0) {
                    List<Contact> founded = new ArrayList<Contact>();
                    for (Contact contact : origData) {
                        if (contact.getName().toLowerCase().contains(constraint)) {
                            founded.add(contact);
                        }
                    }

                    result.values = founded;
                    result.count = founded.size();
                } else {
                    result.values = origData;
                    result.count = origData.size();
                }
                return result;


            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                for (Contact contact : (List<Contact>) results.values) {
                    add(contact);
                }
                notifyDataSetChanged();

            }

        };
    }

    public static class Holder{
        CheckBox ckbox;
    }

}
