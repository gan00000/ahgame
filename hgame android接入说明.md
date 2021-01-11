#SDK接入说明

* [一. SDK接入配置](#100)
* [二. SDK api说明](#101)
	* [1. 实例SDK接口IGama对象](#1)
	* [2. Activity生命周期调用](#2)
	* [3. 初始化sdk](#3)
	* [4. 设置角色信息](#5)
	* [5. 登录接口](#6)
	* [6. 充值接口](#7)
	


----------------

* <h2 id="100">SDK接入配置</h2> 
说明：**_本SDK需要使用androidx库_**

* 添加游戏配置文件，在您的项目中，打开 your_app | assets下创建gama目录并添加 gama_gameconfig.properties 配置文件（该文件SDK对接人员提供）

* 在您的项目中，打开 your_app | Gradle Scripts | build.gradle (Module: app) 并添加以下依赖的aar和其他库，以便编译SDK:

	```

	dependencies {
	    compile fileTree(include: ['*.jar'], dir: 'libs')
	
	    //SDK   添加以下配置以依赖SDK,具体版本对接的时候与SDK人员沟通确定
	    implementation(name: 'GamaSDK-HGameSdk-release', ext: 'aar')

	    api ('com.github.bumptech.glide:glide:4.11.0')
	
	    //Google
	    api 'com.google.android.gms:play-services-auth:19.0.0'
	    api 'com.google.android.gms:play-services-base:17.5.0'
	    api 'com.google.android.gms:play-services-games:21.0.0'
	
	    //firebase
	    implementation 'com.google.firebase:firebase-auth:20.0.1'
	    // Recommended: Add the Firebase SDK for Google Analytics.
	    implementation 'com.google.firebase:firebase-analytics:18.0.0'
	    api 'com.google.firebase:firebase-core:18.0.0'
	    api 'com.google.firebase:firebase-messaging:21.0.1'
	    //crashlytics
	    api 'com.google.firebase:firebase-crashlytics:17.3.0'
	
	
	    api 'com.android.installreferrer:installreferrer:2.2'
	    //google volley
	    api 'com.android.volley:volley:1.1.1'
	
	    //Facebook库
	    api 'com.facebook.android:facebook-login:7.1.0'
	    api 'com.facebook.android:facebook-share:7.1.0'
	    api 'com.facebook.android:facebook-messenger:7.1.0'
	
	    //androidx
	    api 'androidx.legacy:legacy-support-v4:1.0.0'
	    api 'androidx.appcompat:appcompat:1.2.0'
	    api 'androidx.recyclerview:recyclerview:1.1.0'
	    api 'androidx.constraintlayout:constraintlayout:2.0.4'
	    api 'androidx.browser:browser:1.3.0'
	    api 'com.google.code.gson:gson:2.8.6'
	
	    //adjust
	    api 'com.adjust.sdk:adjust-android:4.21.1'
	    //af
	    implementation 'com.appsflyer:af-android-sdk:6.0.0'

	}		
	```

* 在您的项目中，打开 your_app | Gradle Scripts | build.gradle 中productFlavors 内添加以下配置，以动态设置游戏配置

	```
	//添加以下配置 ，比如
	  productFlavors {
		//对接SDK提供
        xxx {
            applicationId "xxx.xxx.xxx"
            minSdkVersion 18
            targetSdkVersion 26
            versionCode 5
            versionName "2.0"
            flavorDimensions "1"

            resValue "string", "facebook_app_name", "xxxx"
            resValue "string", "facebook_app_id", "xxxxxxx"
            resValue "string", "facebook_authorities", "com.facebook.app.FacebookContentProviderxxxxxxx"
           }


    }
    ```
 ------------------------------

<h2 id="101">SDK api说明</h2>
以下为sdk api使用示例,具体请查看SDK demo 

* <h3 id="1">实例SDK接口IGama对象</h3>  
`iGama = IHGameFactory.create(); ` 
 
* <h3 id="2">Activity生命周期调用</h3> 

	```java
	游戏Activity以下相应的声明周期方法（必须调用）:  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ihGame = IHGameFactory.create();
	    //初始化sdk，此方法必须在其他所有接口前调用  SDKVersion.cn为简体中文版，SDKVersion.global为全球版
        ihGame.initSDK(this, SDKVersion.cn);
        //在游戏Activity的onCreate生命周期中调用
        ihGame.onCreate(this);
     }
   @Override
    protected void onResume() {
        super.onResume();
        PL.i("activity onResume");
        ihGame.onResume(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ihGame.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ihGame.onPause(this);
        PL.i("activity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        PL.i("activity onStop");
        ihGame.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PL.i("activity onDestroy");
        ihGame.onDestroy(this);
    } 
    
     @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ihGame.onWindowFocusChanged(this,hasFocus);
    }
    
     /**
     * 6.0以上系统权限授权回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { 
    	super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      	PL.i("activity onRequestPermissionsResult");
      	ihGame.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }  
    ```
 
* <h3 id="3">初始化sdk</h3>
```
	游戏启动的时候调用，通常在游戏activity oncreate中调用
	
	//初始化sdk，此方法必须在其他所有接口前调用  SDKVersion.cn为简体中文版，SDKVersion.global为全球版
        ihGame.initSDK(this, SDKVersion.cn);
```

* <h3 id="5">设置角色信息</h3> 


 ```
 在游戏获得角色信息的时候调用，每次登陆，切换账号等角色变化时调用（必须调用）
 
	/**
     * 在游戏获得角色信息的时候调用
     * roleId 角色id
     * roleName  角色名
     * rolelevel 角色等级
     * vipLevel 角色vip等级
     * severCode 角色伺服器id
     * serverName 角色伺服器名称
     */
ihGame.registerRoleInfo(Activity activity,String roleId,String roleName,String roleLevel,String vipLevel,String severCode,String serverName);
```

* <h3 id="6">登录接口</h3>  

 ```
//登陆接口 ILoginCallBack为登录成功后的回
    
    ihGame.initSDK(BaseMainActivity.this, SDKVersion.cn);
                //登陆接口 ILoginCallBack为登录成功后的回调
                ihGame.login(BaseMainActivity.this, new ILoginCallBack() {
                    @Override
                    public void onLogin(SLoginResponse sLoginResponse) {
                        if (sLoginResponse != null) {
                        //用户id
                            String uid = sLoginResponse.getUserId();
                            String accessToken = sLoginResponse.getAccessToken();
                            String timestamp = sLoginResponse.getTimestamp();
                           
                        }
                    }
                });
```

* <h3 id="7">充值接口</h3>    

 ```
/*
充值接口
cpOrderId cp订单号，请保持每次的值都是不会重复的
extra 自定义透传字段（从服务端回调到cp）
*/
ihGame.webPay(activity, cpOrderId,extra, new IPayListener() {
                    @Override
                    public void onPayFinish(Bundle bundle) {

                    }
                });
```







