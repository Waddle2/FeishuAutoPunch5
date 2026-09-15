package com.waddle2.feishuautopunch;
import android.content.*;
public class BootReceiver extends BroadcastReceiver { @Override public void onReceive(Context c,Intent i){if(!AlarmReceiver.isEnabled(c))return;for(int s=0;s<2;s++){int h=c.getSharedPreferences("settings",0).getInt("h"+s,-1),m=c.getSharedPreferences("settings",0).getInt("m"+s,-1);if(h>=0&&m>=0)AlarmReceiver.scheduleDaily(c,s,h,m);}}}
