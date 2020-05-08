package com.narij.checkv2.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hbb20.CountryCodePicker;
import com.narij.checkv2.R;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.WebServiceMessage;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    //    EditText edtPhone;
    TextInputEditText edtName;
    TextInputEditText edtUsername;
    TextInputEditText edtEmail;
    TextInputEditText edtPassword;
    AppCompatTextView btnLogin;
    AppCompatButton btnRegister;

    APIInterface apiInterface;

    CountryCodePicker ccp;
    EditText editTextCarrierNumber;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dialog = new ProgressDialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog);
        dialog.setTitle("Registering...");
//        edtPhone = findViewById(R.id.edtPhone);
        edtName = findViewById(R.id.edtName);
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        editTextCarrierNumber = (EditText) findViewById(R.id.editText_carrierNumber);

        if (Globals.DEBUG_MODE == true) {
            edtName.setText("ali");
            edtUsername.setText("alireza1201");
            edtEmail.setText("kamisaberi@yahoo.com");
            edtPassword.setText("1234");
            edtPassword.setText("1234");
            editTextCarrierNumber.setText("9365982333");
        }

        apiInterface = APIClient.getClient().create(APIInterface.class);


        ccp.registerCarrierNumberEditText(editTextCarrierNumber);

        ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                // your code
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(Globals.LOG_TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        String token = Objects.requireNonNull(task.getResult()).getToken();

                        Call<WebServiceMessage> call = apiInterface.register(
                                edtName.getText().toString(),
                                edtUsername.getText().toString(),
                                ccp.getFullNumberWithPlus().toString(),
                                edtPassword.getText().toString(),
                                edtEmail.getText().toString(),
                                token,
                                System.currentTimeMillis()
                        );
                        call.enqueue(new Callback<WebServiceMessage>() {
                            @Override
                            public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {

                                WebServiceMessage webServiceMessage = response.body();
                                if (webServiceMessage.isError() == false) {

                                    Intent intent = new Intent(RegisterActivity.this, VerifyAccountActivity.class);
                                    intent.putExtra("phone", ccp.getFullNumberWithPlus().toString());
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Snackbar.make(findViewById(android.R.id.content), webServiceMessage.getMessage(), Snackbar.LENGTH_LONG).show();
                                }


                                if (dialog.isShowing())
                                    dialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<WebServiceMessage> call, Throwable t) {
                                if (dialog.isShowing())
                                    dialog.dismiss();

                            }
                        });


                    }
                });


            }
        });


    }
}
