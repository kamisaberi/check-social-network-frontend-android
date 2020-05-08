package com.narij.checkv2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.narij.checkv2.R;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.WebServiceMessage;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyAccountActivity extends AppCompatActivity {

    APIInterface apiInterface;
    EditText edtVerificationCode;
    Button btnConfirm;
    String phone = "";
    TextView txtMessage;


//    CountryCodePicker ccp;
//    EditText editTextCarrierNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);


        if (getIntent() != null) {
            phone = getIntent().getStringExtra("phone");
        }
        if (Globals.DEBUG_MODE == true) {
            phone = "+989365982333";
        }

        edtVerificationCode = findViewById(R.id.edtVerificationCode);
        btnConfirm = findViewById(R.id.btnConfirm);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        txtMessage = findViewById(R.id.txtMessage);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txtMessage.setText("");
                Call<WebServiceMessage> call = apiInterface.verify(phone, edtVerificationCode.getText().toString(), System.currentTimeMillis());
                call.enqueue(new Callback<WebServiceMessage>() {
                    @Override
                    public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {

                        WebServiceMessage webServiceMessage = response.body();
                        if (webServiceMessage.isError() == false) {
                            Intent intent = new Intent(VerifyAccountActivity.this, SplashActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), webServiceMessage.getMessage(), Snackbar.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<WebServiceMessage> call, Throwable t) {
                    }
                });

            }
        });


//        ccp = (CountryCodePicker) findViewById(R.id.ccp);
//        editTextCarrierNumber = (EditText) findViewById(R.id.editText_carrierNumber);
//
//        ccp.registerCarrierNumberEditText(editTextCarrierNumber);
//
//        ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
//            @Override
//            public void onValidityChanged(boolean isValidNumber) {
//                // your code
//            }
//        });
//
//        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(VerifyAccountActivity.this, ccp.getFullNumberWithPlus(), Toast.LENGTH_LONG).show();
//            }
//        });
    }
}
