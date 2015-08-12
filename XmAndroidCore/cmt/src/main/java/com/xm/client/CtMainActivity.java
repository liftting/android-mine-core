package com.xm.client;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xm.client.apdata.UserManager;
import com.xm.client.ctui.CtListDataAdapter;
import com.xm.client.net.CtConfig;
import com.xm.client.net.CtSenderHelper;
import com.xm.client.net.ctdata.CtBaseDataBean;
import com.xm.client.net.ctdata.CtReceiveDataBean;
import com.xm.client.net.ctdata.CtSendDataBean;
import com.xm.client.net.CtExecutors;
import com.xm.client.net.ctserver.CtDataProtocol;
import com.xm.client.net.listener.CtPostMainDataListener;
import com.xm.client.net.listener.CtSendDataListener;
import com.xm.client.net.ui.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;


public class CtMainActivity extends Activity implements CtPostMainDataListener, CtSendDataListener {

    private boolean isClose = false;
    private ListView mPullListView;
    private EditText mEdInput;


    private List<CtBaseDataBean> datas = new ArrayList<CtBaseDataBean>();
    private CtListDataAdapter adapter;

    private SwipeRefreshLayout mSwipRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPullListView = (ListView) findViewById(R.id.list_ct_data_show);
        mSwipRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swip_ct_data_show);
        mEdInput = (EditText) findViewById(R.id.ed_ct_input_text);

        CtSenderHelper.getInstance().addPostMainListener(CtConfig.chat_main_listener, this);
        CtSenderHelper.getInstance().addSendDataListener(this);


        adapter = new CtListDataAdapter(this);
        adapter.setDataList(datas);
        mPullListView.setAdapter(adapter);

        mSwipRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


            }
        });


    }

    public void send(View v) {
        CtSendDataBean bean = new CtSendDataBean();
        bean.setMessage(mEdInput.getText().toString());
        bean.setSendHeaderCode(CtDataProtocol.CT_CONNECT_NORMAL);

        String currentUserId = UserManager.getInstance().getCurrentUserId();
        bean.setFromUserId(currentUserId);

        String toUserId = "123";
        if (currentUserId.equals("123")) {
            toUserId = "456";
        }

        bean.setToUserId(toUserId);


        mEdInput.setText("");
        CtExecutors.getInstance().syncSendCtRunnable(new CtExecutors.CtDataSendRunnable(bean));


    }

    @Override
    public void onBackPressed() {
        if (!isClose) {
            CtExecutors.getInstance().shutDownConnection();
            isClose = true;
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public void showMessage(CtReceiveDataBean receiveDataBean) {
//        Toast.makeText(this, receiveDataBean.getMessage(), Toast.LENGTH_SHORT).show();
        datas.add(receiveDataBean);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onSendSuccess() {
        // 消息显示进度
    }

    @Override
    public void onSendFail(Exception e) {

    }

    @Override
    public void onHandleData(CtSendDataBean dataBean) {
        String message = dataBean.getMessage();
        if (dataBean.getSendHeaderCode() == CtDataProtocol.CT_CONNECT_NORMAL) {
            datas.add(dataBean);
            adapter.notifyDataSetChanged();
        }

    }
}
