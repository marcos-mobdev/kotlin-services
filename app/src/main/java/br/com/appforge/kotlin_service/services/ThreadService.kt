package br.com.appforge.kotlin_service.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ThreadService : Service() {

    var myThread: MyThread? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        //Initialize resources
        super.onCreate()

        Log.i("info_service", "onCreate: Thread Service")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bundle = intent?.extras
        val delayTime = bundle?.getLong("delayTime")
        val validTime = delayTime ?: 2000L


        myThread?.start()
        myThread = MyThread(validTime)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    inner class MyThread(val validTime:Long): Thread(){
        override fun start() {
            super.run()
            repeat(10){ counter ->
                sleep(validTime)
                Log.i("info_service", "Playing thread service, counter: $counter")
            }
            stopSelf()
        }
    }
}