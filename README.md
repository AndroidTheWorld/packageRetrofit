# packageRetrofit

 please see  [http://www.jianshu.com/p/367eb56e3529/comments/2809140#comment-2809140](http://www.jianshu.com/p/367eb56e3529/comments/2809140#comment-2809140 "android 建造者模式实战--Retrofit二次封装") about details;

#Usage
  
  1.initialization
  
   <code>

public class myApplicatioon extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JinLib.initialize(this);//初始化lib
    }

}

</code>

2.use
<code>

   HttpClient client=new HttpClient.Builder()

                .baseUrl("http://xiaohua.hao.360.cn")//这个如果设置了NetConfig.baseUrl 此处可以不用重新调用baseUrl
                .url("/m/itxt")
                .param("callback","jsonp7")//多个param，可以重复调用此方法
                .build();
        client.post(new OnResultListener() {
            @Override
            public boolean onCache(String cache) {//缓存数据，返回true表示不再请求网络
                return false;
            }

            @Override
            public void onSuccess(String result) {//请求结果
                mTvTest.setText(result);
            }

            @Override
            public void onFailure(String message) {//请求失败的说明
                mTvTest.setText(message);
            }

            @Override
            public void onFinish() {//请求结束

            }
        });

</code>