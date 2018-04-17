package com.lit.xiaomei.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.blanke.xsocket.tcp.client.TcpConnConfig;
import com.blanke.xsocket.tcp.client.XTcpClient;
import com.blanke.xsocket.tcp.client.bean.TargetInfo;
import com.blanke.xsocket.tcp.client.bean.TcpMsg;
import com.blanke.xsocket.tcp.client.helper.stickpackage.AbsStickPackageHelper;
import com.blanke.xsocket.tcp.client.helper.stickpackage.BaseStickPackageHelper;
import com.blanke.xsocket.tcp.client.listener.TcpClientListener;
import com.fyjr.baselibrary.base.BaseActivity;
import com.fyjr.baselibrary.http.url.HttpUrl;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import com.lit.xiaomei.bean.GlobalVariable;
import com.lit.xiaomei.bean.User;
import com.lit.xiaomei.fragment.InformationFragment;
import com.lit.xiaomei.fragment.MineFragment;
import com.lit.xiaomei.fragment.ReleaseFragment;
import com.lit.xiaomei.fragment.ServiceFragment;
import com.lit.xiaomei.fragment.TubeCarFragment;
import com.lit.xiaomei.manager.UseInfoManager;
import com.lit.xiaomei.utils.CreateSendMsg;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements RadioGroup.OnCheckedChangeListener, TcpClientListener, View.OnClickListener {
    private List<Fragment> fragments;
    private TubeCarFragment tubeCarFragment;
    private ReleaseFragment releaseFragment;
    private InformationFragment informationFragment;
    private ServiceFragment serviceFragment;
    private MineFragment mineFragment;
    private int currentIndex = 2;
    private int oldIndex = 2;
    private XTcpClient xTcpClient;
    private String requestSave = "";
    private long time = 0;
    private boolean isClientFaile = false;
    private User.ListDataBean listDataBean = new User.ListDataBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_main);
        initSocket();
    }

    @Override
    public void initView() {
        super.initView();
        listDataBean = UseInfoManager.getUser(this).getListData().get(0);
        initFragments();
        binding.bottomBarRg.setOnCheckedChangeListener(this);
        binding.rlInformation.setOnClickListener(this);
    }

    private void initFragments() {
        tubeCarFragment = TubeCarFragment.newInstance();
        releaseFragment = ReleaseFragment.newInstance();
        informationFragment = InformationFragment.newInstance();
        serviceFragment = ServiceFragment.newInstance();
        mineFragment = MineFragment.newInstance();
        fragments = new ArrayList<>();
        fragments.add(releaseFragment);
        fragments.add(tubeCarFragment);
        fragments.add(informationFragment);
        fragments.add(serviceFragment);
        fragments.add(mineFragment);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, releaseFragment)
                .add(R.id.frame_layout, informationFragment)
                .show(informationFragment)
                .commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_release:
                currentIndex = 0;
                initInformation();
                break;
            case R.id.rb_tube_car:
                currentIndex = 1;
                initInformation();
                break;
            case R.id.rb_information:
                currentIndex = 2;
                selectInformation();
                break;
            case R.id.rb_service:
                currentIndex = 3;
                initInformation();
                break;
            case R.id.rb_mine:
                currentIndex = 4;
                initInformation();
                break;
        }
        showCurrentFragment(currentIndex);
    }

    /**
     * 展示当前选中的Fragment
     *
     * @param currentIndex
     */
    private void showCurrentFragment(int currentIndex) {
        if (currentIndex != oldIndex) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(fragments.get(oldIndex));
            if (!fragments.get(currentIndex).isAdded()) {
                ft.add(R.id.frame_layout, fragments.get(currentIndex));
            }
            ft.show(fragments.get(currentIndex)).commit();
            oldIndex = currentIndex;
        }
    }

    private void initSocket() {
        String ip = HttpUrl.BASE_URL.substring(HttpUrl.BASE_URL.indexOf("//") + 2, HttpUrl.BASE_URL.indexOf(":8081"));
        TargetInfo targetInfo = new TargetInfo(ip, 7600);
        xTcpClient = XTcpClient.getTcpClient(targetInfo);
        xTcpClient.addTcpClientListener(this);
        String start = "";
        String end = "|";
//        AbsStickPackageHelper stickHelper = new SpecifiedStickPackageHelper(start.getBytes(), end.getBytes());
        AbsStickPackageHelper stickHelper = new BaseStickPackageHelper();
        xTcpClient.config(new TcpConnConfig.Builder()
                .setStickPackageHelper(stickHelper)//粘包
                .setIsReconnect(true)
                .create());
        if (xTcpClient.isDisconnected()) {
            xTcpClient.connect();
        } else {
            Log.e("long", "已经存在该连接");
        }
    }

    @Override
    public void onBackClick() {
        if (time == 0 || System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            showMessage("再次点击返回键退出");
        } else if (System.currentTimeMillis() - time < 2000) {
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (xTcpClient != null) {
            xTcpClient.removeTcpClientListener(this);
            xTcpClient.disconnect();//activity销毁时断开tcp连接
        }
    }

    @Override
    public void onConnected(XTcpClient client) {
        Log.e("long", "连接成功");
        HeartBeat();
        if (isClientFaile) {
            isClientFaile = false;
            String informationMsg = CreateSendMsg.createInformationMsg(this, listDataBean.getPR(), listDataBean.getCT());
            xTcpClient.sendMsg(informationMsg);
        }
    }

    @Override
    public void onSended(XTcpClient client, TcpMsg tcpMsg) {
        Log.e("long", "发送数据：" + tcpMsg.getSourceDataString());
    }

    @Override
    public void onDisconnected(XTcpClient client, String msg, Exception e) {
        Log.e("long", client.getTargetInfo().getIp() + "断开连接 " + msg + e);
        isClientFaile = true;
    }

    @Override
    public void onReceive(XTcpClient client, TcpMsg tcpMsg) {
        byte[][] res = tcpMsg.getEndDecodeData();
        byte[] bytes = new byte[0];
        for (byte[] i : res) {
            bytes = i;
            break;
        }
        String re = "";
        if (tcpMsg.getSourceDataString().contains("Qkc=") && (tcpMsg.getSourceDataString().substring(tcpMsg.getSourceDataString().length() - 1).equals("|"))) {
            re = tcpMsg.getSourceDataString();
        } else if (tcpMsg.getSourceDataString().contains("Qkc=") && (!tcpMsg.getSourceDataString().substring(tcpMsg.getSourceDataString().length() - 1).equals("|"))) {
            requestSave += tcpMsg.getSourceDataString();
            return;
        } else if ((!tcpMsg.getSourceDataString().contains("Qkc=")) && (!tcpMsg.getSourceDataString().substring(tcpMsg.getSourceDataString().length() - 1).equals("|"))) {
            requestSave += tcpMsg.getSourceDataString();
            return;
        } else if ((!tcpMsg.getSourceDataString().contains("Qkc=")) && (tcpMsg.getSourceDataString().substring(tcpMsg.getSourceDataString().length() - 1).equals("|"))) {
            requestSave += tcpMsg.getSourceDataString();
            re = requestSave;
            requestSave = "";
        }
        String receive = "";
        Log.e("long", "获得数据：解密前===" + tcpMsg.getSourceDataString());
        try {
            receive = new String(Base64.decode(re, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("long", "获得数据：解密后===" + receive);
        if (receive.contains("DATA")) {
            sendBroadcast(new Intent(GlobalVariable.ReceiverAction.REAL_TIME_MSG).putExtra("Msg", receive));
        }
        if (receive.contains("SJSB")) {
            sendBroadcast(new Intent(GlobalVariable.ReceiverAction.RELEASE_RESULT).putExtra("Msg", receive));
        }

    }

    @Override
    public void onValidationFail(XTcpClient client, TcpMsg tcpMsg) {

    }

    public void sendMsgToSocket(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        xTcpClient.sendMsg(msg);
    }

    public void showInformationFragment() {
        binding.bottomBarRg.check(R.id.rb_information);
    }

    private void HeartBeat() {
        final String heart = CreateSendMsg.createHeartBeatMsg(this);
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (!isClientFaile) {
                        xTcpClient.sendMsg(heart);
                        try {
                            sleep(30000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }.start();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_information:
                binding.bottomBarRg.check(R.id.rb_information);
                break;
        }
    }

    private void initInformation() {
        binding.ivInformation.setImageResource(R.mipmap.main_find_goods_normal);
        binding.tvInformation.setTextColor(getResources().getColor(R.color.c595a6e));
    }

    private void selectInformation() {
        binding.ivInformation.setImageResource(R.mipmap.main_find_goods_select);
        binding.tvInformation.setTextColor(getResources().getColor(R.color.cFD933C));
    }
}
