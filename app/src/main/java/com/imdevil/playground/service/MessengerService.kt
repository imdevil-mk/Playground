package com.imdevil.playground.service

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log


class MessengerService : Service() {

    /** Keeps track of all current registered clients.  */
    var mClients = mutableListOf<Messenger>()

    /** Holds last value set by a client.  */
    var mValue = 0

    /**
     * Handler of incoming messages from clients.
     */
    internal inner class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            Log.d(TAG, "handleMessage: ${msg.what}")
            when (msg.what) {
                MSG_REGISTER_CLIENT -> mClients.add(msg.replyTo)
                MSG_UNREGISTER_CLIENT -> mClients.remove(msg.replyTo)
                MSG_SET_VALUE -> {
                    mValue = msg.arg1
                    var i: Int = mClients.size - 1
                    while (i >= 0) {
                        try {
                            mClients[i].send(
                                Message.obtain(
                                    null,
                                    MSG_SET_VALUE, mValue, 0
                                )
                            )
                        } catch (e: RemoteException) {
                            // The client is dead.  Remove it from the list;
                            // we are going through the list from back to front
                            // so this is safe to do inside the loop.
                            mClients.removeAt(i)
                        }
                        i--
                    }
                }
                MSG_GET_INFO -> {
                    mValue = msg.arg1
                    msg.replyTo.send(
                        Message.obtain(
                            null, MSG_GET_INFO, ++mValue, 0
                        )
                    )
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    private val mMessenger: Messenger = Messenger(IncomingHandler())

    override fun onCreate() {
        Log.d(TAG, "onCreate: ")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG, "onBind: ")
        return mMessenger.binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: ")
        return super.onUnbind(intent)
    }

    companion object {
        const val TAG = "MessengerService"

        /**
         * Command to the service to register a client, receiving callbacks
         * from the service.  The Message's replyTo field must be a Messenger of
         * the client where callbacks should be sent.
         */
        const val MSG_REGISTER_CLIENT = 1

        /**
         * Command to the service to unregister a client, ot stop receiving callbacks
         * from the service.  The Message's replyTo field must be a Messenger of
         * the client as previously given with MSG_REGISTER_CLIENT.
         */
        const val MSG_UNREGISTER_CLIENT = 2

        /**
         * Command to service to set a new value.  This can be sent to the
         * service to supply a new value, and will be sent by the service to
         * any registered clients with the new value.
         */
        const val MSG_SET_VALUE = 3

        const val MSG_GET_INFO = 4
    }
}