package com.coderschool.sosvn.fragment.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coderschool.sosvn.R;
import com.coderschool.sosvn.activity.ReportActivity;
import com.coderschool.sosvn.object.Hospital;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 03.08.2017.
 */

public class HospitalDetailDialog extends DialogFragment {

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

    public static HospitalDetailDialog newInstance() {
        Bundle args = new Bundle();
        HospitalDetailDialog fragment = new HospitalDetailDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.hospital_details, container, false);
        ButterKnife.bind(this, view);
        setupData();
        return view;
    }


    private void setupData() {

        mHospital = getArguments().getParcelable("hospital");
        mDistance = getArguments().getString("distance");

        if (mHospital != null) {
            getDialog().setTitle(mHospital.getName());
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
        startActivity(new Intent(getActivity(), ReportActivity.class));
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

