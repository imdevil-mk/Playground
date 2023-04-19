package com.imdevil.playground.service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import com.imdevil.playground.base.LogActivity
import com.imdevil.playground.databinding.ActivityServiceBinding


class ServiceActivity : LogActivity() {
    companion object {
        const val TAG = "LocalServiceActivity"
    }

    private lateinit var binding: ActivityServiceBinding

    private lateinit var serviceIntent: Intent
    private var localServiceBound = false
    private lateinit var localService: LocalService
    private val localConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            Log.d(TAG, "onServiceConnected: ")
            val binder = service as LocalService.LocalBinder
            localService = binder.getService()
            localServiceBound = true

            binding.unbindService.isEnabled = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.d(TAG, "onServiceDisconnected: ")
            localServiceBound = false
            binding.unbindService.isEnabled = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceIntent = Intent(this@ServiceActivity, LocalService::class.java)
        messengerIntent = Intent(this@ServiceActivity, MessengerService::class.java)

        binding.startService.setOnClickListener {
            startService(serviceIntent)
        }

        binding.stopService.setOnClickListener {
            stopService(serviceIntent)
        }

        binding.bindService.setOnClickListener {
            bindService(serviceIntent, localConnection, BIND_AUTO_CREATE)
        }

        binding.unbindService.setOnClickListener {
            unbindService(localConnection)
        }

        binding.bindMessengerService.setOnClickListener {
            bindService(messengerIntent, mMessengerConnection, BIND_AUTO_CREATE)
        }

        binding.unbindMessengerService.setOnClickListener {
            unbindService(mMessengerConnection)
        }
    }

    private lateinit var messengerIntent: Intent
    private lateinit var mRemoteMessenger: Messenger

    internal class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MessengerService.MSG_SET_VALUE -> {
                    Log.d(TAG, "handleMessage: MSG_SET_VALUE = ${msg.arg1}")
                }
                MessengerService.MSG_GET_INFO -> {
                    Log.d(TAG, "handleMessage: MSG_GET_INFO = ${msg.arg1}")
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    private val mLocalMessenger: Messenger = Messenger(IncomingHandler())

    private val mMessengerConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            Log.d(TAG, "onServiceConnected: ")
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
            mRemoteMessenger = Messenger(service)
            // We want to monitor the service for as long as we are
            // connected to it.
            try {
                var msg = Message.obtain(
                    null,
                    MessengerService.MSG_REGISTER_CLIENT
                )
                msg.replyTo = mLocalMessenger
                mRemoteMessenger.send(msg)

                // Give it some value as an example.
                msg = Message.obtain(
                    null,
                    MessengerService.MSG_SET_VALUE, this.hashCode(), 0
                )
                mRemoteMessenger.send(msg)

                msg = Message.obtain(
                    null,
                    MessengerService.MSG_GET_INFO, 68, 0
                )
                msg.replyTo = mLocalMessenger
                mRemoteMessenger.send(msg)
            } catch (e: RemoteException) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }
        }

        override fun onServiceDisconnected(className: ComponentName) {
            Log.d(TAG, "onServiceDisconnected: ")
        }
    }
}