package com.lit.xiaomei.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.fyjr.baselibrary.base.BaseActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.bean.Constants;
import com.lit.xiaomei.bean.GlobalVariable;
import com.lit.xiaomei.databinding.ActivitySteupBinding;
import com.lit.xiaomei.manager.UseInfoManager;

public class SteupActivity extends BaseActivity<ActivitySteupBinding> implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_steup);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("设置中心");
        setTitleTextColor("#ffffff");
        binding.rlAutoLogin.setOnClickListener(this);
        binding.rlIsxsdh.setOnClickListener(this);
        binding.rlIsbigtext.setOnClickListener(this);
        binding.rlMsgSong.setOnClickListener(this);
        binding.rlShowPhonenumber.setOnClickListener(this);

        if (UseInfoManager.getBoolean(this, Constants.Tag.autoLogin, false)) {
            binding.autoLogin.setChecked(true);
        } else {
            binding.autoLogin.setChecked(false);
        }
        binding.autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    UseInfoManager.putBoolean(SteupActivity.this, Constants.Tag.autoLogin, b);
                } else {
                    UseInfoManager.putBoolean(SteupActivity.this, Constants.Tag.autoLogin, b);
                }
            }
        });
        if (UseInfoManager.getBoolean(this, Constants.Tag.DIANHUA, false)) {
            binding.isxsdh.setChecked(true);
        } else {
            binding.isxsdh.setChecked(false);
        }
        binding.isxsdh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    UseInfoManager.putBoolean(SteupActivity.this, Constants.Tag.DIANHUA, b);
                } else {
                    UseInfoManager.putBoolean(SteupActivity.this, Constants.Tag.DIANHUA, b);
                }
            }
        });

        if (UseInfoManager.getBoolean(this, Constants.Tag.BIGTEXT, false)) {
            binding.isbigtext.setChecked(true);
        } else {
            binding.isbigtext.setChecked(false);
        }
        binding.isbigtext.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    UseInfoManager.putBoolean(SteupActivity.this, Constants.Tag.BIGTEXT, b);
                } else {
                    UseInfoManager.putBoolean(SteupActivity.this, Constants.Tag.BIGTEXT, b);
                }
            }
        });

        if (UseInfoManager.getBoolean(this, Constants.Tag.SONG, false)) {
            binding.msgsong.setChecked(true);
        } else {
            binding.msgsong.setChecked(false);
        }

        binding.msgsong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    UseInfoManager.putBoolean(SteupActivity.this, Constants.Tag.SONG, b);
                } else {
                    UseInfoManager.putBoolean(SteupActivity.this, Constants.Tag.SONG, b);
                }
            }
        });
        if (UseInfoManager.getBoolean(this, "showPhoneNumber", false)) {
            binding.cbShowPhonenumber.setChecked(true);
        } else {
            binding.cbShowPhonenumber.setChecked(false);
        }

        binding.cbShowPhonenumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    UseInfoManager.putBoolean(SteupActivity.this, "showPhoneNumber", b);
                } else {
                    UseInfoManager.putBoolean(SteupActivity.this, "showPhoneNumber", b);
                }
                sendBroadcast(new Intent(GlobalVariable.ReceiverAction.UPDATE_INFORMATION));
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_autoLogin:
                if (binding.autoLogin.isChecked()) {
                    binding.autoLogin.setChecked(false);
                } else {
                    binding.autoLogin.setChecked(true);
                }
                break;
            case R.id.rl_isxsdh:
                if (binding.isxsdh.isChecked()) {
                    binding.isxsdh.setChecked(false);
                } else {
                    binding.isxsdh.setChecked(true);
                }
                break;
            case R.id.rl_isbigtext:
                if (binding.isbigtext.isChecked()) {
                    binding.isbigtext.setChecked(false);
                } else {
                    binding.isbigtext.setChecked(true);
                }
                break;
            case R.id.rl_MsgSong:
                if (binding.msgsong.isChecked()) {
                    binding.msgsong.setChecked(false);
                } else {
                    binding.msgsong.setChecked(true);
                }
                break;
            case R.id.rl_show_phonenumber:
                if (binding.cbShowPhonenumber.isChecked()) {
                    binding.cbShowPhonenumber.setChecked(false);
                } else {
                    binding.cbShowPhonenumber.setChecked(true);
                }
                break;
        }

    }
}
