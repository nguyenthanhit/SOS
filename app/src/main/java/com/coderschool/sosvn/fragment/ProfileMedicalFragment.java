package com.coderschool.sosvn.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.coderschool.sosvn.R;
import com.coderschool.sosvn.fragment.dialog.BloodTypeFragment;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.bitmap;

public class ProfileMedicalFragment extends Fragment{

    private final int REQUEST_CODE_BLOOD_TYPE = 21;

    @BindView(R.id.input_layout_blood_type)
    TextInputLayout tilBloodType;
    @BindView(R.id.input_layout_ice)
    TextInputLayout tilICE;
    @BindView(R.id.input_layout_insurance)
    TextInputLayout tilInsurance;
    @BindView(R.id.input_layout_ice_name)
    TextInputLayout tilICEName;

    @BindView(R.id.ipt_ice)
    EditText iptICE;
    @BindView(R.id.ipt_ice_name)
    EditText iptICEName;
    @BindView(R.id.ipt_insurance_policy_number)
    EditText iptInsuranceNumber;
    @BindView(R.id.ipt_insurance_policy_number_company)
    EditText iptInsuranceNumbeCompany;
    @BindView(R.id.ipt_blood_type)
    EditText iptBloodType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_medical, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case REQUEST_CODE_BLOOD_TYPE:
                    iptBloodType.setText(data.getStringExtra("item"));
                    break;
            }
        }
    }

    @OnClick({R.id.input_layout_blood_type, R.id.ipt_blood_type})
    public void showBloodType() {
        BloodTypeFragment bloodTypeFragment = BloodTypeFragment.newInstance();
        bloodTypeFragment.setTargetFragment(this, REQUEST_CODE_BLOOD_TYPE);
        bloodTypeFragment.show(getFragmentManager(), "");
    }

    private boolean checkInfoUser() {
        boolean check = true;

        // check bloodType
        if (TextUtils.isEmpty(iptBloodType.getText().toString().trim())) {
            check = false;
            tilBloodType.setErrorEnabled(true);
            tilBloodType.setError("Please choose your blood type");
        } else {
            tilBloodType.setErrorEnabled(false);
        }

        // check ICE
        if (TextUtils.isEmpty(iptICE.getText().toString().trim())) {
            check = false;
            tilICE.setErrorEnabled(true);
            tilICE.setError("Please enter your ICE");
        } else {
            tilICE.setErrorEnabled(false);
        }

        // check ICE Name
        if (TextUtils.isEmpty(iptICE.getText().toString().trim())) {
            check = false;
            tilICEName.setErrorEnabled(true);
            tilICEName.setError("Please enter your ice'name ");
        } else {
            tilICEName.setErrorEnabled(false);
        }

        // check Issurance
        if (TextUtils.isEmpty(iptInsuranceNumber.getText().toString().trim())) {
            check = false;
            tilInsurance.setErrorEnabled(true);
            tilInsurance.setError("Please enter your insurace policy");
        } else {
            tilInsurance.setErrorEnabled(false);
        }

        return check;
    }
}
