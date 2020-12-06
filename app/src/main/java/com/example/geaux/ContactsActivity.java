package com.example.geaux;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    private ListView contactsListView;
    ArrayAdapter<String> contactsAdapter = null;
    private String currentPhotoPath = "";
    public static Uri uri;
    private ArrayList<String> contacts = new ArrayList<String>();
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        System.out.println("ME AND DON C");
        setupContactsAdapter();
        getContacts();
    }

    @Override
    public void onResume() {
        super.onResume();
        //setupContactsAdapter();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                getContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void getContacts() {
        int limit = 1000;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        else {
            Cursor cursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext() && limit > 0) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String given = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                //Add contacts and their id's to the list to be adapted by the array
                String element = given + " :: " + id;
                contacts.add(element);
            }
            cursor.close();
            setupContactsAdapter();
        }
    }

    private void setupContactsAdapter() {
        //Put the list of contacts into a listview to be displayed to the user
        contactsListView =
                (ListView)findViewById(R.id.contact_list_view);
        contactsAdapter = new
                ArrayAdapter<String>(this, R.layout.contact_row,
                R.id.contact_row_text_view, contacts);
        contactsListView.setAdapter(contactsAdapter);
    }

    public void onContactClick(View view){
        String contactId = (((TextView)view).getText().toString()).split(" :: ")[1];
        String emailAddress = "";
        Cursor emails = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
        if (emails.moveToNext()) {
            String email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
            emailAddress = email;
        }
        emails.close();


        //Set up email intent
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("application/octet-stream");
        //Ad the contact's email address as an extra
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { emailAddress });
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        //emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //Allow the image to be an attachment
        emailIntent.setType("image/png");

        //Grant permissions
        /*List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            this.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }*/

        //Start email activity
        //startActivity(Intent.createChooser(emailIntent, "Send mail using..."));

        Intent chooser = Intent.createChooser(emailIntent, "Share File");

        List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            this.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        startActivity(chooser);
    }

    private File createImageFile() throws Exception {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}