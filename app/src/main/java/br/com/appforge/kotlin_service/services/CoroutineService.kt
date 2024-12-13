package br.com.appforge.kotlin_service.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CoroutineService : Service() {

    private val coroutine = CoroutineScope(Dispatchers.IO)

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    override fun onCreate() {
        //Initialize resources
        super.onCreate()
        Log.i("info_service", "onCreate: ")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bundle = intent?.extras
        val delayTime = bundle?.getLong("delayTime")
        val validTime = delayTime ?: 2000L

        coroutine.launch {
            repeat(10){ counter ->
                delay(validTime)
                Log.i("info_service", "Playing service: $counter - for $validTime")
            }
            stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        //Finish resources
        super.onDestroy()
        Log.i("info_service", "onDestroy: ")
        coroutine.cancel()
    }
}