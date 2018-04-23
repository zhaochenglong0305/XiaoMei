package com.lit.xiaomei.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fyjr.baselibrary.base.BaseActivity;
import com.fyjr.baselibrary.http.HttpUtil;
import com.fyjr.baselibrary.http.callback.HttpCallBack;
import com.lit.xiaomei.R;
import com.lit.xiaomei.bean.CheckAuthority;
import com.lit.xiaomei.bean.User;
import com.lit.xiaomei.databinding.ActivityInformationDetailsBinding;

import com.lit.xiaomei.bean.Information;
import com.lit.xiaomei.manager.UseInfoManager;

public class InformationDetailsActivity extends BaseActivity<ActivityInformationDetailsBinding> implements AdapterView.OnItemClickListener {
    private Information.SearchINFOBean searchINFOBean = new Information.SearchINFOBean();
    private String[] phones = {};
    private ListPhonesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_information_details);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("详细信息");
        setTitleTextColor("#ffffff");
        if (getIntent().getSerializableExtra("Details") != null) {
            searchINFOBean = (Information.SearchINFOBean) getIntent().getSerializableExtra("Details");
        }
        if (!TextUtils.isEmpty(searchINFOBean.getPH())) {
            String phoneString = searchINFOBean.getPH();
            if (phoneString.contains("，")) {
                phones = phoneString.split("，");

            } else if (phoneString.contains(",")) {
                phones = phoneString.split(",");
            } else if (phoneString.contains(" ")) {
                phones = phoneString.split(" ");
            } else {
                phones = new String[]{phoneString};
            }
        }
        binding.tvDetailsFrom.setText(searchINFOBean.getSF());
        binding.tvDetailsTo.setText(searchINFOBean.getMD());
        binding.tvDetailsType.setText(searchINFOBean.getCH());
        binding.tvDetailsContext.setText(searchINFOBean.getMS());
        binding.tvStationName.setText(searchINFOBean.getNA());
        binding.tvDetailsTime.setText(searchINFOBean.getDT());
        adapter = new ListPhonesAdapter();
        binding.lvDetailsPhones.setAdapter(adapter);
        binding.lvDetailsPhones.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phones[position]));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public class ListPhonesAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return phones.length;
        }

        @Override
        public Object getItem(int position) {
            return phones[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(InformationDetailsActivity.this).inflate(R.layout.adapter_details_phones, parent, false);
            TextView phoneNum = (TextView) convertView.findViewById(R.id.tv_phone_num);
            phoneNum.setText(phones[position]);
            return convertView;
        }
    }

}
