package com.lit.xiaomei.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.fyjr.baselibrary.base.BaseActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.ActivityInvitationFriendBinding;
import com.lit.xiaomei.manager.UseInfoManager;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.SocializeUtils;

import okhttp3.internal.Util;

public class InvitationFriendActivity extends BaseActivity<ActivityInvitationFriendBinding> {
    private static final String APP_ID = "wx1e1aa0e79162f697";
    private IWXAPI api;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_invitation_friend);
    }

    @Override
    public void initView() {
        super.initView();
        dialog = new ProgressDialog(this);
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);
        setTitle("邀请好友");
        setTitleTextColor("#ffffff");
        binding.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUrl();
            }
        });
    }

    private void doShare() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://www.56770.net/reg/Register.htm?aa=" + UseInfoManager.getUser(this).getListData().get(0).getUS();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "网页标题";
        msg.description = "网页描述";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.tiger_small);
        msg.thumbData = com.lit.xiaomei.utils.Util.bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    private void shareUrl(){
        UMWeb web = new UMWeb("http://www.56770.net/reg/Register.htm?aa=" + UseInfoManager.getUser(this).getListData().get(0).getUS());
        web.setTitle("手机配货网");
        web.setThumb(new UMImage(this, R.mipmap.tiger_small));
        web.setDescription("手机配货网");
        new ShareAction(this).withMedia(web)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener).share();
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(InvitationFriendActivity.this,"成功了",Toast.LENGTH_LONG).show();
            SocializeUtils.safeCloseDialog(dialog);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(InvitationFriendActivity.this,"失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(InvitationFriendActivity.this,"取消了",Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
