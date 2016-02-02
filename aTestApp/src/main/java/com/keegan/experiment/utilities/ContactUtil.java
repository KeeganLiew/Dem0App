package com.keegan.experiment.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by keegan on 27/01/16.
 */
public class ContactUtil extends Activity {

    private final static String TAG = ContactUtil.class.getSimpleName();

    public static String[] searchForContactNumber(Intent data, Context mContext) {
        Cursor cursor = null;
        String phoneNumber = "";
        String name = "";
        try {
            Uri contactData = data.getData();
            Cursor c = mContext.getContentResolver().query(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                Log.d(TAG, "NAME: " + name);
                Log.d(TAG, "id: " + id);

                if (Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    while (phones.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.d(TAG, "Number: " + phoneNumber);
                    }
                    phones.close();
                }
            }
            if (phoneNumber.length() == 0) {
                Toast.makeText(mContext, "No phone number found for contact.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "Failed to get contact data.", Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d(TAG, "Returning: " + name + " and " + phoneNumber);
        return new String[]{name, phoneNumber};
    }
}
