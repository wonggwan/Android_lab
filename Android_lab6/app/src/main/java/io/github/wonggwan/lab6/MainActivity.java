package io.github.wonggwan.lab6;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private TextView Condition, cur, dur;
    private SeekBar seekBar;

    private Button PlayPausebutton, Stopbutton, Quitbutton;


    private ObjectAnimator animator;

    private IBinder mBinder;
    private Handler mHandler;

    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    private void bindServiceConnection() {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = service;
            Log.i("Service is ", "connected");
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceConnection = null;
        }
    };

    private void init() {
        PlayPausebutton = (Button) findViewById(R.id.playpause);
        Stopbutton = (Button) findViewById(R.id.stop);
        Quitbutton = (Button) findViewById(R.id.quit);
        Condition = (TextView) findViewById(R.id.condition);
        cur = (TextView) findViewById(R.id.currentTime);
        dur = (TextView) findViewById(R.id.totaltime);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        ImageView imageView = (ImageView) findViewById(R.id.Image);
        animator = ObjectAnimator.ofFloat(imageView, "rotation", 0, 360);
        animator.setDuration(24000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.RESTART);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bindServiceConnection();
        PlayPausebutton.setText("PLAY");
        Condition.setText("Stopped");

        final Thread mThread = new Thread()
        {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (serviceConnection != null) {
                        mHandler.obtainMessage(123).sendToTarget();
                    }
                }
            }
        };

        mThread.start();
        mHandler = new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                switch (msg.what){
                    case 123:
                        try {
                            int code = 104;
                            Parcel data = Parcel.obtain();
                            Parcel reply = Parcel.obtain();
                            mBinder.transact(code,data,reply,0);

                            Bundle bundle = reply.readBundle();

                            seekBar.setProgress(bundle.getInt("prog"));
                            seekBar.setMax(bundle.getInt("dur"));
                            cur.setText(time.format(bundle.getInt("cur")));
                            dur.setText(time.format(bundle.getInt("dur")));

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };


        setSeekBar();
        setbnt();
    }

    private void setSeekBar() {
        seekBar.setProgress(0);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try{
                    int progress = seekBar.getProgress();
                    int code = 105;
                    Parcel data = Parcel.obtain();
                    data.writeInt(progress);
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code,data,reply,0);
                    return;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private void setbnt() {
        PlayPausebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = MainActivity.this.PlayPausebutton.getText().toString();
                try{
                    if(text.equals("PLAY")) {

                        PlayPausebutton.setText("PAUSE");
                        Condition.setText("Playing");
                        if (animator.isPaused()) {
                            animator.resume();
                        }
                        else {
                            animator.start();
                        }

                    }
                    else if (text.equals("PAUSE")) {
                        PlayPausebutton.setText("PLAY");
                        Condition.setText("Paused");
                        animator.pause();
                    }
                    int code = 101;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code,data,reply,0);
                }
                catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });

        Stopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayPausebutton.setText("PLAY");
                Condition.setText("Stopped");
                try{
                    int code = 102;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code,data,reply,0);

                    Bundle bundle = reply.readBundle();
                    cur.setText(time.format(bundle.getInt("cur")));
                    dur.setText(time.format(bundle.getInt("dur")));
                    seekBar.setProgress(bundle.getInt("prog"));
                    animator.end();
                    return;
                }
                catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });

        Quitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(serviceConnection);
                serviceConnection = null;
                try {
                    MainActivity.this.finish();
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            unbindService(serviceConnection);
            serviceConnection = null;
            try {
                MainActivity.this.finish();
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode,event);
    }
}
