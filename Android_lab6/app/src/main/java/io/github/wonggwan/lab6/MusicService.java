package io.github.wonggwan.lab6;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.widget.Toast;


import java.io.IOException;

/**
 * Created by wonggwan on 2017/11/10.
 */

public class MusicService extends Service {
    public MediaPlayer player = null;

    public IBinder mBinder = new MyBinder();

    public void onCreate() {
        super.onCreate();
    }

    public int isStopped = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 101:
                    playOrPause();
                    break;
                case 102:
                    stop();
                    break;
                case 103:
                    onDestroy();
                    break;
                case 104:
                    if(player != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("cur", getposorprog());
                        bundle.putInt("dur", getdur());
                        bundle.putInt("prog", getposorprog());
                        reply.writeBundle(bundle);
                    }
                    break;
                case 105:
                    int i = data.readInt();
                    player.seekTo(i);
                    System.out.println(i);
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }


    public void playOrPause() {
        if(player == null) {
            player = new MediaPlayer();
            Toast.makeText(MusicService.this, "正在播放：늑는다", Toast.LENGTH_SHORT).show();
            try {
                player.setDataSource("/data/melt.mp3");
                player.prepare();
                player.seekTo(0);
                player.setLooping(true);
                player.start();
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(player != null) {
            if (player.isPlaying()) {
                player.pause();
            }
            else {
                player.start();
            }
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
            isStopped = 1;
        }
        try {
            if(isStopped == 1) {
                player.reset();
                player.setDataSource("/data/melt.mp3");
                player.prepare();
                player.seekTo(0);
                player.setLooping(true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getdur() {
        if (player != null) {
            return player.getDuration();
        }
        else return 0;
    }

    public int getposorprog() {
        return player.getCurrentPosition();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        if(player != null) {
            player.stop();
            player.reset();
            player.release();
            player = null;
        }
    }



    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
