package com.coderschool.sosvn.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.coderschool.sosvn.R;
import com.coderschool.sosvn.activity.MainActivity;
import com.coderschool.sosvn.fragment.dialog.BloodTypeFragment;
import com.coderschool.sosvn.fragment.dialog.CountryFragment;
import com.coderschool.sosvn.manager.RealmManger;
import com.coderschool.sosvn.manager.UserManager;
import com.coderschool.sosvn.object.Country;
import com.coderschool.sosvn.object.User;
import com.coderschool.sosvn.util.FixedHoloDatePickerDialog;
import com.coderschool.sosvn.util.StringUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class UserProfileFragment extends Fragment implements UserManager.Callback<User> {

    private static final int RESULT_GALLERY = 0;
    private static final int REQUEST_CODE_NATIONALITY = 20;
    private final int REQUEST_CODE = 21;

    @BindView(R.id.input_layout_email)
    TextInputLayout tilEmail;
    @BindView(R.id.input_password)
    TextInputLayout tilPassword;
    @BindView(R.id.input_password_repeat)
    TextInputLayout tilPasswordRepeat;
    @BindView(R.id.input_layout_birthday)
    TextInputLayout tilBirthday;
    @BindView(R.id.input_layout_blood_type)
    TextInputLayout tilBloodType;
    @BindView(R.id.input_layout_ice)
    TextInputLayout tilICE;
    @BindView(R.id.input_layout_insurance)
    TextInputLayout tilInsurance;
    @BindView(R.id.input_layout_first_name)
    TextInputLayout tilFirstName;
    @BindView(R.id.input_layout_last_name)
    TextInputLayout tilLastName;
    @BindView(R.id.input_layout_nationality)
    TextInputLayout tilNationality;

    @BindView(R.id.ipt_password_repeat)
    EditText iptPasswordRepeat;
    @BindView(R.id.ipt_first_name)
    EditText iptFirstName;
    @BindView(R.id.ipt_last_name)
    EditText iptLastName;
    @BindView(R.id.ipt_birthday)
    EditText iptBirthday;
    @BindView(R.id.ipt_email)
    EditText iptEmail;
    @BindView(R.id.ipt_ice)
    EditText iptICE;
    @BindView(R.id.ipt_insurance_policy_number)
    EditText iptInsuranceNumber;
    @BindView(R.id.ipt_nationality)
    EditText iptCoutry;
    @BindView(R.id.ipt_med_info)
    EditText iptMedicalCondition;
    @BindView(R.id.ipt_blood_type)
    EditText iptBloodType;
    @BindView(R.id.ipt_pass)
    EditText iptPassword;
    @BindView(R.id.iv_user_avatar)
    CircleImageView avatar;
    @BindView(R.id.rg_gender)
    RadioGroup rgGender;
    @BindView(R.id.rb_male)
    RadioButton rbMale;
    @BindView(R.id.rb_female)
    RadioButton rbFeMale;

    DatePickerDialog datePickerDialog;
    AlertDialog dialog;
    UserManager userManager = UserManager.getInstance();
    RealmManger realmManger = RealmManger.getInstance();
    Country country;

    //
    ArrayAdapter<String> mGenderAdapter;
    List<String> mListGenders;
    Bitmap bitmap = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, parent, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        dialog = new SpotsDialog(getContext(), "Updating...");

//        WelcomeCoordinatorLayout coordinatorLayout
//                = (WelcomeCoordinatorLayout) getActivity().findViewById(R.id.coordinator);
//        coordinatorLayout.addPage(
//                R.layout.fragment_signup_s1,
//                R.layout.fragment_signup_s2);
//        coordinatorLayout.setScrollingEnabled(true);
        ButterKnife.bind(this, view);

        userManager.setCallback(this);
        iptCoutry.setKeyListener(null);
        iptBloodType.setKeyListener(null);
        iptBirthday.setKeyListener(null);


        // Toast.makeText(getContext(),userManager.getUser().getUid()+"---"+userManager.getUser().getPhoneNumber(),Toast.LENGTH_SHORT).show();
    }


    @OnClick({R.id.input_layout_birthday, R.id.ipt_birthday})
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

    @OnClick({R.id.btn_save_code})
    public void saveClicked() {
        saveInfoUser();

    }

    @Override
    public void onStop() {
        super.onStop();
        //   saveInfoUser();

    }


    private boolean checkInfoUser() {
        boolean check = true;

        // check birthday
        if (TextUtils.isEmpty(iptBirthday.getText().toString().trim())) {
            check = false;
            tilBirthday.setErrorEnabled(true);
            tilBirthday.setError("Please choose your birthday");
        } else {
            tilBirthday.setErrorEnabled(false);
        }

        // check bloodType
        if (TextUtils.isEmpty(iptBloodType.getText().toString().trim())) {
            check = false;
            tilBloodType.setErrorEnabled(true);
            tilBloodType.setError("Please choose your blood type");
        } else {
            tilBloodType.setErrorEnabled(false);

        }

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

        // check ICE
        if (TextUtils.isEmpty(iptICE.getText().toString().trim())) {
            check = false;
            tilICE.setErrorEnabled(true);
            tilICE.setError("Please enter your ICE");
        } else {
            tilICE.setErrorEnabled(false);

        }

        // check Issurance
        if (TextUtils.isEmpty(iptInsuranceNumber.getText().toString().trim())) {
            check = false;
            tilInsurance.setErrorEnabled(true);
            tilInsurance.setError("Please enter your insurace policy");
        } else {
            tilInsurance.setErrorEnabled(false);

        }

        //check Nationality
        if (TextUtils.isEmpty(iptCoutry.getText().toString().trim())) {
            check = false;
            tilNationality.setErrorEnabled(true);
            tilNationality.setError("Please choose your nationality");
        } else {
            tilNationality.setErrorEnabled(false);

        }
        //check avatar
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.privateuser);
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

        // check medical condition
        if (TextUtils.isEmpty(iptMedicalCondition.getText().toString())) {
            iptMedicalCondition.setText("");
        }


        return check;
