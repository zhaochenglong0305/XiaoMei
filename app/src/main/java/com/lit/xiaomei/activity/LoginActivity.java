package com.lit.xiaomei.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;

import com.fyjr.baselibrary.base.BaseActivity;
import com.fyjr.baselibrary.http.HttpUtil;
import com.fyjr.baselibrary.http.callback.HttpCallBack;
import com.fyjr.baselibrary.http.url.HttpUrl;
import com.lit.xiaomei.R;
import com.lit.xiaomei.bean.CityPhones;
import com.lit.xiaomei.bean.Constants;
import com.lit.xiaomei.databinding.ActivityLoginBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import com.lit.xiaomei.bean.User;
import com.lit.xiaomei.manager.UseInfoManager;
import com.lit.xiaomei.view.DialogShowKeFu;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements View.OnClickListener {

    private String userCity = "";
    private String userName = "";
    private String passWord = "";
    private int ipFre = 0;
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_login);

    }

    @Override
    public void initView() {
        super.initView();
        binding.tvAppName.setText(getAppName(this) + " " + getVersionName(this));
        user = UseInfoManager.getUser(this);
        if (!TextUtils.isEmpty(UseInfoManager.getString(this, "userCity"))) {
            userCity = UseInfoManager.getString(this, "userCity");
        }
        initEditBoder();
        binding.btnLogin.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.tvForgetPwd.setOnClickListener(this);
        binding.cbRememberPassword.setChecked(UseInfoManager.getBoolean(this, "RememberPassword", true));
        binding.cbAutomaticLogon.setChecked(UseInfoManager.getBoolean(this, Constants.Tag.autoLogin, false));

        if (UseInfoManager.getBoolean(this, "RememberPassword", false)) {
            if (user != null) {
                binding.etUsername.setText(user.getListData().get(0).getUS());
                binding.etPassword.setText(user.getListData().get(0).getPW());
            }
        }
        binding.tvKefu.setOnClickListener(this);

        if (UseInfoManager.getBoolean(LoginActivity.this, Constants.Tag.autoLogin, false)) {
            userName = binding.etUsername.getText().toString();
            passWord = binding.etPassword.getText().toString();
            if (TextUtils.isEmpty(userName)) {
                showMessage("账号不能为空！");
                return;
            }
            if (TextUtils.isEmpty(passWord)) {
                showMessage("密码不能为空！");
                return;
            }
            doLogin(binding.etUsername.getText().toString(), binding.etPassword.getText().toString(), getANDROID_ID(), "0", "1", true);
        }
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
                userName = binding.etUsername.getText().toString();
                passWord = binding.etPassword.getText().toString();
                if (TextUtils.isEmpty(userName)) {
                    showMessage("账号不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(passWord)) {
                    showMessage("密码不能为空！");
                    return;
                }
                doLogin(binding.etUsername.getText().toString(), binding.etPassword.getText().toString(), getANDROID_ID(), "0", "1", true);
                break;
            case R.id.btn_register:
                Intent intent = new Intent(this, RegistActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.tv_forget_pwd:
                startActivity(new Intent(this, UpdatePasswordActivity.class));
                break;
            case R.id.tv_kefu:
                getCityPhone();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (data == null) {
                return;
            }
            userName = data.getStringExtra("userName");
            binding.etUsername.setText(userName);
            passWord = data.getStringExtra("password");
            binding.etPassword.setText(passWord);
            userCity = data.getStringExtra("userCity");
        }
    }

    private void doLogin(final String NetID, final String PWord, final String key, final String Type, final String TextFormat, boolean isShowLoading) {
        if (isShowLoading) {
            showLoading();
        }
        final ArrayList<String> ips = UseInfoManager.getStringArraylist(this, "NetIPs");
        HttpUrl.BASE_URL = ips.get(ipFre);
//        key为90cf9e6c2544be04越过权限，正式上线时替换成key
        HttpUtil.getInstance().login(NetID, PWord, getANDROID_ID(), Type, TextFormat, new HttpCallBack<User>() {
            @Override
            public void onSuccess(User data, String msg) {
                hideLoading();
                int rz = Integer.valueOf(data.getListData().get(0).getRZ());
                switch (rz) {
                    case 0:
                        showMessage("机器码发生变化不能登录！");
                        break;
                    case 1:
                        data.getListData().get(0).setPW(PWord);
                        UseInfoManager.putBoolean(LoginActivity.this, "RememberPassword", binding.cbRememberPassword.isChecked());
                        UseInfoManager.putBoolean(LoginActivity.this, Constants.Tag.autoLogin, binding.cbAutomaticLogon.isChecked());
                        UseInfoManager.saveUser(LoginActivity.this, data);
                        UseInfoManager.putString(LoginActivity.this, "userCity", data.getListData().get(0).getCT());
                        showMessage("登录成功！");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        break;
                    case 2:
                        showMessage("密码错误！");
                        break;
                    case 3:
                        showMessage("用户已过期，请联系客服！");
                        break;
                    case 4:
                        showMessage("禁止登陆，请联系客服！");
                        break;
                    case 5:
                        showMessage("用户不存在！");
                        break;
                }
            }

            @Override
            public void onFail(int errorCode, String msg) {
                ipFre++;
                if (ipFre < ips.size()) {
                    showMessage("登录失败，正在切换线路！");
                    doLogin(NetID, PWord, key, Type, TextFormat, false);
                } else {
                    ipFre = 0;
                    hideLoading();
                    showMessage("登录失败，请检查网络！");
                }
            }
        });

    }


    public String getANDROID_ID() {
        return Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
    }

    private void initAutomaticLogon() {
        if (UseInfoManager.getBoolean(this, "AutomaticLogon", false)) {
            if (user != null) {
                doLogin(user.getListData().get(0).getUS(), user.getListData().get(0).getPW(), user.getListData().get(0).getKY(), "0", "1", true);
            }
        }
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getCityPhone() {
        showLoading();
        HttpUtil.getInstance().getCityPhone(new HttpCallBack<CityPhones>() {
            @Override
            public void onSuccess(CityPhones data, String msg) {
                hideLoading();
                CityPhones.OpratesBean opratesBean = null;
                if (!TextUtils.isEmpty(userCity)) {
                    for (CityPhones.OpratesBean ob : data.getOprates()) {
                        if (TextUtils.equals(userCity, ob.getDName())) {
                            opratesBean = ob;
                            break;
                        }
                    }
                }
                DialogShowKeFu dialogShowKeFu = new DialogShowKeFu(LoginActivity.this, opratesBean);
                dialogShowKeFu.showAtLocation(binding.rlLoginMain, Gravity.CENTER, 0, 0);
            }

            @Override
            public void onFail(int errorCode, String msg) {
                hideLoading();
            }
        });
    }
}
