package com.coderschool.sosvn.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.coderschool.sosvn.R;
import com.coderschool.sosvn.fragment.ProfileMedicalFragment;
import com.coderschool.sosvn.fragment.ProfilePersonalFragment;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {

    private static final int RESULT_GALLERY = 0;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Bitmap bitmap = null;

    @BindView(R.id.iv_user_avatar)
    CircleImageView ivUserAvatar;
    @BindView(R.id.iv_camera_change_avatar)
    ImageView ivCameraChangeAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case RESULT_GALLERY:
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        ivUserAvatar.setImageBitmap(bitmap);
                        ivCameraChangeAvatar.setVisibility(View.GONE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @OnClick({R.id.iv_user_avatar, R.id.iv_camera_change_avatar})
    public void clickSave() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_GALLERY);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    ProfilePersonalFragment tab1 = new ProfilePersonalFragment();
                    return tab1;
                case 1:
                    ProfileMedicalFragment tab2 = new ProfileMedicalFragment();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Personal";
                case 1:
                    return "Medical";
            }
            return null;
        }
    }
}
