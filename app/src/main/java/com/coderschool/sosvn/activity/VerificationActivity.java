package com.coderschool.sosvn.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.coderschool.sosvn.R;
import com.coderschool.sosvn.fragment.OTPVerificationFragment;

import butterknife.ButterKnife;

public class VerificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        ButterKnife.bind(this);

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_verify_otp, new OTPVerificationFragment());
        ft.commit();
        //end
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}