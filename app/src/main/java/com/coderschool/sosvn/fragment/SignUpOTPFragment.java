package com.coderschool.sosvn.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.coderschool.sosvn.R;
import com.coderschool.sosvn.activity.SignUpOTPActivity;
import com.coderschool.sosvn.fragment.dialog.CountryFragment;
import com.coderschool.sosvn.manager.UserManager;
import com.coderschool.sosvn.object.Country;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class SignUpOTPFragment extends Fragment {

    private final int REQUEST_CODE = 20;
    @BindView(R.id.input_layout_country)
    TextInputLayout tilNationality;
    @BindView(R.id.input_layout_phone)
    TextInputLayout tilPhone;

    @BindView(R.id.ipt_country)
    EditText iptCountry;
    @BindView(R.id.ipt_phone)
    EditText iptPhoneNumber;
    AlertDialog dialog;
    Country country;



    UserManager userManager = UserManager.getInstance();
    private String mPhoneNumber;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    SignUpOTPActivity activity;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    Log.d("KKK", phoneAuthCredential.toString());
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Log.d("KKK", e.toString());
                }

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("ResendToken", forceResendingToken);
                    bundle.putString("VerificationId", s);
                    bundle.putString("phoneNumber", mPhoneNumber);

                    dialog.dismiss();
                    activity = (SignUpOTPActivity) getActivity();
                    activity.findViewById(R.id.parent_layout).setVisibility(View.GONE);
                    OTPVerificationFragment otpVerificationFragment = new OTPVerificationFragment();
                    otpVerificationFragment.setArguments(bundle);
                    activity.switchFragment(otpVerificationFragment);


                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_otp, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        iptCountry.setKeyListener(null);
        dialog = new SpotsDialog(getContext(), "Sending to code…");
    }

    @OnClick(R.id.tv_sign_in)
    public void goToSignInActivity() {
        activity = (SignUpOTPActivity) getActivity();
        SignInOTPFragment signInOTPFragment = new SignInOTPFragment();
        activity.switchFragment(signInOTPFragment);
    }


    @OnClick({R.id.btn_sign_up})
    public void goOTPVerification(View view) {

        if (checkInfo()) {
            dialog.show();
            mPhoneNumber = iptPhoneNumber.getText().toString();
            if (mPhoneNumber.startsWith("0")) {
                mPhoneNumber = mPhoneNumber.substring(1);
            }
            if (country != null) {
                mPhoneNumber = country.getPhoneCode() + mPhoneNumber;
                userManager.sendVertificationCode(getActivity(),mCallback,mPhoneNumber);
            }
        } else {
            return;
        }

    }

    private boolean checkInfo() {
        boolean check = true;
        if (!TextUtils.isEmpty(iptPhoneNumber.getText().toString().trim())) {
            check = true;
            tilPhone.setErrorEnabled(false);

        } else {
            check = false;
            tilPhone.setErrorEnabled(true);
            tilPhone.setError("Incorrect phone number");
        }
        //check country
        boolean check1 = true;
        if (!TextUtils.isEmpty(iptCountry.getText().toString().trim())) {
            check1 = true;
            tilNationality.setErrorEnabled(false);

        } else {
            check1 = false;
            tilPhone.setErrorEnabled(true);
            tilNationality.setError("Select country");
        }
        return check && check1;
    }
    @OnClick({R.id.input_layout_country, R.id.ipt_country})
    public void showEditDialog() {
        CountryFragment countryFragment = CountryFragment.newInstance();
        countryFragment.setTargetFragment(this, REQUEST_CODE);
        countryFragment.show(getFragmentManager(), "");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            country = data.getBundleExtra("item").getParcelable("country");
            iptCountry.setText(country.getName() + "(" + country.getPhoneCode() + ")");
        }

    }

}