package com.imdevil.playground.service.aidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

private const val TAG = "RemoteService"

class RemoteService : Service() {

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    private val binder = object : IBookManager.Stub() {
        override fun getBookList(): MutableList<Book> {
            return mutableListOf<Book>()
        }

        override fun addBookIn(book: Book?) {
            Log.d(TAG, "addBookIn: ${Thread.currentThread()}")
            Thread.sleep(3000)
            Log.d(TAG, "addBookIn: $book")
        }

        override fun addBookOut(book: Book?): Int {
            Log.d(TAG, "addBookOut: $book")
            book?.name = "out"
            return book?.id ?: -1
        }

        override fun addBookInOut(book: Book?) {
            Log.d(TAG, "addBookInOut: $book")
        }
    }
}