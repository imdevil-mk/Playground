package com.imdevil.playground.service.aidl

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.imdevil.playground.R

private const val TAG = "ClientActivity"

class ClientActivity : AppCompatActivity() {

    private lateinit var remoteService: IBookManager


    private val deathRecipient: IBinder.DeathRecipient = IBinder.DeathRecipient {

    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            remoteService = IBookManager.Stub.asInterface(service)
            service?.linkToDeath(deathRecipient, 0)

            val book = Book().apply {
                id = 100
                this.name = "Java"
            }
            Log.d(
                TAG,
                "onServiceConnected: start add ${Thread.currentThread()} ${System.currentTimeMillis()}"
            )
            remoteService.addBookOut(book)
            Log.d(
                TAG,
                "onServiceConnected: end add ${Thread.currentThread()} ${System.currentTimeMillis()} $book "
            )
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        val intent = Intent(this, RemoteService::class.java)
        bindService(intent, connection, BIND_AUTO_CREATE)
    }
}