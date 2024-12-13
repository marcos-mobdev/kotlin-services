package br.com.appforge.kotlin_service.services

import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import br.com.appforge.kotlin_service.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ForegroundService : Service() {

    private val coroutine = CoroutineScope(Dispatchers.IO)

    var counter = 0

    inner class CustomBinder: Binder() {
        fun getService(): ForegroundService {
            return this@ForegroundService
        }
    }

    override fun onBind(intent: Intent): IBinder ?{
        return CustomBinder()
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("info_service", "onCreate")
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val channelId = "reminders"
        val currentDate = System.currentTimeMillis()
        val notification = NotificationCompat.Builder(this, channelId).apply {
            setSmallIcon(R.drawable.ic_location_24)
            //setWhen(currentDate)
            setShowWhen(true)
            //setChannelId("News")
            setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.car
                )
            )
            setContentTitle("My Service")
            setContentText("My service is running at your phone")
        }

        startForeground(1,notification.build())

        coroutine.launch {
            repeat(10){ i ->
                counter = i
                Log.i("info_service", "Playing service: $counter")
                delay(2000)
            }
            //Stop service manually
            stopSelf()
        }
        //Restart the service if canceled by Android
        return START_STICKY
        //return START_STICKY_COMPATIBILITY

        //Do not restart the service if canceled by Android
        //return START_NOT_STICKY

        //Restarts the service, passing the Intent parameters that were present when the service was restarted
        //return START_REDELIVER_INTENT

        //Default return
        //return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutine.cancel()
        Log.i("info_service", "onDestroy")

    }
}