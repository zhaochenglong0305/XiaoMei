package com.lit.xiaomei.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.fyjr.baselibrary.base.BaseActivity;
import com.fyjr.baselibrary.http.HttpUtil;
import com.fyjr.baselibrary.http.callback.HttpCallBack;
import com.lit.xiaomei.R;
import com.lit.xiaomei.bean.User;
import com.lit.xiaomei.databinding.ActivityMineBinding;
import com.lit.xiaomei.manager.UseInfoManager;

public class MineActivity extends BaseActivity<ActivityMineBinding> {
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_mine);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("个人资料");
        setTitleTextColor("#ffffff");
        user = UseInfoManager.getUser(this);
        getUserInformation(user.getListData().get(0).getUS(),
                user.getListData().get(0).getPW(),
                user.getListData().get(0).getKY(), user.getListData().get(0).getTY());
    }

    private void getUserInformation(String NetID, String PWord, String key, String Type) {
        showLoading();
        HttpUtil.getInstance().getUserInformation(NetID, PWord, key, Type, new HttpCallBack<User>() {
            @Override
            public void onSuccess(User data, String msg) {
                hideLoading();
                User.ListDataBean listDataBean = data.getListData().get(0);
                if (TextUtils.equals("1", listDataBean.getRZ())) {
                    binding.tvUsername.setText(listDataBean.getUS());
                    binding.tvServiceTime.setText(listDataBean.getBD() + "  至  " + listDataBean.getED());
                    binding.tvStationName.setText(listDataBean.getCP());
                    binding.tvAddress.setText(listDataBean.getAD());
                    binding.tvCity.setText(listDataBean.getPR() + "   " + listDataBean.getCT());
                    binding.tvPhone.setText(listDataBean.getPH());
                }

            }

            @Override
            public void onFail(int errorCode, String msg) {
                hideLoading();
                showMessage("获取失败！");
            }
        });
    }
}
