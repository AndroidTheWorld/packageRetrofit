# packageRetrofit

 please see  [http://www.jianshu.com/p/5def90a34177 "android 建造者模式实战--Retrofit二次封装") about details;

#Usage
  
  1.initialization
  
```java

  public class myApplicatioon extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JinLib.initialize(this);//初始化lib
        AppContext.initialize(this);
    }
  }

```

2.use

```java

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

```