package com.coderschool.sosvn.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coderschool.sosvn.R;
import com.coderschool.sosvn.object.Hospital;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 03.08.2017.
 */

public class HospitalDetails extends AppCompatActivity {

    private Hospital mHospital;
    private String mDistance;

    @BindView(R.id.iv_hospital)
    ImageView ivHospital;
    @BindView(R.id.hospital_title)
    TextView hospitalTitle;
    @BindView(R.id.hospital_distance)
    TextView hospitalDistance;
    @BindView(R.id.hospital_rating)
    RatingBar hospitalRating;
    @BindView(R.id.hospital_address)
    TextView hospitalAddress;
    @BindView(R.id.hospital_phone)
    TextView hospitalPhone;
    @BindView(R.id.hospital_email)
    TextView hospitalEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_details);
        ButterKnife.bind(this);

        setupData();
    }

    private void setupData() {

        mHospital = getIntent().getExtras().getParcelable("hospital");
        mDistance = getIntent().getExtras().getString("distance");

        if (mHospital != null) {
            Glide.with(this)
                    .load(mHospital.getUrl())
                    .centerCrop()
                    .placeholder(R.drawable.p4)
                    .into(ivHospital);
            hospitalTitle.setText(mHospital.getName());
            hospitalRating.setRating(mHospital.getRating());
            hospitalDistance.setText(mDistance);
            hospitalAddress.setText(mHospital.getAddress());
            hospitalPhone.setText(mHospital.getPhone());
            hospitalEmail.setText(mHospital.getEmail());
        }
    }

    @OnClick({R.id.btn_call})
    public void call() {
        startActivity(new Intent(this, ReportActivity.class));
    }

    @OnClick({R.id.btn_directions})
    public void getDirections() {

        Hospital hospital = mHospital;
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + String.valueOf(hospital.getLatitude())
                + "," + String.valueOf(hospital.getLongitude()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }


}

