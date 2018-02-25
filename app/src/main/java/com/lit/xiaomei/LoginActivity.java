package com.lit.xiaomei;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.fyjr.baselibrary.base.BaseActivity;
import com.fyjr.baselibrary.http.HttpUtil;
import com.fyjr.baselibrary.http.callback.HttpCallBack;
import com.fyjr.baselibrary.http.url.HttpUrl;
import com.lit.xiaomei.databinding.ActivityLoginBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import bean.User;
import manager.UseInfoManager;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements View.OnClickListener {
    private static final int GETNETIPFAIL = 0;
    private static final int GETNETIPSUCCESS = 1;
    private static final int GETNETIPNOTFIND = 2;
    private GetNetIPHandler getNetIPHandler;
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
        getNetIPHandler = new GetNetIPHandler();
        user = UseInfoManager.getUser(this);
        initNetIP();
        initEditBoder();
        binding.btnLogin.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.cbRememberPassword.setChecked(UseInfoManager.getBoolean(this, "RememberPassword", false));
        binding.cbAutomaticLogon.setChecked(UseInfoManager.getBoolean(this, "AutomaticLogon", false));
        binding.cbRememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UseInfoManager.putBoolean(LoginActivity.this, "RememberPassword", isChecked);
            }
        });
        binding.cbAutomaticLogon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UseInfoManager.putBoolean(LoginActivity.this, "AutomaticLogon", isChecked);
            }
        });
        if (UseInfoManager.getBoolean(this, "RememberPassword", false)) {
            if (user != null) {
                binding.etUsername.setText(user.getListData().get(0).getUS());
                binding.etPassword.setText(user.getListData().get(0).getPW());
            }
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
                break;
        }
    }

    private void doLogin(final String NetID, final String PWord, final String key, final String Type, final String TextFormat, boolean isShowLoading) {
        if (isShowLoading) {
            showLoading();
        }
        final ArrayList<String> ips = UseInfoManager.getStringArraylist(this, "NetIPs");
        HttpUrl.BASE_URL = ips.get(ipFre);
//        key为90cf9e6c2544be04越过权限，正式上线时替换成key
        HttpUtil.getInstance().login(NetID, PWord, "90cf9e6c2544be04", Type, TextFormat, new HttpCallBack<User>() {
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
                        UseInfoManager.saveUser(LoginActivity.this, data);
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

    private void initNetIP() {
        showLoading();
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    String serverURL = "http://blog.sina.com.cn/s/blog_1669867c40102x4m7.html";
                    // String serverURL =
                    // "http://blog.sina.com.cn/s/blog_1669867c40102x0mb.html";
                    Document document = Jsoup.connect(serverURL).get();
                    Elements elements = document.select("div");
                    Element element = elements.get(15);
                    if (!TextUtils.isEmpty(element.text())) {
                        msg.what = GETNETIPSUCCESS;
                        msg.obj = element.text();
                    } else {
                        msg.what = GETNETIPNOTFIND;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = GETNETIPFAIL;
                }
                getNetIPHandler.sendMessage(msg);
            }
        }.start();
    }

    private class GetNetIPHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            hideLoading();
            ArrayList<String> ips = new ArrayList<>();
            switch (msg.what) {
                case GETNETIPFAIL:
//                    113.6.252.165
                    ips.add("http://113.6.252.165:8081/");
                    break;
                case GETNETIPSUCCESS:
                    String res = (String) msg.obj;
                    if (res.contains("ABC") && res.contains("DEF")) {
                        String sub = res.substring(res.indexOf("ABC") + 3, res.indexOf("DEF"));
                        if (sub.contains(",")) {
                            String[] subs = sub.split(",");
                            for (int i = 0; i < subs.length; i++) {
                                ips.add("http://" + subs[i] + ":8081/");
                            }
                        } else {
                            ips.add(sub);
                        }
                    } else {
                        ips.add("http://113.6.252.165:8081/");
                    }
//                    initAutomaticLogon();
                    break;
                case GETNETIPNOTFIND:
                    ips.add("http://113.6.252.165:8081/");
                    break;
            }
            UseInfoManager.putStringArraylist(LoginActivity.this, "NetIPs", ips);
        }
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
}