//
    }

    private void saveInfoUser() {
        if (checkInfoUser()) {
            dialog.show();
            User newUser = new User();
            newUser.setId(userManager.getUser().getUid());
            newUser.setBirthday(iptBirthday.getText().toString().trim());
            newUser.setBloodType(iptBloodType.getText().toString().trim());
            newUser.setFirstName(iptFirstName.getText().toString().trim());
            String gender = ((RadioButton) getView()
                    .findViewById(rgGender.getCheckedRadioButtonId())).getText().toString();
            newUser.setGender(gender);
            newUser.setLastName(iptLastName.getText().toString().trim());
            newUser.setIce(iptLastName.getText().toString().trim());
            newUser.setInsuranceNunmber(iptLastName.getText().toString().trim());
            newUser.setNationality(iptCoutry.getText().toString().trim());
            newUser.setUrlAvatar(StringUtil.getStringFromBitMap(bitmap));
            newUser.setEmail(iptEmail.getText().toString().trim());
            newUser.setPassword(iptPassword.getText().toString().trim());
            newUser.setMedicalConditions(iptMedicalCondition.getText().toString());

            userManager.writeUserToDB(getActivity(), newUser);

        } else {
            Log.d("NOT_SAVE_USER", "Error");
            return;
        }
    }

    @OnClick(R.id.iv_user_avatar)
    public void clickSave() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_GALLERY);
    }

    @OnClick({R.id.input_layout_blood_type, R.id.ipt_blood_type})
    public void showBloodType() {
        BloodTypeFragment bloodTypeFragment = BloodTypeFragment.newInstance();
        bloodTypeFragment.setTargetFragment(this, REQUEST_CODE);
        bloodTypeFragment.show(getFragmentManager(), "");
    }

    @OnClick({R.id.input_layout_nationality, R.id.ipt_nationality})
    public void showNationality() {
        CountryFragment countryFragment = CountryFragment.newInstance();
        countryFragment.setTargetFragment(this, REQUEST_CODE_NATIONALITY);
        countryFragment.show(getFragmentManager(), "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case REQUEST_CODE:
                    iptBloodType.setText(data.getStringExtra("item"));
                    break;
                case REQUEST_CODE_NATIONALITY:
                    country = data.getBundleExtra("item").getParcelable("country");
                    iptCoutry.setText(country.getName());
                    break;
                case RESULT_GALLERY:
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        avatar.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    public void onSuccess(List<User> list) {

    }

    @Override
    public void onComplete(boolean check) {
        dialog.dismiss();
        if (check) {

            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
            //  getActivity().finish();
        } else {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
}