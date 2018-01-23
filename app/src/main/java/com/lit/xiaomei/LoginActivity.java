package com.lit.xiaomei;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.fyjr.baselibrary.base.BaseActivity;
import com.lit.xiaomei.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_login);

    }

    @Override
    public void initView() {
        super.initView();
        initEditBoder();
        binding.btnLogin.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
    }

    private void initEditBoder() {
        binding.etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//获得焦点
                    binding.llInputUsername.setBackgroundResource(R.drawable.boder_login_input_select);
                } else {//未获得焦点
                    binding.llInputUsername.setBackgroundResource(R.drawable.boder_login_input_normal);
                }
            }
        });
        binding.etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//获得焦点
                    binding.llInputPassword.setBackgroundResource(R.drawable.boder_login_input_select);
                } else {//未获得焦点
                    binding.llInputPassword.setBackgroundResource(R.drawable.boder_login_input_normal);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                break;
            case R.id.btn_register:
                break;
        }
    }
    private void doLogin(){

    }
}
