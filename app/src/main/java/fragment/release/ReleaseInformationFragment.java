package fragment.release;


import android.content.BroadcastReceiver;
import android.content.Context;
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

import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.EditReleaseMsgActivity;
import com.lit.xiaomei.MainActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentRelesaeInformationBinding;

import bean.GlobalVariable;
import bean.User;
import manager.UseInfoManager;
import utils.CreateSendMsg;
import view.DialogCarLongType;
import view.DialogGoodType;
import view.DialogReleaseSelectCity;

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
    private String danwei = "吨";
    private String danjia = "元/吨";
    private int informationType = 1;
    private int cityType = 1;
    private String from = "", to = "", che = "", huo = "", shu = "", money = "";
    private boolean isLd = false;

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
        binding.rlPublishFrom.setOnClickListener(this);
        binding.rlPublishTo.setOnClickListener(this);
        binding.rlMsgGoods.setOnClickListener(this);
        binding.rlMsgCars.setOnClickListener(this);
        binding.tvContent.setOnClickListener(this);
        binding.btnPublish.setOnClickListener(this);
        binding.rlCarLongType.setOnClickListener(this);
        binding.rlGoodType.setOnClickListener(this);
        binding.etGoodsNum.addTextChangedListener(new GoodNumEditChangeListener());
        binding.etGoodsMoney.addTextChangedListener(new FreightEditChangeListener());
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

    private void showCityDialog() {
        DialogReleaseSelectCity dialogReleaseSelectCity = new DialogReleaseSelectCity(getActivity());
        dialogReleaseSelectCity.showAtLocation(binding.llReleaseMain, Gravity.CENTER, 0, 0);
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
                        to = select;
                        setEdtext(from, to, che, isLd, huo, shu, money);
                        break;
                }
            }
        });
    }

    private void showAttributeDialog() {
        DialogCarLongType carLongType = new DialogCarLongType(getActivity());
        carLongType.showAtLocation(binding.llReleaseMain, Gravity.CENTER, 0, 0);
        carLongType.setOnCarFinishListener(new DialogCarLongType.OnCarFinishListener() {
            @Override
            public void onClick(boolean isLD, String carlLong, String carType) {
                che = carlLong + carType;
                if (TextUtils.isEmpty(che)) {
                    che = "不限车型，不限车长";
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
                huo = goodType;
                binding.tvGoodType.setText(huo);
                setEdtext(from, to, che, isLd, huo, shu, money);
            }
        });
    }

    private void setEdtext(String from, String to, String che, boolean isLd, String huo, String shu, String money) {
        String result = "";
        if (TextUtils.isEmpty(to)) {
            to = "";
        } else {
            to = "->" + to;
        }
        if (TextUtils.isEmpty(che)) {
            if (informationType == 1) {
                che = "，求车";
            } else {
                che = "，有车";
            }
        } else {
            if (informationType == 1) {
                che = "，求" + che + "车";
            } else {
                che = "，有" + che + "车";
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
            result = from + to + lz + huo + shu + money + che;
        } else {
            result = from + to + lz + che + huo + shu + money;
        }
        msg = result;
        binding.tvContent.setText(msg);
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
}
