package com.activity.loganle.uome.ContactDetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.loganle.uome.other.Contact;
import com.activity.loganle.uome.R;
import com.activity.loganle.uome.Tools.CircleImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.activity.loganle.uome.Data.DataSource;

public class ContactDetailFragment extends Fragment {
    //For testing
    public static final String PROFILEPIC = "PROFILE_PIC";
    public static final String ID_CONTACT = "ID_CONTACT";
    private static final String EMAILTAG = "EMAILTAG";
    //---End testing
    private int imageHeight, imageWidth;
    private String imageType;
    private static String LOGTAG = "LOGTAG";
    private DataSource dataSource;
    public static final String ContactBundle = "Contact_Bundle";
    private Uri uri;
    public static final String imageFileName = "imageFileName";
    public Activity activity;
    Contact contact;

    private static int phonenum = 0;

    public ContactDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new DataSource(activity);
        //1. Get the bundle object passed from ContactListActivity using getArguments object
        Bundle b = getArguments();
        //Check whether the bundle object is not null and check if it is a correct object
        if (b != null) {
            contact = new Contact(b);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        // Inflate the layout for this fragment
        if (contact != null) {
//            Toast.makeText(activity,"itemId selected: "+ contact.getIdContact(),Toast.LENGTH_SHORT).show();
            //Open gallery by tapping to image view and values of detail layout( Hanah's work)
            TextView nameText = (TextView) view.findViewById(R.id.nameText);
            nameText.setText(contact.getName());

            TextView addressText = (TextView) view.findViewById(R.id.addressValue);
            addressText.setText(contact.getAddress());

            TextView phoneText = (TextView) view.findViewById(R.id.phoneText);
            phoneText.setText(contact.getPhone());

            TextView emailText = (TextView) view.findViewById(R.id.Email_Text_value);
            emailText.setText(contact.getEmail());

            TextView owedMoneyText = (TextView) view.findViewById(R.id.owedMoneyText);
            owedMoneyText.setText(String.valueOf(contact.getOwedMoney()));

            TextView deadlineTextView = (TextView) view.findViewById(R.id.deadlineTextView);
            deadlineTextView.setText(contact.getDeadlineDate());

        }

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (contact != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.ImageViewDetail);
            try {
                String imagePath = contact.getImagePath();
                loadImageFromStorage(imagePath, imageView);
            } catch (FileNotFoundException e) {

            }
        }
        //Email floating button
        FloatingActionButton floatingActionButtonEmail = (FloatingActionButton) view.findViewById(R.id.mailFloatingButton);
        floatingActionButtonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailToOwner();
            }
        });
        //Phone floating button
        FloatingActionButton floatingActionButtonPhonecall = (FloatingActionButton) view.findViewById(R.id.phoneFloatingButton);
        floatingActionButtonPhonecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOwner();
            }
        });
    }

    private void loadImageFromStorage(String path, ImageView imageView) throws FileNotFoundException {
        try {
            File f = new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            CircleImage circleIamge = new CircleImage(activity);
            imageView.setImageBitmap(circleIamge.transform(b));
        } catch (Exception e) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        dataSource.close();
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        // the reference to the activity its working on
        activity = (Activity) context;
    }

    public void sendEmailToOwner() {
        Log.i(EMAILTAG, "call email method successfully");
        String[] addreses = {contact.getEmail()};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto: "));
        intent.putExtra(intent.EXTRA_EMAIL, addreses);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
        else {
            Toast.makeText(getActivity(),"No email applications found",Toast.LENGTH_SHORT).show();
        }
    }

    public void callOwner() {
//        String phoncall = contact.getPhone();
//        Intent intent = new Intent(Intent.ACTION_CALL);
//        intent.setData(Uri.parse(phoncall));
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+contact.getPhone()));
        startActivity(callIntent);
        if (callIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(callIntent);
        }
        else {
            Toast.makeText(getActivity(),"No phone applications found",Toast.LENGTH_SHORT).show();
        }
    }

    //Methods to reduce image size


}





