package com.imdevil.playground.service.aidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.os.RemoteException
import android.util.Log

private const val TAG = "AIDL-RemoteService"

class RemoteService : Service() {

    private val remoteCallbackList: RemoteCallbackList<IBookChangeListener> = RemoteCallbackList()
    private val books = mutableListOf<Book>()

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    private val binder = object : IBookManager.Stub() {

        override fun addBookIn(book: Book) {
            Log.d(TAG, "addBookIn: start in ${Thread.currentThread()}  $book")
            Thread.sleep(3000)
            books.add(book)
            book.name += " from addBookIn"
            Log.d(TAG, "addBookIn: $book")
            notifyBooksChanged()
        }

        override fun addBookOut(book: Book): Int {
            Log.d(TAG, "addBookOut: start in ${Thread.currentThread()} $book")
            Thread.sleep(3000)
            books.add(book)
            book.name += " from addBookOut"
            Log.d(TAG, "addBookOut: $book")
            notifyBooksChanged()
            return book.id
        }

        override fun addBookInOut(book: Book) {
            Log.d(TAG, "addBookInOut: start in ${Thread.currentThread()} $book")
            Thread.sleep(3000)
            books.add(book)
            book.name += " from addBookInOut"
            Log.d(TAG, "addBookInOut: $book")
            notifyBooksChanged()
        }

        override fun addBookOneWay(book: Book) {
            Log.d(TAG, "addBookOneWay: start in ${Thread.currentThread()} $book")
            Thread.sleep(3000)
            books.add(book)
            book.name += " from addBookOneWay"
            Log.d(TAG, "addBookOneWay: $book")
            notifyBooksChanged()
        }

        override fun getBookList(): MutableList<Book> {
            Log.d(TAG, "getBookListOneWay: ${Thread.currentThread()}")
            Thread.sleep(3000)
            return books
        }

        override fun registerBookChangeListener(listener: IBookChangeListener?) {
            Log.d(TAG, "registerBookChangeListener: ")
            remoteCallbackList.register(listener)
        }

        override fun unregisterBookChangeListener(listener: IBookChangeListener?) {
            Log.d(TAG, "unregisterBookChangeListener: ")
            remoteCallbackList.unregister(listener)
        }
    }

    private fun notifyBooksChanged() {
        val cb: Int = remoteCallbackList.beginBroadcast()
        for (i in 0 until cb) {
            try {
                remoteCallbackList.getBroadcastItem(i).onBookChanged(books)
                remoteCallbackList.getBroadcastItem(i).onBookChangedOneWay(books)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
        remoteCallbackList.finishBroadcast()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }
}