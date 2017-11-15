package com.coderschool.sosvn.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coderschool.sosvn.R;
import com.coderschool.sosvn.manager.ReportManager;
import com.coderschool.sosvn.manager.UserManager;
import com.coderschool.sosvn.object.Report;
import com.coderschool.sosvn.object.User;
import com.coderschool.sosvn.util.PermissionUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.redbooth.WelcomeCoordinatorLayout;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportActivity extends AppCompatActivity implements BaseToggleSwitch.OnToggleSwitchChangeListener {

    @BindView(R.id.iv_part_of_body)
    ImageView ivPartOfBody;


    //Report Personal section
    @BindView(R.id.tv_first_name_key)
    TextView tvFirstNameKey;
    @BindView(R.id.tv_last_name_key)
    TextView tvLastNameKey;
    @BindView(R.id.tv_age_key)
    TextView tvAgeKey;
    @BindView(R.id.tv_gender_key)
    TextView tvGenderKey;
    @BindView(R.id.tv_nationality_key)
    TextView tvNationalityKey;

    // Report medical section
    @BindView(R.id.tv_ice_key)
    TextView tvICEKey;
    @BindView(R.id.tv_policy_no_key)
    TextView tvPolicyNoKey;
    @BindView(R.id.tv_blood_type_key)
    TextView tvBloodTypeKey;
    @BindView(R.id.tv_conditions_key)
    TextView tvConditionsKey;

    // Injury information
    @BindView(R.id.tv_type_key)
    TextView tvTypeKey;
    @BindView(R.id.tv_severity_key)
    TextView tvSeverityKey;

    // Injury information
    @BindView(R.id.tv_injury_info)
    TextView tvInjuryInfo;
    @BindView(R.id.tv_personal_info)
    TextView tvPersonalInfo;
    @BindView(R.id.tv_medical_info)
    TextView tvMedicalInfo;


    //Report Personal section
    @BindView(R.id.tv_first_name)
    TextView tvFirstName;
    @BindView(R.id.tv_last_name)
    TextView tvLastName;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_nationality)
    TextView tvNationality;

    // Report medical section
    @BindView(R.id.tv_ice)
    TextView tvICE;
    @BindView(R.id.tv_policy_no)
    TextView tvPolicyNo;
    @BindView(R.id.tv_blood_type)
    TextView tvBloodType;
    @BindView(R.id.tv_conditions)
    TextView tvConditions;

    // Injury information
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_severity)
    TextView tvSeverity;
    @BindView(R.id.iv_where_report_summary)
    ImageView ivWhereReportSummary;

    // Language switch
    @BindView(R.id.switcher)
    ToggleSwitch switcher;

    WelcomeCoordinatorLayout coordinatorLayout;

    private String mType;
    private String mSeverity;
    private static Bitmap injuryMarkBitmap;

    // Summary
    private ReportManager reportManager;
    private Report report;

    //Firebase
    UserManager userManager = UserManager.getInstance();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null == FirebaseAuth.getInstance().getCurrentUser()){
            startActivity(new Intent(this, SignUpOTPActivity.class));
            finish();
        };

        setContentView(R.layout.activity_report);

