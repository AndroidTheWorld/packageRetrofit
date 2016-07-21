package com.path.packageretrofit;

import android.app.ProgressDialog;
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
                .baseUrl("http://tuqu.ykclick.com")//这个如果设置了NetConfig.baseUrl 此处可以不用重新调用baseUrl
                .url("/pic/v1/pictures")
                .param("category","1")//多个param，可以重复调用此方法
                .param("pageNo","1")//多个param，可以重复调用此方法
                .bodyType(HttpClient.OBJECT,News.class)
                .build();
        client.post(new OnResultListener<News>() {
            @Override
            public boolean onCache(String cache) {//缓存数据，返回true表示不再请求网络
                return false;
            }

            @Override
            public void onSuccess(String result) {//请求结果

            }

            @Override
            public void onFailure(String message) {//请求失败的说明
                mTvTest.setText(message);
            }

            @Override
            public void onFinish() {//请求结束

            }

            @Override
            public void onSuccess(News result) {
                mTvTest.setText(result.getErrDesc());
            }

            @Override
            public void onFailure(News cache, String message) {

            }
        });
    }

    private void init() {
        mTvTest= (TextView) findViewById(R.id.tv_main_test);
        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("loading...");
        dialog.setCancelable(true);
        dialog.show();

    }
}
