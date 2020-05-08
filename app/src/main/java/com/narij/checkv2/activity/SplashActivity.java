package com.narij.checkv2.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.narij.checkv2.R;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.model.ContactModel;
import com.narij.checkv2.service.TimeService;
import com.narij.checkv2.util.ContactUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends AppCompatActivity {

    private File file;

    RadioGroup rdg;
    RadioButton rdbLocal;
    RadioButton rdbGlobal;

    ImageButton btnAgain;

    private static final String[] LOCATION_AND_CONTACTS = {
            Manifest.permission.READ_CONTACTS
    };


    private static final int RC_CONTACTS_PERM = 124;

    private boolean hasContactsPermissions() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



/*
        Intent broadcastIntent = new Intent(this, AlarmBroadcastReceiver.class);
        // The Pending Intent to pass in AlarmManager
        PendingIntent pIntent = PendingIntent.getBroadcast(
                this,
                0,
                broadcastIntent,
                0
        );
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
*/



/*
        alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pIntent);
        // show a message that the alarm was set
        Toast.makeText(this, "Alarm was set, please wait.", Toast.LENGTH_LONG).show();
*/



/*
    startService(new Intent(this, TimeService.class));

*/



        File extDir = getExternalFilesDir(null);
        file = new File(extDir, Globals.contactListFile);

        rdg = findViewById(R.id.rdg);
        rdbLocal = findViewById(R.id.rdbLocal);
        rdbGlobal = findViewById(R.id.rdbGlobal);

//        if (Globals.DEBUG_MODE == true) {
//            rdg.setVisibility(View.VISIBLE);
//        }
        rdbLocal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    Globals.BASE_URL = "http://192.168.1.200/laravel-check/public/";
                    Globals.PROFILE_URL = Globals.BASE_URL + "uploads/";
                }
            }
        });

        rdbGlobal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    Globals.BASE_URL = "http://check.narij.ir/public/";
                    Globals.PROFILE_URL = Globals.BASE_URL + "uploads/";
                }
            }
        });

        btnAgain = findViewById(R.id.btnAgain);
        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (haveNetworkConnection() == true) {
                    btnAgain.setVisibility(View.GONE);
                    reload();
                } else {
                    btnAgain.setVisibility(View.VISIBLE);
                    Toast.makeText(SplashActivity.this, "network problem", Toast.LENGTH_SHORT).show();
                }


            }
        });

//        Log.d(Globals.LOG_TAG, "ABS PTH :" + extDir.getAbsolutePath());
//        EasyPermissions.getInstance().requestPermissions(this, this);

//        ((TextView) findViewById(R.id.txtLogo)).setTypeface(Globals.boldRoboto);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    public void reload() {
        btnAgain.setVisibility(View.GONE);


        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            int a;
            while ((a = bis.read()) != -1) {
                sb.append((char) a);
            }
            bis.close();
            fis.close();
            Type listType = new TypeToken<HashSet<ContactModel>>() {
            }.getType();
            Log.d(Globals.LOG_TAG, "size :" + sb.toString().getBytes().length + "");
            Globals.contacts = new Gson().fromJson(sb.toString(), listType);


        } catch (Exception e) {
            Log.d(Globals.LOG_TAG, e.getMessage());
        } finally {
            readContacts();
        }
        Log.d(Globals.LOG_TAG, "contact list size : " + Globals.contacts.size());


    }

    @Override
    protected void onStart() {
        super.onStart();

//        if (file.exists())
//            file.delete();

        if (haveNetworkConnection() == true) {
            btnAgain.setVisibility(View.GONE);
            reload();
        } else {
            btnAgain.setVisibility(View.VISIBLE);
            Toast.makeText(this, "network problem", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @AfterPermissionGranted(RC_CONTACTS_PERM)
    public void readContacts() {
        if (hasContactsPermissions()) {
            new getContactsAsyncTask().execute();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_contacts),
                    RC_CONTACTS_PERM,
                    Manifest.permission.READ_CONTACTS);
        }
    }

    public Set<ContactModel> getContacts(Context ctx) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Globals.SHARED_PREF, 0);
        long last_contact_update = pref.getLong(Globals.PREF_LAST_CONTACT_UPDATE_KEY, 1000);

        long current_contact_upade = System.currentTimeMillis();
