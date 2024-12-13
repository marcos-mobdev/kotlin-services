package br.com.appforge.kotlin_service.view

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import br.com.appforge.kotlin_service.databinding.ActivityMainBinding
import br.com.appforge.kotlin_service.services.ForegroundService

class MainActivity : AppCompatActivity() , ServiceConnection{

    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var serviceConnection:ServiceConnection

    /*
    //Approach without MainActivity interface ServiceConnection
    private val serviceConnection:ServiceConnection = object:ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            TODO("Not yet implemented")
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            TODO("Not yet implemented")
        }
    }

     */

    private lateinit var connectedService: ForegroundService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requestNotificationPermissions()

        //val myService = Intent(this,MyService::class.java)
        val myConnection = Intent(this, ForegroundService::class.java)
        serviceConnection = this


        with(binding){
            btnStart.setOnClickListener {
                //myConnection.putExtra("delayTime", 3000L)

                //Required Android SDK >= 26 for Foreground service
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(myConnection)
                }else{
                    startService(myConnection)
                }

                bindService(myConnection, serviceConnection, BIND_AUTO_CREATE)
            }
            btnStop.setOnClickListener {
                stopService(myConnection)
                unbindService(serviceConnection)
            }

            btnGetData.setOnClickListener {
                val counter = connectedService.counter
                Toast.makeText(this@MainActivity, "Counter: $counter", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.POST_NOTIFICATIONS
            )
            if (notificationPermission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
            }
        }

    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.i("info_service", "onServiceConnected")
        val customBinder = service as ForegroundService.CustomBinder
        connectedService = customBinder.getService()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        Log.i("info_service", "onServiceDisconnected")
        Toast.makeText(this, "Disconnected service", Toast.LENGTH_SHORT).show()
    }



}