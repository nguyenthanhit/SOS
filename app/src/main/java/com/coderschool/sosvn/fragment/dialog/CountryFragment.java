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

import com.coderschool.sosvn.R;
import com.coderschool.sosvn.adapter.CountryAdapter;
import com.coderschool.sosvn.manager.UserManager;
import com.coderschool.sosvn.object.Country;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 7/26/2017.
 */

public class CountryFragment extends DialogFragment{

    @BindView(R.id.rv_countries)
    RecyclerView rvCountries;

    CountryAdapter countryAdapter;
    List<Country> mCountryList;
    UserManager userManager = UserManager.getInstance();

    public static CountryFragment newInstance() {
        Bundle args = new Bundle();
        CountryFragment fragment = new CountryFragment();
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

        View view = inflater.inflate(R.layout.fragment_list_country, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCountryList = new ArrayList<>();
        mCountryList = userManager.getAllCountries();
        countryAdapter = new CountryAdapter(getContext());
        countryAdapter.setData(mCountryList);

        rvCountries.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvCountries.setHasFixedSize(true);
        rvCountries.setAdapter(countryAdapter);
        countryAdapter.setListener(new CountryAdapter.Listener() {
            @Override
            public void onItemClick(Country country) {
                Intent i = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("country", country);
                i.putExtra("item", bundle);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                dismiss();
            }
        });
    }


    @OnClick(R.id.btnClose)
    public void onClose() {
        dismiss();
    }

}
