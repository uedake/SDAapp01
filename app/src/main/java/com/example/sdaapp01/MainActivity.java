package com.example.sdaapp01;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sdaapp01.db.Record;
import com.example.sdaapp01.util.L;

import java.util.Date;

public class MainActivity extends Activity {
    Intent intent;
    final static String TAG = "SDAapp01Dbg";
    private DataAdapter mDataAdapter = null;
    private Cursor mDataCursor = null;
    private Handler mHandler;

    private ListView mListView = null;
    private TextView mTextView = null;

    private BroadcastReceiver mReceiveReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("UPDATE".equals(intent.getAction())) {
                L.i("UPDATE");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mDataAdapter != null) {
                            L.i("notifyDataSetChanged");
                            MainActivity.this.updateImage();
                            MainActivity.this.updateList();
                            MainActivity.this.updateState();
                        }
                    }
                });
            }
        }

    };
    private final int imageW=600;
    private final int imageH=600;
    private final int maxGrabityScale=1000;

    private void updateList(){
        mDataCursor = Record.fetchResultCursor(10);
        mDataAdapter = new DataAdapter(MainActivity.this, mDataCursor);
        mListView.setAdapter(mDataAdapter);
    }

    private void updateImage(){
        Cursor mDataCursor = Record.fetchResultCursor(100);

        Bitmap bitmap = Bitmap.createBitmap(imageW, imageH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);

        Paint latestDataPaint = new Paint();
        latestDataPaint.setColor(Color.RED);
        latestDataPaint.setStyle(Paint.Style.FILL);
        latestDataPaint.setAntiAlias(true);

        Paint oldDataPaint = new Paint();
        oldDataPaint.setColor(Color.BLUE);
        oldDataPaint.setStyle(Paint.Style.FILL);
        oldDataPaint.setAntiAlias(true);

        Paint axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);

        int centerX=imageW/2;
        int centerY=imageH/2;

        canvas.drawLine(0,centerY,imageW,centerY,axisPaint);
        canvas.drawLine(centerX,0,centerX,imageH,axisPaint);

        while(mDataCursor.moveToNext()){
            double data1 = mDataCursor.getDouble(mDataCursor.getColumnIndexOrThrow("ax"));
            double data3 = mDataCursor.getDouble(mDataCursor.getColumnIndexOrThrow("az"));
            NormalizedAcceleration na=new NormalizedAcceleration(data3,data1);
//                                L.e("read data("+mDataCursor.getPosition()+"):"+na.horizontal+","+na.vertical);
            double scale=Math.min(imageW,imageH)/2;

            double x=centerX+na.horizontal/maxGrabityScale*scale;
            double y=centerY-na.vertical/maxGrabityScale*scale;
            if(mDataCursor.getPosition()<10)
                canvas.drawCircle((float)x, (float)y, 5, latestDataPaint);
            else
                canvas.drawCircle((float)x, (float)y, 5, oldDataPaint);
        }
        // ImageViewに作成したBitmapを与える
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
    }

    private void updateState(){
        Cursor mDataCursor = Record.fetchResultCursor(20);
        double v=0;
        double h=0;
        int cnt=mDataCursor.getCount();
        while(mDataCursor.moveToNext()) {
            double data1 = mDataCursor.getDouble(mDataCursor.getColumnIndexOrThrow("ax"));
            double data3 = mDataCursor.getDouble(mDataCursor.getColumnIndexOrThrow("az"));
            v+=Math.abs(data3);
            h+=data1;
        }
        v=v/cnt;
        h=h/cnt;

        double ang=Math.atan2(v,h)/3.14*2*90;
        double power=Math.sqrt(h*h+v*v);
        String state="不明";
        if(power<100){
            state="静止";
        }
        else if(ang>50){
            state="貧乏ゆすり";
            sendBroadcast(state);
            soundPlay(state);
        }
        else if(ang<25){
            state="すり足";
            soundPlay(state);
            sendBroadcast(state);
        }
        else if(power>700){
            state="早歩き";
        }
        mTextView.setText(state+":"+(int)ang+","+(int)power);
    }

    private void sendBroadcast(String state){
        Intent i=new Intent();
        i.setAction("com.tizen.action.SLIPPER");
        i.putExtra("state",state);
        sendBroadcast(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        setContentView(R.layout.activity_main);

        L.e("@@@@@ onCreate ");

        m_clsBGM = MediaPlayer.create(this, R.raw.binbou);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(
                        mReceiveReceiver,
                        new IntentFilter("UPDATE"));

        findViewById(R.id.button1).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        L.e("test");
                        CReceiver.startNormalizing();
                    }
                });
        findViewById(R.id.buttonClear).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Record.clear();
                        m_clsBGM.pause();
                    }
                }
        );

    /* 周辺機器にメッセージを送信 -- Control-Bボタン */
        findViewById(R.id.button2).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction("com.example.sdaapp01.sda.action.OTHER_NOTIFICATION");
                        intent.setClassName(
                                "com.nttdocomo.android.smartdeviceagent",
                                "com.nttdocomo.android.smartdeviceagent.RequestReceiver");
                        intent.putExtra(
                                "com.example.sdaapp01.sda.extra.NAME_JA",
                                "SDAapp01");
                        intent.putExtra(
                                "com.example.sdaapp01.sda.extra.NAME_EN",
                                "SDAapp01");
                        intent.putExtra(
                                "com.example.sdaapp01.sda.extra.ILLUMINATION",
                                "a".getBytes());
                        L.i("@@@@@ sendBroadcast(" + intent.toString() + ")");
                        sendBroadcast(intent);
                    }
                });

    /* （予備)周辺機器にメッセージを送信 -- Control-Cボタン */
        findViewById(R.id.button3).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent();
                        intent.setAction("com.example.sdaapp01.sda.action.OTHER_NOTIFICATION");
                        intent.setClassName(
                                "com.nttdocomo.android.smartdeviceagent",
                                "com.nttdocomo.android.smartdeviceagent.RequestReceiver");
                        intent.putExtra(
                                "com.example.sdaapp01.sda.extra.NAME_JA",
                                "SDAapp01");
                        intent.putExtra(
                                "com.example.sdaapp01.sda.extra.NAME_EN",
                                "SDAapp01");
                        intent.putExtra(
                                "com.example.sdaapp01.sda.extra.CONTENT_2",
                                "c".getBytes());
                        intent.putExtra(
                                "com.example.sdaapp01.sda.extra.ILLUMINATION",
                                "b".getBytes());

                        L.i("@@@@@ sendBroadcast(" + intent.toString() + ")");
                        sendBroadcast(intent);

                    }
                });
        mTextView =(TextView)findViewById(R.id.textView);

        mListView = (ListView) findViewById(R.id.listView);
        updateList();
    }

    private MediaPlayer m_clsBGM;

    Date last;
    public void soundPlay(String state) {
        Date now=new Date();
        if(last !=null && now.getTime()-last.getTime()<5000){
            return;
        }
        last=now;
        if(state.equals("貧乏ゆすり")) {
            m_clsBGM = MediaPlayer.create(this, R.raw.binbou);
        }
        else if(state.equals("すり足")) {
            m_clsBGM = MediaPlayer.create(this, R.raw.suriashi);
        }
        m_clsBGM.start();
    }
}