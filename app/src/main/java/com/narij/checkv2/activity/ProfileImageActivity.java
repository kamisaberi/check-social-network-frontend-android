package com.narij.checkv2.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileImageActivity extends AppCompatActivity {

    APIInterface apiInterface;

    String phone;
    Button btnTakePhoto;
    Button btnFromGalley;

    ImageView imgProfile;

//    public final int RC_CAMERA_AND_LOCATION = 10001;

    private static final int RC_EXTERNAL_STORAGE_PERM = 124;

    private boolean hasReadExternalStoragePermissions() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);

        //Log.d(Globals.LOG_TAG, "PHOTO 1");
        apiInterface = APIClient.getClient().create(APIInterface.class);
        //Log.d(Globals.LOG_TAG, "PHOTO 2");
        imgProfile = (ImageView) findViewById(R.id.imgProfile);

        Button btnSend = findViewById(R.id.btnSend);

//        phone = getIntent().getStringExtra("phone");
        final ProgressBar prgLoading = (ProgressBar) findViewById(R.id.prgLoading);


        btnFromGalley = (Button) findViewById(R.id.btnFromGallery);

        //Log.d(Globals.LOG_TAG, phone);


        btnFromGalley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                readGallery();

            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prgLoading.setVisibility(View.VISIBLE);
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

                        try {
                            WebServiceMessage message = response.body();
                            if (!message.isError()) {
                                Log.d(Globals.LOG_TAG, message.getMessage());
                                Globals.loggedInUser.setAvatar(message.getMessage());
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {

                            }
                        } catch (Exception e) {
                            Log.d(Globals.LOG_TAG, e.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(Call<WebServiceMessage> call, Throwable t) {

                    }
                });


            }
        });

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (resultCode == RESULT_OK && requestCode == 1001) {
            Uri uri = data.getData();
            if (EasyPermissions.hasPermissions(this, perms)) {
                String filePath = getRealPathFromURIPath(uri, ProfileImageActivity.this);

                Globals.selectedProfilePhotoToUpload = new File(filePath);

                try {
                    InputStream imageStream = getContentResolver().openInputStream(uri);

                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imgProfile.setImageBitmap(selectedImage);
                    Picasso.get().load(Globals.selectedProfilePhotoToUpload).transform(new CircleTransform()).into(imgProfile);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                EasyPermissions.requestPermissions(this, "Permission ?",
                        RC_EXTERNAL_STORAGE_PERM, perms);
//                EasyPermissions.requestPermissions( Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        }
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
