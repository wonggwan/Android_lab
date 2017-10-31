package io.github.wonggwan.lab5;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class myWidget extends AppWidgetProvider {
    private static final String STATICACTION = "StaticFilter";
    private Products current;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews localremoteviews = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        localremoteviews.setOnClickPendingIntent(R.id.appwidget_text, pi);
        ComponentName me = new ComponentName(context, myWidget.class);
        appWidgetManager.updateAppWidget(me,localremoteviews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        if(intent.getAction().equals(STATICACTION)){
            current = (Products) intent.getParcelableExtra("detail");
            Integer position = intent.getIntExtra("position", -1);

            Intent localintent = new Intent(context,ProductDetails.class);
            localintent.putExtra("position", position);
            localintent.putExtra("detail", current);

            RemoteViews localremoteviews = new RemoteViews(context.getPackageName(),R.layout.my_widget);
            PendingIntent localpendingintent = PendingIntent.getActivity(context,0,localintent,PendingIntent.FLAG_UPDATE_CURRENT);
            localremoteviews.setTextViewText(R.id.appwidget_text, current.getName() + " 现仅售" + current.getPrice()+"!");
            localremoteviews.setImageViewResource(R.id.shopmark, current.getImgObject());
            localremoteviews.setOnClickPendingIntent(R.id.appwidget_text,localpendingintent);
            ComponentName localname = new ComponentName(context,myWidget.class);
            AppWidgetManager.getInstance(context).updateAppWidget(localname,localremoteviews);
        }
    }
            }