package com.lit.xiaomei.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.fyjr.baselibrary.base.BaseActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.ActivityInvitationFriendBinding;
import com.lit.xiaomei.manager.UseInfoManager;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import okhttp3.internal.Util;

public class InvitationFriendActivity extends BaseActivity<ActivityInvitationFriendBinding> {
    private static final String APP_ID="wx1e1aa0e79162f697";
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_invitation_friend);
    }

    @Override
    public void initView() {
        super.initView();
        api = WXAPIFactory.createWXAPI(getApplicationContext(),APP_ID,true);
        api.registerApp(APP_ID);
        setTitle("邀请好友");
        setTitleTextColor("#ffffff");
        binding.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doShare();
            }
        });
    }

    private void doShare(){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl="http://www.56770.net/reg/Register.htm?aa="+ UseInfoManager.getUser(this).getListData().get(0).getUS();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title="网页标题";
        msg.description="网页描述";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(),R.mipmap.tiger);
        msg.thumbData = com.lit.xiaomei.utils.Util.bmpToByteArray(thumb,true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);

    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