//        Log.d(Globals.LOG_TAG, "last update :" + last_contact_update);
//        if (Globals.DEBUG_MODE == true) {
//            last_contact_update = 0;
//        }
        String filter = ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP + " > " + last_contact_update;
        Log.d(Globals.LOG_TAG, "filter :" + filter);
        List<ContactModel> list = new ArrayList<>();
        try {
            ContentResolver contentResolver = ctx.getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, filter, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                        Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(),
                                ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
//                        Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

//                        Bitmap photo = null;
//                        if (inputStream != null) {
//                            photo = BitmapFactory.decodeStream(inputStream);
//                        }
                        while (cursorInfo.moveToNext()) {
                            ContactModel info = new ContactModel();
                            info.id = id;
//                            info.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            String mn = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


//                            if (mn.equals("null") == true)
//                                continue;
//                            if (mn == null)
//                                continue;
//
//                            if (mn.startsWith("+") == true) {
//                                info.mobileNumber = mn;
//                            } else if (mn.startsWith("00") == true) {
//                                mn = "+" + mn.substring(2);
//                                info.mobileNumber = mn;
//                            } else if (mn.startsWith("00") == false && mn.startsWith("0") == true) {
//                                mn = "+98" + mn.substring(1);
//                                info.mobileNumber = mn;
//                            } else {
////                                info.mobileNumber = mn;
//                                continue;
//                            }
//
//                            mn = mn.replaceAll("\\s+", "");
//
//
//                            info.mobileNumber = mn;


                            String phone = ContactUtils.refinePhoneNumber(mn);
                            if (phone != null)
                                info.mobileNumber = phone;
                            else
                                continue;

//                            Log.d(Globals.LOG_TAG, "P : " + info.mobileNumber);
//                            info.photo = photo;
//                            info.photoURI = pURI.toString();
                            list.add(info);
                        }
                        cursorInfo.close();
                    }
                }
                cursor.close();
//                Globals.contacts = list;
            }
        } catch (Exception e) {
            Log.d(Globals.LOG_TAG, e.getMessage());
        }

        Set<ContactModel> uniques = new HashSet<>(list);

        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(Globals.PREF_LAST_CONTACT_UPDATE_KEY, current_contact_upade);
        editor.commit();


//        Log.d(Globals.LOG_TAG, "S L :::" + list.size());
//        Log.d(Globals.LOG_TAG, "S L :::" + uniques.size());

        return uniques;
    }


    private class getContactsAsyncTask extends AsyncTask<Void, Integer, Set<ContactModel>> {

        //List<ContactModel> contacts;


        @Override
        protected Set<ContactModel> doInBackground(Void... voids) {
            return getContacts(getApplicationContext());
        }

        @Override
        protected void onPostExecute(Set<ContactModel> contactModels) {
            super.onPostExecute(contactModels);

            Globals.contacts.addAll(contactModels);
            String str = new Gson().toJson(Globals.contacts);
//                Log.d(Globals.LOG_TAG, str);
//                JSONObject jsonObject = new JSONObject(str);
            Log.d(Globals.LOG_TAG, "contact size before:" + Globals.contacts.size());

            try {

//                if (file.exists())
//                    file.delete();

                FileOutputStream fos = new FileOutputStream(file, false);
//                BufferedOutputStream bos = new BufferedOutputStream(fos);
//                Log.d(Globals.LOG_TAG, "file size :" + str.getBytes().length);
                fos.write(str.getBytes());
                fos.close();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, Globals.SPLASH_TIME_OUT);

//                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();


            } catch (FileNotFoundException e) {
                Log.d(Globals.LOG_TAG, e.getMessage());
            } catch (IOException e) {
                Log.d(Globals.LOG_TAG, e.getMessage());
            }

/*
            StringBuffer sb = new StringBuffer();
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                int a;
                while ((a = bis.read()) != -1) {
                    sb.append((char) a);
                }
                bis.close();
                fis.close();
                Type listType = new TypeToken<ArrayList<ContactModel>>() {
                }.getType();
                Globals.contacts = new Gson().fromJson(sb.toString(), listType);
                Log.d(Globals.LOG_TAG, "contact size after:" + Globals.contacts.size());
            } catch (FileNotFoundException e) {
                Log.d(Globals.LOG_TAG, e.getMessage());
            } catch (IOException e) {
                Log.d(Globals.LOG_TAG, e.getMessage());
            } catch (Exception e) {
                Log.d(Globals.LOG_TAG, e.getMessage());
            }

*/

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}
