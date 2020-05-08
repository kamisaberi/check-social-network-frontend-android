package com.narij.checkv2.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.narij.checkv2.R;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.firebase.app.Config;
import com.narij.checkv2.firebase.util.NotificationUtils;
import com.narij.checkv2.fragment.AccountFragment;
import com.narij.checkv2.fragment.AddFragment;
import com.narij.checkv2.fragment.ExpandableGroupsFragment;
import com.narij.checkv2.fragment.FriendsFragment;
import com.narij.checkv2.model.ContactModel;
import com.narij.checkv2.model.Duty;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.RetrofitGroups;
import com.narij.checkv2.retrofit.model.RetrofitRegisteredUsers;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    APIInterface apiInterface;
    Handler handler = new Handler();
//    FancyButton btnAddDuty;

    //    private ExpandableGroupsFragment mFragment;
    private Fragment mFragment;


    BottomNavigationView bottomNavigationView;


    private BroadcastReceiver mRegistrationBroadcastReceiver;


//    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    //    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission is granted
//                getContacts(MainActivity.this);
//            } else {
//                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        btnAddDuty = (FancyButton) findViewById(R.id.btnAddDuty);
        apiInterface = APIClient.getClient().create(APIInterface.class);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

//                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
//                    String payload= intent.getStringExtra("payload");
                    Toast.makeText(MainActivity.this, "Push notification: " + message, Toast.LENGTH_LONG).show();

//                    txtMessage.setText(message + "\n" + payload);
//                    Log.d(Globals.LOG_TAG, message);

//                    FileOutputStream fos = null;
//                    try {
//                        fos = new FileOutputStream(file);
//                        Log.d(Globals.LOG_TAG, "file size :" + message.getBytes().length);
//                        fos.write(message.getBytes());
//                        fos.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }


                }
            }
        };


        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                return;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentManager fragmentManager = getSupportFragmentManager();

                switch (menuItem.getItemId()) {
                    case R.id.mnuHome:

                        mFragment = ExpandableGroupsFragment.newInstance(3);
                        fragmentManager.beginTransaction().replace(R.id.recycler_view_container, mFragment).commit();
                        break;

                    case R.id.mnuSearch:
                        break;

                    case R.id.mnuAdd:

                        mFragment = new AddFragment();
                        fragmentManager.beginTransaction().replace(R.id.recycler_view_container, mFragment).commit();

                        break;

                    case R.id.mnuFriends:

                        mFragment = new FriendsFragment();
                        fragmentManager.beginTransaction().replace(R.id.recycler_view_container, mFragment).commit();

                        break;

                    case R.id.mnuAccount:

                        mFragment = new AccountFragment();
                        fragmentManager.beginTransaction().replace(R.id.recycler_view_container, mFragment).commit();
//                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_out, R.anim.fade_in).replace(R.id.recycler_view_container,
//                                mFragment).commit();
                        break;
                }
                return true;
            }
        });


        mFragment = ExpandableGroupsFragment.newInstance(3);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_out, R.anim.fade_in).replace(R.id.recycler_view_container, mFragment).commit();


        ArrayList<String> phones = new ArrayList<>();
        for (ContactModel cm : Globals.contacts) {
//            Log.d(Globals.LOG_TAG, cm.mobileNumber);
            phones.add(cm.mobileNumber);
        }

        String strphones = TextUtils.join(",", phones);
        //Log.d(Globals.LOG_TAG, strphones);

        try {
//        strphones = "+989112547896,+981245692322,+961442525252,+989304982248,+145236521412,+989111425011,+989113334886";
            Call<RetrofitRegisteredUsers> call = apiInterface.getRegisteredUsers(
                    Globals.loggedInUser.getId(),
                    strphones,
                    System.currentTimeMillis()
            );
            call.enqueue(new Callback<RetrofitRegisteredUsers>() {
                @Override
                public void onResponse(Call<RetrofitRegisteredUsers> call, Response<RetrofitRegisteredUsers> response) {
                    RetrofitRegisteredUsers retrofitRegisteredUsers = response.body();
                    if (retrofitRegisteredUsers.isError() == false) {
                        Globals.registeredUsers = retrofitRegisteredUsers.getUsers();
                        Log.d(Globals.LOG_TAG, "REGISTERED USERS : " + Globals.registeredUsers.size());
                    }
                }

                @Override
                public void onFailure(Call<RetrofitRegisteredUsers> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            Log.d(Globals.LOG_TAG, e.getMessage());
        }

        Call<RetrofitGroups> call1 = apiInterface.getGroups(Globals.loggedInUser.getId(), System.currentTimeMillis());
        call1.enqueue(new Callback<RetrofitGroups>() {
            @Override
            public void onResponse(Call<RetrofitGroups> call, Response<RetrofitGroups> response) {

                RetrofitGroups retrofitGroups = response.body();
                if (retrofitGroups.isError() == false) {
                    Globals.justGroups = retrofitGroups.getGroups();

                }
            }

            @Override
            public void onFailure(Call<RetrofitGroups> call, Throwable t) {

            }
        });

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
//            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
//        } else {
//            // Android version is lesser than 6.0 or the permission is already granted.
//            List<Co ntactModel> contacts = getContacts(MainActivity.this);
//            Log.d(Globals.LOG_TAG, "contact size :" + contacts.size());
//        }


//        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//        while (phones.moveToNext()) {
//            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            Log.d(Globals.LOG_TAG, name + " " + phoneNumber);
//            //Toast.makeText(getApplicationContext(),name, Toast.LENGTH_LONG).show();
//        }
//        phones.close();


//        btnAddDuty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, AddDutyBasicInfoActivity.class);
//                startActivityForResult(intent, 1001);
//            }
//        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, AddDutyBasicInfoActivity.class);
//                startActivityForResult(intent, 1001);
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            Duty duty = new Duty();
            duty.setId(Integer.parseInt(data.getStringExtra("id")));
            duty.setTitle(data.getStringExtra("name"));
            duty.setDescription(data.getStringExtra("description"));
            duty.setDuration(data.getLongExtra("duration", 0L));
            duty.setStartDate(data.getLongExtra("start_date", 0L));
            duty.setParent(Integer.parseInt(data.getStringExtra("parent")));

        }


    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

//        handler.removeCallbacks(runnable);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.mnuExit) {
//
//            Globals.loggedInUser = new User();
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_friends) {
            Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

}




