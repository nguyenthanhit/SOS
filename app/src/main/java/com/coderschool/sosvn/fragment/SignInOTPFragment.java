package com.coderschool.sosvn.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.coderschool.sosvn.R;
import com.coderschool.sosvn.activity.MainActivity;
import com.coderschool.sosvn.activity.SignUpOTPActivity;
import com.coderschool.sosvn.manager.UserManager;
import com.coderschool.sosvn.object.User;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

/**
 * Created by Admin on 7/31/2017.
 */

public class SignInOTPFragment extends Fragment implements UserManager.Callback<User>{

    @BindView(R.id.input_layout_email)
    TextInputLayout tilEmail;
    @BindView(R.id.input_layout_password)
    TextInputLayout tilPassword;

    @BindView(R.id.ipt_email)
    EditText etEmail;

    @BindView(R.id.ipt_pass_word)
    EditText etPassword;

    @BindView(R.id.text_incorrect)
    TextView tvIncorrect;


     UserManager userManager = UserManager.getInstance();
    SignUpOTPActivity activity;

    AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userManager.setCallback(this);
        dialog = new SpotsDialog(getContext(),"Logining...");
    }



    @OnClick(R.id.btn_sign_in)
    public void signIn() {

        if (checkInfoUser()){
            dialog.show();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            userManager.login(getActivity(),email,password);
        }
        else {
            return;
        }

    }

    @OnClick(R.id.tv_new_account)
    public void goToSignUpActivity() {

        activity = (SignUpOTPActivity) getActivity();
        SignUpOTPFragment signUpOTPFragment = new SignUpOTPFragment();
        activity.switchFragment(signUpOTPFragment);
    }

    private boolean checkInfoUser() {
        boolean check = true;
        // check email
        Pattern pattern1 = Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");
        Matcher matcher1 = pattern1.matcher(etEmail.getText().toString().trim());
        if (!matcher1.matches()) {
            check = false;
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("Please enter your email");
        } else {
            tilEmail.setErrorEnabled(false);

        }
        // check password
        if (etPassword.getText().toString().trim().length() < 6) {
            check = false;
            tilPassword.setErrorEnabled(true);
            tilPassword.setError("Your password have at least 6 character");
        } else {
            tilPassword.setErrorEnabled(false);

        }
        return check;
    }
    @Override
    public void onSuccess(List<User> list) {

    }

    @Override
    public void onComplete(boolean check) {
        dialog.dismiss();
        if (check) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        } else {
            etEmail.setText("");
            etPassword.setText("");
        }
    }
}
