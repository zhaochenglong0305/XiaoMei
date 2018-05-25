package com.lit.xiaomei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import com.fyjr.baselibrary.base.BaseActivity;
import com.fyjr.baselibrary.http.HttpUtil;
import com.fyjr.baselibrary.http.callback.HttpCallBack;
import com.lit.xiaomei.R;
import com.lit.xiaomei.bean.SimpleBean;
import com.lit.xiaomei.databinding.ActivityUpdatePasswordBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class UpdatePasswordActivity extends BaseActivity<ActivityUpdatePasswordBinding> implements View.OnClickListener {
    private String user = "", yzm = "", pwd = "", pwd2 = "";
    private boolean isFirst = true;
    private MyCountDownTimer myCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_update_password);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("修改密码");
        setTitleTextColor("#ffffff");
        myCountDownTimer = new MyCountDownTimer(60000, 1000);
        binding.btnGetYzm.setOnClickListener(this);
        binding.btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_yzm:
                user = binding.etUser.getText().toString();
                if (TextUtils.isEmpty(user)) {
                    showMessage("手机号不能为空！");
                    return;
                }
                if (!isChinaPhoneLegal(user)) {
                    showMessage("请输入正确的手机号！");
                    return;
                }
                getYZM(user);
                break;
            case R.id.btn_ok:
                if (isFirst) {
                    user = binding.etUser.getText().toString();
                    yzm = binding.etYzm.getText().toString();
                    if (TextUtils.isEmpty(user)) {
                        showMessage("手机号不能为空！");
                        return;
                    }
                    if (TextUtils.isEmpty(yzm)) {
                        showMessage("验证码不能为空！");
                        return;
                    }
                    checkYZM(user, yzm);
                } else {
                    pwd = binding.etPwd.getText().toString();
                    pwd2 = binding.etPwd2.getText().toString();
                    if (TextUtils.isEmpty(pwd)) {
                        showMessage("密码不能为空！");
                        return;
                    }
                    if (TextUtils.isEmpty(pwd2)) {
                        showMessage("密码不能为空！");
                        return;
                    }
                    if (!TextUtils.equals(pwd, pwd2)) {
                        showMessage("密码不一致！");
                        return;
                    }
                    doFinish(user, pwd);
                }
                break;
        }

    }

    private void doFinish(final String user, final String pwd) {
        showLoading();
        HttpUtil.getInstance().updatePassword(user, pwd, getANDROID_ID(), new HttpCallBack<SimpleBean>() {
            @Override
            public void onSuccess(SimpleBean data, String msg) {
                hideLoading();
                showMessage("修改成功！");
                Intent intent = new Intent();
                intent.putExtra("password", pwd);
                setResult(102, intent);
                finish();
            }

            @Override
            public void onFail(int errorCode, String msg) {
                hideLoading();
                showMessage("修改失败！");
            }
        });
    }

    private void checkYZM(String user, String yzm) {
        showLoading();
        HttpUtil.getInstance().checkYZM(user, yzm, new HttpCallBack<SimpleBean>() {
            @Override
            public void onSuccess(SimpleBean data, String msg) {
                isFirst = false;
                binding.llYzm.setVisibility(View.GONE);
                binding.llPassword.setVisibility(View.VISIBLE);
                binding.btnOk.setText("完成");
                hideLoading();
            }

            @Override
            public void onFail(int errorCode, String msg) {
                showMessage("验证码错误！");
                hideLoading();
            }
        });

    }

    private void getYZM(String user) {
        showLoading();
        HttpUtil.getInstance().getYZM(user, new HttpCallBack<SimpleBean>() {
            @Override
            public void onSuccess(SimpleBean data, String msg) {
                showMessage("验证码发送成功！");
                hideLoading();
                myCountDownTimer.start();
            }

            @Override
            public void onFail(int errorCode, String msg) {
                hideLoading();
                showMessage("验证码发送失败！");
            }
        });
    }


    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(166)|(17[0-8])|(18[0-9])|(19[8-9])|(147,145))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    // 复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        // 计时过程
        @Override
        public void onTick(long l) {
            // 防止计时过程中重复点击
            binding.btnGetYzm.setClickable(false);
            binding.btnGetYzm.setText(l / 1000 + "s");

        }

        // 计时完毕的方法
        @Override
        public void onFinish() {
            // 重新给Button设置文字
            binding.btnGetYzm.setText("获取验证码");
            // 设置可点击
            binding.btnGetYzm.setClickable(true);
        }
    }

    public String getANDROID_ID() {
        return Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
    }
}
