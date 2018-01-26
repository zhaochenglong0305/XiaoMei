package fragment.release;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.EditReleaseMsgActivity;
import com.lit.xiaomei.MainActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentRelesaeInformationBinding;

import bean.GlobalVariable;
import bean.Information;
import bean.User;
import fragment.InformationFragment;
import manager.UseInfoManager;
import utils.CreateSendMsg;
import utils.FormatString;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReleaseInformationFragment extends BaseFragment<FragmentRelesaeInformationBinding> implements View.OnClickListener {
    private MainActivity mainActivity;
    private IntentFilter intentFilter;
    private ReceiveMsgReceiver receiveMsgReceiver;
    private User.ListDataBean bean = new User.ListDataBean();
    private String type = "货源";
    private final static int REQUESTCODE = 1; // 返回的结果码
    private String msg = "";

    public ReleaseInformationFragment() {
    }

    public static ReleaseInformationFragment newInstance() {
        ReleaseInformationFragment fragment = new ReleaseInformationFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_relesae_information;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        super.initView();
        intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalVariable.ReceiverAction.RELEASE_RESULT);
        receiveMsgReceiver = new ReceiveMsgReceiver();
        getContext().registerReceiver(receiveMsgReceiver, intentFilter);
        bean = UseInfoManager.getUser(getContext()).getListData().get(0);
        binding.tvPublishFrom.setText(bean.getCT());
        binding.rlMsgGoods.setOnClickListener(this);
        binding.rlMsgCars.setOnClickListener(this);
        binding.tvContent.setOnClickListener(this);
        binding.btnPublish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_msg_goods:
                initMsgGoodsType();
                break;
            case R.id.rl_msg_cars:
                initMsgCarsType();
                break;
            case R.id.tv_content:
                startActivityForResult(new Intent(getContext(), EditReleaseMsgActivity.class), REQUESTCODE);
                break;
            case R.id.btn_publish:
                if (TextUtils.isEmpty(msg)) {
                    showMessage("信息不能为空！");
                    return;
                }
                showLoading();
                String releseMsg = CreateSendMsg.createReleaseMsg(getContext(), type, binding.tvPublishFrom.getText().toString(), "", msg);
                mainActivity.sendMsgToSocket(releseMsg);
                break;
        }
    }

    private void initMsgGoodsType() {
        type = "货源";
        binding.ivMsgGoods.setVisibility(View.VISIBLE);
        binding.ivMsgCars.setVisibility(View.GONE);
    }

    private void initMsgCarsType() {
        type = "车源";
        binding.ivMsgGoods.setVisibility(View.GONE);
        binding.ivMsgCars.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(receiveMsgReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE) {
            if (data == null) {
                return;
            }
            String text = data.getStringExtra("Text");
            msg = text;
            binding.tvContent.setText(text);
        }
    }

    private class ReceiveMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("Msg");
            Log.e("long", "ReleaseInformationFragment获得数据：" + msg);
            if (msg.length() > 20) {
                hideLoading();
                showMessage("上报成功！");
                mainActivity.showInformationFragment();
            }
        }
    }
}
