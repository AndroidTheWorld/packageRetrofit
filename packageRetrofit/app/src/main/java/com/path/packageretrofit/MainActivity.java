package com.path.packageretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.path.net.HttpClient;
import com.path.net.OnResultListener;

public class MainActivity extends AppCompatActivity {
    private TextView mTvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        data();
    }

    private void data() {
        HttpClient client=new HttpClient.Builder()
                .baseUrl("http://xiaohua.hao.360.cn")//这个如果设置了NetConfig.baseUrl 此处可以不用重新调用baseUrl
                .url("/m/itxt")
                .param("callback","jsonp7")//多个param，可以重复调用此方法
                .build();
        client.post(new OnResultListener() {
            @Override
            public boolean onCache(String cache) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                mTvTest.setText(result);
            }

            @Override
            public void onFailure(String message) {
                mTvTest.setText(message);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void init() {
        mTvTest= (TextView) findViewById(R.id.tv_main_test);

    }
}
