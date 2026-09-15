package com.waddle2.feishuautopunch;
import android.app.Activity; import android.os.Bundle; import android.view.WindowManager; import android.widget.TextView;
public class LaunchFeishuActivity extends Activity { @Override protected void onCreate(Bundle b){super.onCreate(b);getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);TextView t=new TextView(this);setContentView(t);new android.os.Handler().postDelayed(()->{AlarmReceiver.launchFeishu(this,"定时");finishAndRemoveTask();},200);}}
