package com.lit.xiaomei.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;

import com.fyjr.baselibrary.base.BaseActivity;
import com.fyjr.baselibrary.http.HttpUtil;
import com.fyjr.baselibrary.http.callback.HttpCallBack;
import com.lit.xiaomei.R;
import com.lit.xiaomei.bean.SimpleBean;
import com.lit.xiaomei.databinding.ActivityRegistBinding;
import com.lit.xiaomei.view.DialogRegistSelectCity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistActivity extends BaseActivity<ActivityRegistBinding> implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private MyCountDownTimer myCountDownTimer;
    private String use = "";
    private String yzm = "";
    private String pwd = "";
    private String pwd2 = "";
    private String province = "";
    private String city = "";
    private String tjrNum = "008";
    private String userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_regist);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("注册");
        setTitleTextColor("#ffffff");
        myCountDownTimer = new MyCountDownTimer(60000, 1000);
        binding.tvCity.setOnClickListener(this);
        binding.btnGetYzm.setOnClickListener(this);
        binding.btnRegist.setOnClickListener(this);
        binding.rgUserType.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_yzm:
                use = binding.etUser.getText().toString();
                if (TextUtils.isEmpty(use)) {
                    showMessage("手机号码不能为空！");
                    return;
                }
                if (!isChinaPhoneLegal(use)) {
                    showMessage("请输入正确的手机号！");
                    return;
                }
                getYZM(use);
                break;
            case R.id.tv_city:
                showSelectCity();
                break;
            case R.id.btn_regist:
                use = binding.etUser.getText().toString();
                yzm = binding.etYzm.getText().toString();
                pwd = binding.etPwd.getText().toString();
                pwd2 = binding.etPwd2.getText().toString();
                tjrNum = binding.etTJRID.getText().toString();
                if (TextUtils.isEmpty(userType)) {
                    showMessage("请选择用户类型！");
                    return;
                }
                if (TextUtils.isEmpty(use)) {
                    showMessage("手机号码不能为空！");
                    return;
                }
                if (!isChinaPhoneLegal(use)) {
                    showMessage("请输入正确的手机号！");
                    return;
                }
                if (TextUtils.isEmpty(yzm)) {
                    showMessage("验证码不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    showMessage("密码不能为空！");
                    return;
                }
                if (pwd.length() < 6) {
                    showMessage("密码最少为6位数！");
                    return;
                }
                if (TextUtils.isEmpty(pwd2)) {
                    showMessage("密码不能为空！");
                    return;
                }
                if (!TextUtils.equals(pwd, pwd2)) {
                    showMessage("输入密码不一致！");
                    return;
                }
                if (TextUtils.isEmpty(city)) {
                    showMessage("请选择城市！");
                    return;
                }
                if (TextUtils.isEmpty(tjrNum)) {
                    showMessage("推荐人不能为空！");
                    return;
                }

                checkTJR(use, pwd, "", "", "", province, city, getANDROID_ID(), tjrNum, yzm, userType);
                break;
        }

    }

    private void getYZM(String phone) {
        showLoading();
        HttpUtil.getInstance().getYZM(phone, new HttpCallBack<SimpleBean>() {
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

    private void checkTJR(final String user, final String pwd, final String name, final String station, final String phone,
                          final String sheng, final String city, final String key, final String TJRID, final String yzm, final String ClassID) {
        showLoading();
        HttpUtil.getInstance().checkTJR(TJRID, sheng, city, new HttpCallBack<SimpleBean>() {
            @Override
            public void onSuccess(SimpleBean data, String msg) {
                doRegist(user, pwd, name, station, phone, sheng, city, key, TJRID, yzm, ClassID);
            }

            @Override
            public void onFail(int errorCode, String msg) {
                hideLoading();
                new AlertDialog.Builder(RegistActivity.this).setTitle("系统提示")
                        .setMessage(
                                "推荐人不存在！如忘记推荐人编号或从网站直接下载客户端，" +
                                        "请在推荐人选项中输入“008”，也可拨打客服电话：0417-3332535咨询。")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {// 添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {// 确定按钮的响应事件
                            }
                        }).show();
            }
        });
    }

    private void doRegist(final String user, final String pwd, String name, String station, String phone,
                          String sheng, final String city, String key, String TJRID, String yzm, String ClassID) {
        HttpUtil.getInstance().doRegist(user, pwd, name, station, phone,
                sheng, city, key, TJRID, yzm, ClassID, new HttpCallBack<SimpleBean>() {
                    @Override
                    public void onSuccess(SimpleBean data, String msg) {
                        showMessage(msg);
                        hideLoading();
                        Intent intent = new Intent();
                        intent.putExtra("userName", user);
                        intent.putExtra("password", pwd);
                        intent.putExtra("userCity", city);
                        setResult(RESULT_CODE, intent);
                        finish();
                    }

                    @Override
                    public void onFail(int errorCode, String msg) {
                        showMessage(msg);
                        hideLoading();
                    }
                });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_car:
                userType = "1";
                break;
            case R.id.rb_station:
                userType = "2";
                break;
        }
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

    private void showSelectCity() {
        DialogRegistSelectCity dialogRegistSelectCity = new DialogRegistSelectCity(this);
        dialogRegistSelectCity.setSelectCityListener(new DialogRegistSelectCity.OnCitySelectListener() {
            @Override
            public void onClick(String c, String p) {
                city = c;
                province = p;
                binding.tvCity.setText(c);
            }
        });
        dialogRegistSelectCity.showAtLocation(binding.llRegistMain, Gravity.CENTER, 0, 0);
    }

    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(166)|(17[0-8])|(18[0-9])|(19[8-9])|(147,145))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public String getANDROID_ID() {
        return Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
    }
}
