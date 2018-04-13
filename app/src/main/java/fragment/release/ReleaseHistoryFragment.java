package fragment.release;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fyjr.baselibrary.base.BaseFragment;
import com.fyjr.baselibrary.utils.TimeUtil;
import com.lit.xiaomei.MainActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentReleaseHistoryBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;

import bean.GlobalVariable;
import bean.ReleaseHistory;
import manager.UseInfoManager;
import utils.CreateSendMsg;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReleaseHistoryFragment extends BaseFragment<FragmentReleaseHistoryBinding> {
    private ArrayList<ReleaseHistory> releaseHistories = new ArrayList<>();
    private MainActivity mainActivity;
    private ReceiveMsgReceiver receiveMsgReceiver;
    private IntentFilter intentFilter;
    private HistoryListAdapter adapter = null;


    public ReleaseHistoryFragment() {
        // Required empty public constructor
    }

    public static ReleaseHistoryFragment newInstance() {
        ReleaseHistoryFragment fragment = new ReleaseHistoryFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UseInfoManager.getReleseaeHistoryArraylist(getContext()) != null) {
            releaseHistories = UseInfoManager.getReleseaeHistoryArraylist(getContext());
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_release_history;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(receiveMsgReceiver);
    }

    @Override
    public void initView() {
        super.initView();
        if (UseInfoManager.getReleseaeHistoryArraylist(getContext()) != null) {
            releaseHistories = UseInfoManager.getReleseaeHistoryArraylist(getContext());
        }
        intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalVariable.ReceiverAction.RELEASE_RESULT);
        receiveMsgReceiver = new ReceiveMsgReceiver();
        getContext().registerReceiver(receiveMsgReceiver, intentFilter);
        adapter = new HistoryListAdapter();
        binding.lvReleaseHistory.setAdapter(adapter);
    }

    private class HistoryListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return releaseHistories.size();
        }

        @Override
        public Object getItem(int position) {
            return releaseHistories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_release_history, parent, false);
            TextView historyContext = (TextView) view.findViewById(R.id.tv_history_context);
            TextView historyTime = (TextView) view.findViewById(R.id.tv_time);
            Button again = (Button) view.findViewById(R.id.btn_again);
            Button delete = (Button) view.findViewById(R.id.btn_delete);
            final ReleaseHistory releaseHistory = releaseHistories.get(position);
            historyContext.setText(releaseHistory.getReleaseContext());
            historyTime.setText(TimeUtil.getTimeFormatText(releaseHistory.getTime()));
            again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoading();
                    String releseMsg = CreateSendMsg.createReleaseMsg(getContext(), releaseHistory.getReleaseType(),
                            releaseHistory.getFrom(), "", releaseHistory.getReleaseContext());
                    mainActivity.sendMsgToSocket(releseMsg);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    releaseHistories.remove(releaseHistory);
                    UseInfoManager.putReleseaeHistoryArraylist(getContext(), releaseHistories);
                    notifyDataSetChanged();
                }
            });

            return view;
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
            } else {
                hideLoading();
                showMessage("网络异常！");
            }
        }
    }
}
