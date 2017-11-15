package com.coderschool.sosvn.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.coderschool.sosvn.R;
import com.coderschool.sosvn.ahoy.AhoyOnboarderActivity;
import com.coderschool.sosvn.ahoy.AhoyOnboarderCard;
import com.coderschool.sosvn.fragment.SignUpOTPFragment;
import com.coderschool.sosvn.manager.RealmManger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpOTPActivity extends AhoyOnboarderActivity {

    private final int FIND_TITLE = R.string.find_title;
    private final int CALL_TITLE = R.string.call_emergency;
    private final int REPORT_TITLE = R.string.report_title;
    private final int FIND_MESS = R.string.find_mess;
    private final int CALL_MESS = R.string.call_mess;
    private final int REPORT_MESS = R.string.report_mess;

    @BindView(R.id.parent_layout)
    RelativeLayout ahoy;

    RealmManger realmManger = RealmManger.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        // Set initial fragment
        setInitialFragment(savedInstanceState);

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard(getResources().getString(FIND_TITLE)
                , getResources().getString(FIND_MESS)
                , R.drawable.ic_find_hospital);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard(getResources().getString(CALL_TITLE)
                , getResources().getString(CALL_MESS)
                , R.drawable.ic_call_doctor);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard(getResources().getString(REPORT_TITLE)
                , getResources().getString(REPORT_MESS)
                , R.drawable.ic_report_checker);

        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);

        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);

        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.grey_200);
        }

        setGradientBackground();


        setOnboardPages(pages);


    }

    private void setInitialFragment(Bundle savedInstanceState) {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fl_sign_up_otp) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            SignUpOTPFragment mainFragment = new SignUpOTPFragment();
            mainFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_sign_up_otp, mainFragment, SignUpOTPFragment.class.getSimpleName()).commit();
        }
    }

    //responsible for switching fragments
    public void switchFragment(Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fl_sign_up_otp, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onFinishButtonPressed() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ahoy.setVisibility(View.VISIBLE);
    }
}