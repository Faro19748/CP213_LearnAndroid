package com.example.lab

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LabAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // อัปเดตทุก Widget ของคลาสนี้เมื่อถึงรอบเวลา
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // สร้าง RemoteViews จาก Layout XML ของ Widget
            val views = RemoteViews(context.packageName, R.layout.widget_lab)

            val timeString = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            views.setTextViewText(R.id.widget_text, "Updated: $timeString")

            // สร้าง PendingIntent สำหรับการกดที่ Widget เพื่อให้อัปเดตได้ด้วยเงื่อนไข ACTION_APPWIDGET_UPDATE
            val intent = Intent(context, LabAppWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                appWidgetId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // ติดตั้ง Click Listener
            views.setOnClickPendingIntent(R.id.widget_title, pendingIntent)
            views.setOnClickPendingIntent(R.id.widget_text, pendingIntent)

            // สั่งอัปเดตวิวของ Widget นี้
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
