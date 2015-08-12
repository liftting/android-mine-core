package com.xm.client.ctui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.xm.client.CtMainActivity;
import com.xm.client.R;
import com.xm.client.apdata.UserInfo;
import com.xm.client.apdata.UserManager;
import com.xm.client.net.CtExecutors;
import com.xm.client.net.CtHandler;
import com.xm.client.net.listener.CtSocConnectListener;

/**
 * Created by wm on 15/7/14.
 */
public class CtLoginActivity extends Activity {

    private EditText mEdUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ct_login);
        mEdUserId = (EditText) findViewById(R.id.ed_ct_user_id);
    }

    public void login(View v) {
        initConnect();
    }

    public void initConnect() {
        String inputText = mEdUserId.getText().toString();
        if (TextUtils.isEmpty(inputText)) {
            inputText = "123";
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(inputText);
        userInfo.setUserName("wangming");
        userInfo.setUserNickName("nickName");
        UserManager.getInstance().setCurrentUser(userInfo);

        // 开始将连接建立
        CtExecutors.getInstance().connect(new CtSocConnectListener() {
            @Override
            public void connectSuccess() {
                CtHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        invokeCt();
                    }
                });
            }

            @Override
            public void connectClose() {

            }

            @Override
            public void connectError() {

            }
        });

    }

    private void invokeCt() {
        Intent intent = new Intent();
        intent.setClass(this, CtMainActivity.class);
        startActivity(intent);
        finish();
    }


}
