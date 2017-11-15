package com.coderschool.sosvn.fragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.coderschool.sosvn.R;
import com.coderschool.sosvn.adapter.BloodTypeAdapter;
import com.coderschool.sosvn.manager.UserManager;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by PC on 7/24/2017.
 */

public class BloodTypeFragment extends DialogFragment {

    @BindView(R.id.rv_bloodType)
    RecyclerView rvBloodType;

    UserManager mUserManager = UserManager.getInstance();

    BloodTypeAdapter mBloodTypeAdapter;
    List<String> mBloodList;

    public static BloodTypeFragment newInstance() {
        Bundle args = new Bundle();
        BloodTypeFragment fragment = new BloodTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blood_type, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBloodList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.blood_array)));
        mBloodTypeAdapter = new BloodTypeAdapter(getContext());
        mBloodTypeAdapter.setData(mBloodList);

        rvBloodType.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvBloodType.setAdapter(mBloodTypeAdapter);

        mBloodTypeAdapter.setListener(new BloodTypeAdapter.Listener() {
            @Override
            public void onItemClick(String blood) {
                Intent i = new Intent();
                i.putExtra("item",blood);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,i);
                dismiss();
            }
        });
    }

    @OnClick(R.id.btnClose)
    public void onClose() {
        dismiss();
    }


}
