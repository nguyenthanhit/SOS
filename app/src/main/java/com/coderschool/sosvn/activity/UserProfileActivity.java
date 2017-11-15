package com.coderschool.sosvn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.coderschool.sosvn.R;
import com.coderschool.sosvn.fragment.UserProfileFragment;

import butterknife.ButterKnife;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_user_profile, new UserProfileFragment());
        ft.commit();
        //end
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Phong4", "data: " + data);
    }
}