package com.coderschool.sosvn.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.coderschool.sosvn.R;
import com.coderschool.sosvn.fragment.dialog.CountryFragment;
import com.coderschool.sosvn.object.Country;
import com.coderschool.sosvn.util.FixedHoloDatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfilePersonalFragment extends Fragment {

    private static final int REQUEST_CODE_NATIONALITY = 20;

    private DatePickerDialog datePickerDialog;
    private Country country;

    @BindView(R.id.input_layout_email)
    TextInputLayout tilEmail;
    @BindView(R.id.input_password)
    TextInputLayout tilPassword;
    @BindView(R.id.input_password_repeat)
    TextInputLayout tilPasswordRepeat;
    @BindView(R.id.input_layout_first_name)
    TextInputLayout tilFirstName;
    @BindView(R.id.input_layout_last_name)
    TextInputLayout tilLastName;
    @BindView(R.id.input_layout_nationality)
    TextInputLayout tilNationality;

    @BindView(R.id.ipt_first_name)
    EditText iptFirstName;
    @BindView(R.id.ipt_last_name)
    EditText iptLastName;
    @BindView(R.id.ipt_birthday)
    EditText iptBirthday;
    @BindView(R.id.ipt_email)
    EditText iptEmail;
    @BindView(R.id.ipt_pass)
    EditText iptPassword;
    @BindView(R.id.ipt_password_repeat)
    EditText iptPasswordRepeat;
    @BindView(R.id.ipt_nationality)
    EditText iptCountry;
    @BindView(R.id.rg_gender)
    RadioGroup rgGender;
    @BindView(R.id.rb_male)
    RadioButton rbMale;
    @BindView(R.id.rb_female)
    RadioButton rbFeMale;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_personal, container, false);
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
                case REQUEST_CODE_NATIONALITY:
                    country = data.getBundleExtra("item").getParcelable("country");
                    iptCountry.setText(country.getName());
                    break;
            }
        }
    }

    @OnClick(R.id.ipt_birthday)
    public void showDialogDate() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final Context themedContext = new ContextThemeWrapper(
                getContext(),
                android.R.style.Theme_Holo_Light_Dialog
        );
        final DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        datePickerDialog = new FixedHoloDatePickerDialog(themedContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(Calendar.YEAR, i);
                        calendar.set(Calendar.MONTH, i1);
                        calendar.set(Calendar.DAY_OF_MONTH, i2);
                        iptBirthday.setText(format.format(calendar.getTime()).toString());
                    }
                }, year, month, day);
        datePickerDialog.setTitle("Choose Your Birthday");
        datePickerDialog.show();

    }

    @OnClick({R.id.input_layout_nationality, R.id.ipt_nationality})
    public void showNationality() {
        CountryFragment countryFragment = CountryFragment.newInstance();
        countryFragment.setTargetFragment(this, REQUEST_CODE_NATIONALITY);
        countryFragment.show(getFragmentManager(), "");
    }

    private boolean checkInfoUser() {
        boolean check = true;

        // check first name
        if (TextUtils.isEmpty(iptFirstName.getText().toString().trim())) {
            check = false;
            tilFirstName.setErrorEnabled(true);
            tilFirstName.setError("Please enter your first name");
        } else {
            tilFirstName.setErrorEnabled(false);
        }

        // check last name
        if (TextUtils.isEmpty(iptLastName.getText().toString().trim())) {
            check = false;
            tilLastName.setErrorEnabled(true);
            tilLastName.setError("Please enter your last name");
        } else {
            tilLastName.setErrorEnabled(false);
        }

        // gender
        if (!rbFeMale.isChecked() && !rbMale.isChecked()) {
            check = false;
            rbFeMale.setError("Please choose");
        } else {
            rbFeMale.setError(null);
        }

        //check Nationality
        if (TextUtils.isEmpty(iptCountry.getText().toString().trim())) {
            check = false;
            tilNationality.setErrorEnabled(true);
            tilNationality.setError("Please choose your nationality");
        } else {
            tilNationality.setErrorEnabled(false);
        }

        //check email
        Pattern pattern1 = Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");
        Matcher matcher1 = pattern1.matcher(iptEmail.getText().toString().trim());
        if (!matcher1.matches()) {
            check = false;
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("Please enter your email");
        } else {
            tilEmail.setErrorEnabled(false);
        }

        // check pass word

        if (iptPassword.getText().toString().trim().length() < 6) {
            check = false;
            tilPassword.setErrorEnabled(true);
            tilPassword.setError("Your password have at least 6 character");
        } else {
            tilPassword.setErrorEnabled(false);
        }

        // check repeat password
        if (!TextUtils.equals(iptPasswordRepeat.getText(), iptPassword.getText())) {
            check = false;
            tilPasswordRepeat.setErrorEnabled(true);
            tilPasswordRepeat.setError(" Your password and confirmation password do not match");
        } else {
            tilPasswordRepeat.setErrorEnabled(false);
        }

        return check;
    }
}