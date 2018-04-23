package com.lit.xiaomei.activity;

import android.os.Bundle;
import android.app.Activity;

import com.fyjr.baselibrary.base.BaseActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.ActivityInvitationFriendBinding;

public class InvitationFriendActivity extends BaseActivity<ActivityInvitationFriendBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_invitation_friend);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("邀请好友");
        setTitleTextColor("#ffffff");
    }
}
