package seino.indomobil.dmsmobile.presentation.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import seino.indomobil.dmsmobile.R
import java.util.*

class FirebaseMessage : FirebaseMessagingService() {
    private val channelId = "channel"
    private val channelName = "Notification"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val data = remoteMessage.data
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        notificationBuilder.setContentTitle(data["title"].toString())
            .setContentTitle(data["body"].toString())
            .setStyle(NotificationCompat.BigTextStyle().bigText(data["body"].toString()))
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setSmallIcon(R.drawable.img_logo_dms)
            .setVibrate(longArrayOf(Notification.DEFAULT_VIBRATE.toLong()))
            .setSound(soundUri)
            .setChannelId("channel")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mng = this.getSystemService(NotificationManager::class.java)
            val channel = NotificationChannel(
                this.channelId,
                this.channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.lightColor = Color.RED
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            channel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            channel.enableVibration(true)
            channel.enableLights(true)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.setSound(soundUri, audioAttributes)
            mng.createNotificationChannel(channel)
        }

        val classType: Class<*>?
        val bundle = Bundle()
        try {
            val layout = JSONObject(data["layout"].toString())
            val objMain = JSONObject(layout.get("Main").toString())
            classType = Class.forName(objMain.get("Package").toString())

            try {
                val objItem = JSONObject(layout.get("Item").toString())
                val statusItem = objItem.get("Status") as Boolean
                if (statusItem) {
                    val detail = objItem.get("Detail").toString()
                    bundle.putString("StatusLayout", "true")
                    bundle.putString("Package", detail)
                } else {
                    bundle.putString("StatusLayout", "false")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val param = JSONObject(data["param"].toString())
            val statusParam = param.get("Status") as Boolean
            if (statusParam) {
                val detailParam = param.get("Detail").toString()
                bundle.putString("StatusParam", "true")
                bundle.putString("Params", detailParam)
            } else {
                bundle.putString("StatusParam", "false")
            }
            bundle.putString("Notification", "true")
            val i = Intent(this, classType)
            i.putExtras(bundle)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getActivity(this, notificationID(), i, PendingIntent.FLAG_IMMUTABLE)
            } else {
                PendingIntent.getActivity(this, notificationID(), i, PendingIntent.FLAG_ONE_SHOT)
            }
            notificationBuilder.setContentIntent(pendingIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                notificationBuilder.priority = NotificationManager.IMPORTANCE_HIGH
            } else {
                notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH
            }
            manager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }


        super.onMessageReceived(remoteMessage)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun notificationID(): Int {
        val random = Random()
        return random.nextInt(9999 - 1) + 1
    }
}