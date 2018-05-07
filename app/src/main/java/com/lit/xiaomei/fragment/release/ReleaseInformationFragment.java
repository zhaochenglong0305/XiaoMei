package com.lit.xiaomei.fragment.release;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.fyjr.baselibrary.base.BaseFragment;
import com.fyjr.baselibrary.utils.TimeUtil;
import com.lit.xiaomei.activity.MainActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentRelesaeInformationBinding;

import java.util.ArrayList;
import java.util.HashMap;

import com.lit.xiaomei.bean.GlobalVariable;
import com.lit.xiaomei.bean.ReleaseHistory;
import com.lit.xiaomei.bean.User;
import com.lit.xiaomei.manager.LocationManager;
import com.lit.xiaomei.manager.UseInfoManager;
import com.lit.xiaomei.utils.CreateSendMsg;
import com.lit.xiaomei.view.DialogCarLongType;
import com.lit.xiaomei.view.DialogGoodType;
import com.lit.xiaomei.view.DialogReleaseSelectCity;

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
    private String danwei = "吨";
    private String danjia = "元/吨";
    private int informationType = 1;
    private int cityType = 1;
    private String from = "", to = "", che = "", huo = "", shu = "", money = "";
    private boolean isLd = false;
    private String publishMsg = "";
    private int againTime = 0;
    private int againNum = 0;
    private int[] times = {0, 3, 10, 15, 20, 25, 30};
    private int[] nums = {0, 3, 4, 5, 6, 7, 8, 9, 10, 20, 30};

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
        from = bean.getCT();
        againTime = times[0];
        againNum = nums[0];
        binding.tvPublishFrom.setText(from);
        binding.rlPublishFrom.setOnClickListener(this);
        binding.rlPublishTo.setOnClickListener(this);
        binding.rlMsgGoods.setOnClickListener(this);
        binding.rlMsgCars.setOnClickListener(this);
        binding.btnPublish.setOnClickListener(this);
        binding.rlCarLongType.setOnClickListener(this);
        binding.rlGoodType.setOnClickListener(this);
        binding.etGoodsNum.addTextChangedListener(new GoodNumEditChangeListener());
        binding.etGoodsMoney.addTextChangedListener(new FreightEditChangeListener());
        binding.sAgainTime.setOnItemSelectedListener(new OnAgainTimeItemSelectedListener());
        binding.sAgainNum.setOnItemSelectedListener(new OnAgainNumItemSelectedListener());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_msg_goods:
                initMsgGoodsType();
                setEdtext(from, to, che, isLd, huo, shu, money);
                break;
            case R.id.rl_msg_cars:
                initMsgCarsType();
                setEdtext(from, to, che, isLd, huo, shu, money);
                break;
            case R.id.btn_publish:
                publishMsg = binding.etContent.getText().toString();
                if (TextUtils.isEmpty(publishMsg)) {
                    showMessage("信息不能为空！");
                    return;
                }
                showLoading();
                String releseMsg = CreateSendMsg.createReleaseMsg(getContext(), type, binding.tvPublishFrom.getText().toString(), "", publishMsg);
                mainActivity.sendMsgToSocket(releseMsg);
                break;
            case R.id.rl_publish_from:
                cityType = 1;
                showCityDialog();
                break;
            case R.id.rl_publish_to:
                cityType = 2;
                showCityDialog();
                break;
            case R.id.rl_car_long_type:
                showAttributeDialog();
                break;
            case R.id.rl_good_type:
                showGoodTypeDialog();
                break;
        }
    }

    private void initMsgGoodsType() {
        informationType = 1;
        type = "货源";
        binding.ivMsgGoods.setVisibility(View.VISIBLE);
        binding.ivMsgCars.setVisibility(View.GONE);
    }

    private void initMsgCarsType() {
        informationType = 2;
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
            binding.etContent.setText(text);
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
                ArrayList<ReleaseHistory> releaseHistories = new ArrayList<>();
                if (UseInfoManager.getReleseaeHistoryArraylist(getContext()) != null) {
                    releaseHistories = UseInfoManager.getReleseaeHistoryArraylist(getContext());
                }
                releaseHistories.add(new ReleaseHistory(publishMsg, TimeUtil.currentTimeMillis(), againTime, againNum, type, binding.tvPublishFrom.getText().toString()));
                UseInfoManager.putReleseaeHistoryArraylist(getContext(), releaseHistories);
                getContext().sendBroadcast(new Intent(GlobalVariable.ReceiverAction.UPDATE_HISTORY));
//                initRelease();
                mainActivity.showInformationFragment();
            } else {
                hideLoading();
                showMessage("网络异常！");
            }
        }
    }

    private void showCityDialog() {
        DialogReleaseSelectCity dialogReleaseSelectCity = new DialogReleaseSelectCity(getActivity());
        dialogReleaseSelectCity.setSelectCityListener(new DialogReleaseSelectCity.OnCitySeectListener() {
            @Override
            public void onClick(String select) {
                switch (cityType) {
                    case 1:
                        binding.tvPublishFrom.setText(select);
                        from = select;
                        setEdtext(from, to, che, isLd, huo, shu, money);
                        break;
                    case 2:
                        binding.tvPublishTo.setText(select);
                        binding.tvPublishTo.setTextColor(getResources().getColor(R.color.c595a6e));
                        to = select;
                        setEdtext(from, to, che, isLd, huo, shu, money);
                        break;
                }
            }
        });
        dialogReleaseSelectCity.showAtLocation(binding.llReleaseMain, Gravity.CENTER, 0, 0);
    }

    private void showAttributeDialog() {
        DialogCarLongType carLongType = new DialogCarLongType(getActivity());
        carLongType.showAtLocation(binding.llReleaseMain, Gravity.CENTER, 0, 0);
        carLongType.setOnCarFinishListener(new DialogCarLongType.OnCarFinishListener() {
            @Override
            public void onClick(boolean isLD, String carlLong, String carType) {
                if (TextUtils.isEmpty(carType)) {
                    che = carlLong + carType;
                } else {
                    che = carlLong + "，" + carType;
                }
                binding.tvCarLongType.setText(che);
                setEdtext(from, to, che, isLd, huo, shu, money);
            }
        });
    }

    private void showGoodTypeDialog() {
        DialogGoodType goodType = new DialogGoodType(getActivity());
        goodType.showAtLocation(binding.llReleaseMain, Gravity.CENTER, 0, 0);
        goodType.setOnGoodTypeListener(new DialogGoodType.OnGoodTypeListener() {
            @Override
            public void onClick(String goodType) {
                if (!TextUtils.isEmpty(goodType)) {
                    huo = goodType;
                    binding.tvGoodType.setText(huo);
                    setEdtext(from, to, che, isLd, huo, shu, money);
                }
            }
        });
    }

    private void setEdtext(String from, String to, String che, boolean isLd, String huo, String shu, String money) {
        String result = "";
        if (TextUtils.isEmpty(to)) {
            to = "出发";
        } else {
            to = "->" + to;
        }
        String cheR = "";
        if (TextUtils.isEmpty(che)) {
            if (informationType == 1) {
                cheR = "，求车";
            } else {
                cheR = "，有车";
            }
        } else {
            if (informationType == 1) {
                cheR = "，求" + che + "车";
            } else {
                cheR = "，有" + che + "车";
            }
        }
        String lz = "";
        if (isLd) {
            lz = "，零担";
        }
        if (TextUtils.isEmpty(huo)) {
            if (informationType == 1) {
                huo = "，有货";
            } else {
                huo = "，求货";
            }
        } else {
            if (informationType == 1) {
                huo = "，有" + huo;
            } else {
                huo = "，求" + huo;
            }
        }
        if (TextUtils.isEmpty(shu)) {
            shu = "";
        } else {
            shu = shu + danwei + "，";
        }
        if (TextUtils.isEmpty(money)) {
            money = "";
        } else {
            money = money + danjia;
        }
        if (informationType == 1) {
            result = from + to + lz + huo + shu + money + cheR;
        } else {
            result = from + to + lz + cheR + huo + shu + money;
        }
        binding.etContent.setText(result);
    }

    private class GoodNumEditChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            shu = binding.etGoodsNum.getText().toString();
            setEdtext(from, to, che, isLd, huo, shu, money);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private class FreightEditChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            money = binding.etGoodsMoney.getText().toString();
            setEdtext(from, to, che, isLd, huo, shu, money);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private class OnAgainTimeItemSelectedListener implements OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            againTime = times[i];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            againTime = times[0];
        }
    }

    private class OnAgainNumItemSelectedListener implements OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            againNum = nums[i];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            againNum = nums[0];
        }
    }

    private void initRelease() {
        from = bean.getCT();
        binding.tvPublishFrom.setText(from);
        to = "";
        binding.tvPublishTo.setText("请选择到达地");
        binding.tvPublishTo.setTextColor(getResources().getColor(R.color.cc1c1c1));
        initMsgGoodsType();
        isLd = false;
        che = "";
        binding.tvCarLongType.setText("请选择车长车型");
        huo = "";
        binding.tvGoodType.setText("请选择货物类型");
        shu = "";
        binding.etGoodsNum.setText("");
        money = "";
        binding.etGoodsMoney.setText("");
        publishMsg = "";
        binding.etContent.setText("");
        againTime = times[0];
        binding.sAgainTime.setGravity(0);
        againNum = nums[0];
        binding.sAgainNum.setGravity(0);
    }

}
