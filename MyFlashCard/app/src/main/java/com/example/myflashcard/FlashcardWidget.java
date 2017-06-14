package com.example.myflashcard;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class FlashcardWidget extends AppWidgetProvider {
    private static List<Data> mList;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        mList = getDataFromDB(context);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        Random random = new Random();
        int n = random.nextInt(mList.size()) + 1;
        views.setTextViewText(R.id.tv_widget, mList.get(n).question);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
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

    private static List<Data> getDataFromDB(Context context) {
        String[] columns = new String[]{CardContract.CardData.QUESTION, CardContract.CardData.ANSWER};
        Cursor cursor = context.getContentResolver().query(DataProvider.CARD_URI, columns, null, null, null);
        if (cursor == null) return null;
        List<Data> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Data data = new Data(cursor.getString(0), cursor.getString(1));
                list.add(data);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }
}

