package com.narij.checkv2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.narij.checkv2.R;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.library.CircleTransform;
import com.narij.checkv2.model.Group;
import com.narij.checkv2.model.User;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.RetrofitUser;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity {

    CircleImageView imgProfile;
    Button btnEditProfile;
    TextView txtName;
    TextView txtNumberOfTasks;
    TextView txtNumberOfFriends;
    TextView txtNumberOfJobs;


    APIInterface apiInterface;
    int user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        imgProfile = findViewById(R.id.imgProfile);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        txtName = findViewById(R.id.txtName);
        txtNumberOfTasks = findViewById(R.id.txtNumberOfTasks);
        txtNumberOfFriends = findViewById(R.id.txtNumberOfFriends);
        txtNumberOfJobs = findViewById(R.id.txtNumberOfJobs);

        if (getIntent() != null) {
            user = getIntent().getIntExtra("user", 0);
        }

        if (user == Globals.loggedInUser.getId()) {
            String p = Globals.PROFILE_URL + Globals.loggedInUser.getId() + "/" + Globals.loggedInUser.getAvatar();
            Log.d(Globals.LOG_TAG, p);
            Picasso.get().load(p).transform(new CircleTransform()).placeholder(R.drawable.profile_default).into(imgProfile);

            txtName.setText(Globals.loggedInUser.getName());
            txtNumberOfFriends.setText(Globals.friends.size() + "");
            txtNumberOfJobs.setText(Globals.groups.size() + "");

            int tasks = 0;
            for (Group g : Globals.groups) {
                tasks += g.getDuties().size();
            }

            txtNumberOfTasks.setText(tasks + "");

            btnEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AccountActivity.this, EditProfileActivity.class);
                    startActivityForResult(intent, 1001);
                }
            });
        } else {

            Call<RetrofitUser> call = apiInterface.getUser(user, System.currentTimeMillis());
            call.enqueue(new Callback<RetrofitUser>() {
                @Override
                public void onResponse(Call<RetrofitUser> call, Response<RetrofitUser> response) {
                    RetrofitUser retrofitUser = response.body();
                    User user = retrofitUser.getUser();
                    if (retrofitUser.isError() == false) {
                        if (user.getAvatar().trim().isEmpty() == false) {
                            String p = Globals.PROFILE_URL + user.getId() + "/" + user.getAvatar();
                            Picasso.get().load(p).transform(new CircleTransform()).placeholder(R.drawable.profile_default).into(imgProfile);
                        }
                        txtName.setText(user.getName());
                        btnEditProfile.setText("Follow");
                        btnEditProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<RetrofitUser> call, Throwable t) {

                }
            });


        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            txtName.setText(Globals.loggedInUser.getName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Globals.loggedInUser.getAvatar().isEmpty()) {
            Picasso.get().load(R.drawable.profile_default).transform(new CircleTransform()).into(imgProfile);
        } else {
            String p = Globals.PROFILE_URL + Globals.loggedInUser.getId() + "/" + Globals.loggedInUser.getAvatar();
            Picasso.get().load(p).transform(new CircleTransform()).placeholder(R.drawable.profile_default).into(imgProfile);
        }
        txtName.setText(Globals.loggedInUser.getName());

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//
//            case R.id.mnuExit:
//                Globals.loggedInUser = new User();
//                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//
//                break;
//
//
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }
}
