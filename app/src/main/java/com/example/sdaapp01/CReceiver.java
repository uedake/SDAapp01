package com.example.sdaapp01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.sdaapp01.db.Record;
import com.example.sdaapp01.util.L;
import com.example.sdaapp01.util.Util;

public class CReceiver extends BroadcastReceiver {
	private Context context;
    private static final int normalizingMeasureMax=10;
    private static final double thres=100;

    private static int overThresCnt=0;

    private static boolean normalizingNeeded=false;
    private static int normalizingMeasureCnt=0;
    private static Acceleration sumupAcceleration =new Acceleration(0,0,0);

    private static Acceleration normalVec=new Acceleration(0,0,1);

    private static NormalizedAcceleration lastMeasure=null;

    public static void startNormalizing(){
        L.e("start normalizing");
        normalizingNeeded=true;
        normalizingMeasureCnt=0;
        sumupAcceleration =new Acceleration(0,0,0);
    }

    @Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		String action = intent.getAction();
		L.i("@@@@@ onReceive()" + action);
        if ("com.nttdocomo.android.smartdeviceagent.action.NOTIFICATION".equals(action)) {
            handleNotification(intent);
		}
        //logExtras(intent);
	}
    private Acceleration readFromIntent(Intent intent){
        Bundle extras = intent.getExtras();
        String uri = extras.getString("com.nttdocomo.android.smartdeviceagent.extra.CONTENT_URI_1");
        byte[] byteData = Util.readBytesFromUri(context, uri);
        return Acceleration.fromBytes(byteData);
    }
    private void updateNormalizingVectorIfNeeded(Acceleration a){
        if(normalizingNeeded){
            sumupAcceleration = sumupAcceleration.add(a);

            normalizingMeasureCnt++;
            if(normalizingMeasureCnt>=normalizingMeasureMax){
                normalVec= sumupAcceleration.scale(1.0/normalizingMeasureCnt);
                normalizingNeeded=false;
                Toast.makeText(context,"normal vec calculated:"+normalVec.getIntString(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private void diffProcessing(NormalizedAcceleration na){
        NormalizedAcceleration last=lastMeasure;
        lastMeasure=na;
        if(last!=null) {
            NormalizedAcceleration diff = new NormalizedAcceleration(na.vertical - last.vertical, na.horizontal - last.horizontal);
            if (diff.calcNorm() > thres) {
                overThresCnt++;
//                Toast.makeText(context, "walk cnt:" + overThresCnt, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void saveData(NormalizedAcceleration na){
        Record record;
        record = new Record(na.horizontal, 0, na.vertical-1000);
        record.save();
    }
    private void updateView(){
        Intent updateIntent = new Intent();
        updateIntent.setAction("UPDATE");
        updateIntent.setClass(context, MainActivity.class);
        LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent);
    }
    private void handleNotification(Intent intent){
        L.i("@@@@@ handle NOTIFICATION");
        Acceleration a = readFromIntent(intent);
        updateNormalizingVectorIfNeeded(a);

        NormalizedAcceleration na=NormalizedAcceleration.covert(a,normalVec);
  //      diffProcessing(na);
        saveData(na);
        updateView();
    }

    private void logExtras(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            for (final String key : extras.keySet()) {
                Object obj = extras.get(key);
                if (obj instanceof Integer) {
                    L.i("key: " + key + " val: " + String.valueOf(obj));
                } else {
                    L.i("key: " + key + " val: " + obj);
                }
            }
        }
    }

    private void logBytes(byte[] byteData) {
		for (int i = 0; i < byteData.length; i++) {
			L.i("byteData[" + i + "]:" + ((short)byteData[i]& 0xFF));
		}
    }

}
