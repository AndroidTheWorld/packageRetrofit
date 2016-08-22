package com.path.packageretrofit;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.path.net.HttpClient;
import com.path.net.OnProgressListener;
import com.path.net.OnResultListener;
import com.path.utils.NameUtil;
import com.path.utils.StringUtil;
import com.path.utils.Video;
import com.path.utils.VideoProvider;

import java.io.File;

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
                .bodyType(HttpClient.OBJECT,News.class) //当要得到已经解析完成的实体类时，添加此方法即可
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
        mTvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, 8242);
            }
        });
       /* ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("loading...");
        dialog.setCancelable(true);
        dialog.show();*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode==8242){
            Uri uri =data.getData();
            if (uri==null)return;
            Video video=new VideoProvider().getVideo(uri);
            upload(video);
        }
    }


    private void upload(Video video) {
        HttpClient client=new HttpClient.Builder()
                .baseUrl("http://192.168.1.69:8081")
                .url("/video/v1/upload")
                .param("userid","E70DFC5C7009B58520B823F5F574CE50")
                .param("videoName",video.getTitle())
                .param("videoDesc",video.getTitle())
                .param("activityid","")
                .param("category","")
                .param("imagename", StringUtil.buffer(NameUtil.getPicName(video.getImage(),false),".jpg"))
                .file("imagefile",new File(video.getImage()))
                .file("file",new File(video.getPath()))
                .build();
        client.upload(new OnProgressListener() {
            @Override
            public void onProgress(int newProgress) {
                Log.i("上传成功","newProgress="+newProgress);
            }
        });

    }
}