//        PermissionUtils.requestCall(this);

        coordinatorLayout = (WelcomeCoordinatorLayout) findViewById(R.id.coordinator_report);

        coordinatorLayout.addPage(
                R.layout.report_s1_type,
                R.layout.report_s2_severity,
                R.layout.report_s3_mark,
                R.layout.report_s5_call,
                R.layout.report_s4_summary
        );

        coordinatorLayout.setScrollingEnabled(true);

        ButterKnife.bind(this);

        switcher.setOnToggleSwitchChangeListener(this);

        //Set defaults
        mType = getResources().getString(R.string.what_happened_accident);
        mSeverity = getResources().getString(R.string.severity_high);

        //Setup body mapping
        setupBodyMapping();

        //Report
        reportManager = ReportManager.getInstance();
        report = reportManager.getReport();
        initReport();
        updateReport();


    }

    private void setupBodyMapping() {
        injuryMarkBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.human_body_man_filled);

        ivPartOfBody.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                float posX = event.getX();
                float posY = event.getY();
                drawCircleAt(posX, posY);
                return false;
            }
        });
    }

    // fragment_report_what_happened

    @OnClick({R.id.btn_type_accident, R.id.btn_type_food_poisoning, R.id.btn_type_other_injury})
    public void updateType(View view) {
        switch (view.getId()) {
            case R.id.btn_type_accident:
                mType = getResources().getString(R.string.what_happened_accident);
                break;
            case R.id.btn_type_food_poisoning:
                mType = getResources().getString(R.string.what_happened_food_poisoning);
                break;
            case R.id.btn_type_other_injury:
                mType = getResources().getString(R.string.what_happened_other_injury);
                break;
        }
        updateReport();
        coordinatorLayout.setCurrentPage(coordinatorLayout.getPageSelected()+1, true);
    }

    // fragment_report_severity

    @OnClick({R.id.btn_severity_high, R.id.btn_severity_medium, R.id.btn_severity_low})
    public void setUpHighlight(View view) {
        switch (view.getId()) {
            case R.id.btn_severity_high:
                mSeverity = getResources().getString(R.string.severity_high);
                break;
            case R.id.btn_severity_medium:
                mSeverity = getResources().getString(R.string.severity_high);
                break;
            case R.id.btn_severity_low:
                mSeverity = getResources().getString(R.string.severity_high);
                break;
        }
        updateReport();
        coordinatorLayout.setCurrentPage(coordinatorLayout.getPageSelected()+1, true);
    }

    // Body BMP functions

    @OnClick(R.id.btn_clear_marks)
    public void clearMarks(){

        //Clears drawing bitmap
        ivPartOfBody.destroyDrawingCache();

        Glide.with(this)
                .load(R.mipmap.human_body_man_filled)
                .crossFade()
                .into(ivPartOfBody);
        updateReport();
    }

    public void drawCircleAt(float posX, float posY) {
        ivPartOfBody.buildDrawingCache();
        injuryMarkBitmap = ivPartOfBody.getDrawingCache();

        int colorAtTouch = injuryMarkBitmap.getPixel((int) posX, (int) posY);

        String targetColorAsString = "#" + Integer.toHexString(ContextCompat.getColor(this, R.color.body));
        int targetColor = Color.parseColor(targetColorAsString);
        if (colorAtTouch == targetColor) {
            Canvas canvas = new Canvas(injuryMarkBitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(getResources().getColor(R.color.colorPrimary));
            canvas.drawCircle(posX, posY, 30, paint);
            ivPartOfBody.setImageBitmap(injuryMarkBitmap);
        }
        updateReport();
    }

    public void updateReport() {
        if (report != null) {
            Bitmap bm = ivPartOfBody.getDrawingCache();
            ivWhereReportSummary.setImageBitmap(bm);
            tvType.setText(mType);
            tvSeverity.setText(mSeverity);
        }
    }

    public void initReport(){
        setEnglishDescirption();

         final User userData[] = new User[1];

         ValueEventListener listener = new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot snapshot) {
                 userData[0] = snapshot
                         .getValue(User.class);
                 updateUserDataInReport(userData[0]);

                 Log.d("XX", userData[0].toString());
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {
             }
         };

        userManager.getUserData(listener);

    }

    private void updateUserDataInReport(User userData) {
        tvFirstName.setText(userData.getFirstName());
        tvLastName.setText(userData.getLastName());
        tvAge.setText(userData.getBirthday());
        tvNationality.setText(userData.getNationality());
        tvGender.setText(userData.getGender());
        tvICE.setText(userData.getIce());
        tvPolicyNo.setText(userData.getInsuranceNunmber());
        tvBloodType.setText(userData.getBloodType());
    }

    @Override
    public void onToggleSwitchChangeListener(int position, boolean isChecked) {
        Log.d("xx", "Switcher position: " + String.valueOf(position));
        if (position == 0) {
            setEnglishDescirption();
        } else {
            setVietnameseDescription();
        }
    }

    private void setVietnameseDescription() {
        tvPersonalInfo.setText(getResources().getString(R.string.report_personal_info_vn));
        tvMedicalInfo.setText(getResources().getString(R.string.report_medical_info_vn));
        tvInjuryInfo.setText(getResources().getString(R.string.report_injury_info_vn));

        tvFirstNameKey.setText(getResources().getString(R.string.report_first_name_vn));
        tvLastNameKey.setText(getResources().getString(R.string.report_last_name_vn));
        tvAgeKey.setText(getResources().getString(R.string.report_age_vn));
        tvNationalityKey.setText(getResources().getString(R.string.report_nationality_vn));
        tvGenderKey.setText(getResources().getString(R.string.report_gender_vn));
        tvICEKey.setText(getResources().getString(R.string.report_ice_vn));
        tvPolicyNoKey.setText(getResources().getString(R.string.report_policy_vn));
        tvBloodTypeKey.setText(getResources().getString(R.string.report_blood_type_vn));
        tvConditionsKey.setText(getResources().getString(R.string.report_conditions_vn));
        tvTypeKey.setText(getResources().getString(R.string.report_type_vn));
        tvSeverityKey.setText(getResources().getString(R.string.report_severity_vn));

    }

    private void setEnglishDescirption() {
        tvPersonalInfo.setText(getResources().getString(R.string.report_personal_info));
        tvMedicalInfo.setText(getResources().getString(R.string.report_medical_info));
        tvInjuryInfo.setText(getResources().getString(R.string.report_injury_info));

        tvFirstNameKey.setText(getResources().getString(R.string.report_first_name));
        tvLastNameKey.setText(getResources().getString(R.string.report_last_name));
        tvAgeKey.setText(getResources().getString(R.string.report_age));
        tvNationalityKey.setText(getResources().getString(R.string.report_nationality));
        tvGenderKey.setText(getResources().getString(R.string.report_gender));
        tvICEKey.setText(getResources().getString(R.string.report_ice));
        tvPolicyNoKey.setText(getResources().getString(R.string.report_policy));
        tvBloodTypeKey.setText(getResources().getString(R.string.report_blood_type));
        tvConditionsKey.setText(getResources().getString(R.string.report_conditions));
        tvTypeKey.setText(getResources().getString(R.string.report_type));
        tvSeverityKey.setText(getResources().getString(R.string.report_severity));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @OnClick({R.id.btn_call_intent})
    public void callMe() {
        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:111222333"));


        if (PermissionUtils.checkCall(this)) {
            try {
                startActivity(in);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "Could not find an activity to place the call.", Toast.LENGTH_SHORT).show();
            }
        } else {
//            Toast.makeText(this, "nO PERMISSION?", Toast.LENGTH_SHORT).show();

            PermissionUtils.requestCall(this);
        }
    }
}
