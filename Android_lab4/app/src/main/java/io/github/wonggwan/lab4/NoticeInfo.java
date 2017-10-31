package io.github.wonggwan.lab4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class NoticeInfo extends BroadcastReceiver {
    private Products current;
    private NotificationManager notificationmanager;
    private PendingIntent contentIntent;
    private static Integer number = 0x123;

    private static final String STATICACTION = "StaticFilter";
    private static final String DYNAMICACTION = "DynamicFilter";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(STATICACTION)) {
            current = (Products) intent.getParcelableExtra("detail");
            Integer position = intent.getIntExtra("position", -1);

            assert current != null;

            Integer img = current.getImgObject();
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), img);
            notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification.Builder notification = new Notification.Builder(context);
            notification.setContentTitle("跳楼大甩卖！")
                    .setContentIntent(contentIntent)
                    .setContentText(current.getName() + "仅需" + current.getPrice() + '!')
                    .setSmallIcon(R.mipmap.shopicon)
                    .setLargeIcon(bitmap)
                    .setTicker("您有一条新的消息")
                    .setAutoCancel(true);
            Intent sendIntend = new Intent().setClass(context, ProductDetails.class);
            sendIntend.putExtra("position", position);
            sendIntend.putExtra("detail", current);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, sendIntend, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(contentIntent);
            notificationmanager.notify(number += 1, notification.build());
        }
        else if (intent.getAction().equals(DYNAMICACTION)) {
            current = (Products) intent.getParcelableExtra("detail");
            assert current != null;

            Integer img = current.getImgObject();
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), img);
            notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification.Builder notification = new Notification.Builder(context);
            notification.setContentTitle("马上下单")
                    .setContentText(current.getName() + "已经加入购物车")
                    .setContentIntent(contentIntent)
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.mipmap.shopicon)
                    .setTicker("您有新商品已添加到购物车")
                    .setAutoCancel(true);
            Intent sendIntend = new Intent().setClass(context, MainActivity.class);
            sendIntend.putExtra("added",true);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, sendIntend, 0);
            notification.setContentIntent(contentIntent);
            notificationmanager.notify(number += 1, notification.build());
        }
    }
}