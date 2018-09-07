package com.activity.loganle.uome.AddingContact;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.activity.loganle.uome.other.Contact;
import com.activity.loganle.uome.ContactList.ContactListActivity;
import com.activity.loganle.uome.R;
import com.activity.loganle.uome.Tools.CircleImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.activity.loganle.uome.Data.DataSource;

import static android.app.Activity.RESULT_OK;

public class AddingContactFragment extends Fragment {
    private DataSource dataSource;
    private static final int SELECTED_PIC = 1001;
    public Activity activity;
    private OnFragmentInteractionListener mListener;
    private Contact newContact;
    private List<Contact> contacts;
    private Bitmap bitmapImageProfile;

    public AddingContactFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new DataSource(getActivity());
        newContact = new Contact();
        dataSource.open();
        //get all current contacts from database
        contacts = dataSource.getAllContactsFromDatabase();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adding_contact,container,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewAddingContact);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(v);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button addNewContact = (Button) view.findViewById(R.id.addContactButton);
        addNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setMessage("Save this contact ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //get all values from the user
                                getAllInContacInfo();
                                dataSource.createContact(newContact);
                                Toast.makeText(activity, "Add contact successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activity,ContactListActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }
                        ).show();
            }
        });

    }

    public void openGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECTED_PIC);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECTED_PIC:
                if (resultCode == RESULT_OK) {
                    bitmapImageProfile = getBitMapImage(data);
                    final ImageView imageView = (ImageView) activity.findViewById(R.id.imageViewAddingContact);
                    displayTempImage(bitmapImageProfile, imageView);
                }
                break;
        }
    }
    public void displayTempImage(Bitmap bitmap, ImageView imageView) {

        try {
            imageView.setImageBitmap(new CircleImage(activity).transform(bitmap));
        } catch (Exception e) {
        }
    }
    public String saveImageToInternalStorage(Bitmap bitmapImage) {
        String fileSavingImages = newContact.getName() + contacts.size();
        File imageFile = new File(activity.getFilesDir(), fileSavingImages);
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(imageFile);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageFile.getAbsolutePath();
    }

    public void getAllInContacInfo(){
        //get name from edit text
        EditText contactName = (EditText) activity.findViewById(R.id.nameAddingContact);
        String contactNameString = contactName.getText().toString();
        newContact.setName(contactNameString);
        //get image from gallery
        String imagePath = saveImageToInternalStorage(bitmapImageProfile);
        newContact.setPersonalpic(imagePath);
        EditText addressText = (EditText) activity.findViewById(R.id.addressValue);
        newContact.setAddress(addressText.getText().toString());
        EditText phoneText = (EditText) activity.findViewById(R.id.phoneValue);
        newContact.setPhone(phoneText.getText().toString());
        EditText owedMoney = (EditText) activity.findViewById(R.id.owedMoneyValue);
        newContact.setOwedMoney(Double.parseDouble(owedMoney.getText().toString()));
        EditText deadlineText = (EditText) activity.findViewById(R.id.deadlineValue);
        newContact.setDeadlineDate(deadlineText.getText().toString());
        EditText emailText = (EditText) activity.findViewById(R.id.emailValueEditText);
        newContact.setEmail(emailText.getText().toString());
    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public Bitmap getBitMapImage(Intent data) {
        Uri uri = data.getData();
        InputStream iStream = null;
        try {
            iStream = activity.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] blob = new byte[0];
        try {
            blob = getBytes(iStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(blob, 0, blob.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options,200,150);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(blob,0,blob.length,options);

        return decodedBitmap;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
