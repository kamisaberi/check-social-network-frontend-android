package com.narij.checkv2.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.narij.checkv2.R;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.library.CircleTransform;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.WebServiceMessage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {


    Toolbar toolbar;
    ProgressDialog progressDialog;
    APIInterface apiInterface;

    CircleImageView imgProfile;
    Button btnChangeImage;


    TextInputEditText edtName;
    TextInputEditText edtUsername;
    TextInputEditText edtEmail;

    private static final int RC_EXTERNAL_STORAGE_PERM = 124;

    private boolean hasReadExternalStoragePermissions() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    @AfterPermissionGranted(RC_EXTERNAL_STORAGE_PERM)
    public void readGallery() {
        if (hasReadExternalStoragePermissions()) {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
//                openGalleryIntent.setType("image/*");
            startActivityForResult(openGalleryIntent, 1001);
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_external_storage),
                    RC_EXTERNAL_STORAGE_PERM,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

//        final ProgressBar prgLoading = (ProgressBar) findViewById(R.id.prgLoading);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);

        imgProfile = findViewById(R.id.imgProfile);

        btnChangeImage = findViewById(R.id.btnChangeImage);

        edtName = findViewById(R.id.edtName);
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);

        edtName.setText(Globals.loggedInUser.getName());
        edtUsername.setText(Globals.loggedInUser.getUsername());
        edtEmail.setText(Globals.loggedInUser.getEmail());

        String p = Globals.PROFILE_URL + Globals.loggedInUser.getId() + "/" + Globals.loggedInUser.getAvatar();
        Log.d(Globals.LOG_TAG, p);
        Picasso.get().load(p).transform(new CircleTransform()).placeholder(R.drawable.profile_default).into(imgProfile);


        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(EditProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_change_profile_image);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button btnNewProfilePhoto = dialog.findViewById(R.id.btnNewProfilePhoto);
                Button btnRemoveProfilePhoto = dialog.findViewById(R.id.btnRemoveProfilePhoto);

                btnNewProfilePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        dialog.dismiss();
                        readGallery();

//                        Intent intent = new Intent(EditProfileActivity.this, ProfileImageActivity.class);
//                        startActivityForResult(intent, 1001);


                    }
                });

                btnRemoveProfilePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        progressDialog.show();
                        Call<WebServiceMessage> call = apiInterface.removeImage(Globals.loggedInUser.getId(), System.currentTimeMillis());
                        call.enqueue(new Callback<WebServiceMessage>() {
                            @Override
                            public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {
                                WebServiceMessage message = response.body();

                                if (message.isError() == false) {

                                    Globals.loggedInUser.setAvatar("");
                                    if (Globals.loggedInUser.getAvatar().isEmpty()) {
                                        Picasso.get().load(R.drawable.profile_default).transform(new CircleTransform()).into(imgProfile);
                                    } else {
                                        String p = Globals.PROFILE_URL + Globals.loggedInUser.getId() + "/" + Globals.loggedInUser.getAvatar();
                                        Picasso.get().load(p).transform(new CircleTransform()).placeholder(R.drawable.profile_default).into(imgProfile);

                                    }

                                }
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<WebServiceMessage> call, Throwable t) {
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();

                            }
                        });

                        dialog.dismiss();

                    }
                });


                dialog.show();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (resultCode == RESULT_OK && requestCode == 1001) {
            Uri uri = data.getData();
            if (EasyPermissions.hasPermissions(this, perms)) {


                String filePath = getRealPathFromURIPath(uri, EditProfileActivity.this);
                Globals.selectedProfilePhotoToUpload = new File(filePath);

                try {
                    InputStream imageStream = getContentResolver().openInputStream(uri);

                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imgProfile.setImageBitmap(selectedImage);
                    Picasso.get().load(Globals.selectedProfilePhotoToUpload).transform(new CircleTransform()).into(imgProfile);

                    progressDialog.show();
                    RequestBody mFile = RequestBody.create(okhttp3.MediaType.parse("image/*"), Globals.selectedProfilePhotoToUpload);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", Globals.selectedProfilePhotoToUpload.getName(), mFile);

                    Call<WebServiceMessage> call = apiInterface.setProfilePhoto(
                            Globals.loggedInUser.getId(),
                            fileToUpload,
                            System.currentTimeMillis()
                    );
                    call.enqueue(new Callback<WebServiceMessage>() {
                        @Override
                        public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            try {
                                WebServiceMessage message = response.body();
                                if (!message.isError()) {
                                    Log.d(Globals.LOG_TAG, message.getMessage());
                                    Globals.loggedInUser.setAvatar(message.getMessage());
                                    String p = Globals.PROFILE_URL + Globals.loggedInUser.getId() + "/" + Globals.loggedInUser.getAvatar();
                                    Picasso.get().load(p).transform(new CircleTransform()).placeholder(R.drawable.profile_default).into(imgProfile);

//                                    Intent intent = new Intent();
//                                    setResult(RESULT_OK, intent);
//                                    finish();
                                } else {

                                }
                            } catch (Exception e) {
                                Log.d(Globals.LOG_TAG, e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<WebServiceMessage> call, Throwable t) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            } else {
                EasyPermissions.requestPermissions(this, "Permission ?",
                        RC_EXTERNAL_STORAGE_PERM, perms);
//                EasyPermissions.requestPermissions( Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        }


//        if (resultCode == RESULT_OK && requestCode == 1001) {
//
//            String p = Globals.PROFILE_URL + Globals.loggedInUser.getId() + "/" + Globals.loggedInUser.getAvatar();
//            Log.d(Globals.LOG_TAG, p);
//            Picasso.get().load(p).transform(new CircleTransform()).placeholder(R.drawable.profile_default).into(imgProfile);
//
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_edit_profile_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mnuSend:

                progressDialog.show();

                Call<WebServiceMessage> call = apiInterface.editProfile(
                        Globals.loggedInUser.getId(),
                        edtName.getText().toString(),
                        edtUsername.getText().toString(),
                        edtEmail.getText().toString(),
                        System.currentTimeMillis()
                );

                call.enqueue(new Callback<WebServiceMessage>() {
                    @Override
                    public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {
                        WebServiceMessage message = response.body();
                        if (message.isError() == false) {

                            Globals.loggedInUser.setName(edtName.getText() + "");
                            Globals.loggedInUser.setUsername(edtUsername.getText() + "");
                            Globals.loggedInUser.setEmail(edtEmail.getText() + "");

                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<WebServiceMessage> call, Throwable t) {

                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });


                break;
            case android.R.id.home:

                onBackPressed();
                break;

        }

        return true;
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


}
