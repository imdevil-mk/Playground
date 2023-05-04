package com.imdevil.playground.service.aidl

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.imdevil.playground.databinding.ActivityClientBinding

private const val TAG = "AIDL-ClientActivity"

class ClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientBinding
    private lateinit var remoteService: IBookManager

    private val deathRecipient: IBinder.DeathRecipient = IBinder.DeathRecipient {
        binding.addBookIn.isEnabled = false
        binding.addBookOut.isEnabled = false
        binding.addBookInOut.isEnabled = false
        binding.addBookOneWay.isEnabled = false
        binding.getBookListOneWay.isEnabled = false
    }

    private val bookChangeListener: IBookChangeListener = object : IBookChangeListener.Stub() {
        override fun onBookChanged(books: MutableList<Book>?) {
            Log.d(TAG, "onBookChanged: start ${Thread.currentThread()}")
            Thread.sleep(3000)
            Log.d(TAG, "onBookChanged: end   ${Thread.currentThread()} $books")
        }

        override fun onBookChangedOneWay(books: MutableList<Book>?) {
            Log.d(TAG, "onBookChangedOneWay: start ${Thread.currentThread()}")
            Thread.sleep(3000)
            Log.d(TAG, "onBookChangedOneWay: end   ${Thread.currentThread()} $books")
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected: ")
            service?.linkToDeath(deathRecipient, 0)
            remoteService = IBookManager.Stub.asInterface(service)
            remoteService.registerBookChangeListener(bookChangeListener)

            binding.addBookIn.isEnabled = true
            binding.addBookOut.isEnabled = true
            binding.addBookInOut.isEnabled = true
            binding.addBookOneWay.isEnabled = true
            binding.getBookListOneWay.isEnabled = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected: ")
        }
    }

/*
1, AIDL方法默认是同步的，即调用线程会等待AIDL方法的执行完成
2，在方法参数上标记in，可以将这个对象写入_data传递到服务端，服务端对对象的修改不会回传到客户端
3，在方法参数上标记out，不会将这个对象写入_data，所以服务端无法收到这个对象。实际上服务端接收到的入参是构造的一个空对象，
   在服务端对这个空对象修改后，会被写入_reply，被客户端接收，然后客户端的参数从中readFromParcel
4，oneway让方法变为异步，代价是方法不能有返回值，同时参数只能是in的
5，oneway修饰的回调发生在客户端的Binder线程池，否则发生客户端主线程
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, RemoteService::class.java)
        bindService(intent, connection, BIND_AUTO_CREATE)

        binding.addBookIn.setOnClickListener {
            val book = Book().apply {
                id = 10001
                name = "Thinking in Java"
            }
            Log.d(TAG, "addBookIn start ${Thread.currentThread()} $book")
            remoteService.addBookIn(book)
            Log.d(TAG, "addBookIn end   ${Thread.currentThread()} $book")
        }

        binding.addBookOut.setOnClickListener {
            val book = Book().apply {
                id = 10002
                name = "Effective Java"
            }
            Log.d(TAG, "addBookOut start ${Thread.currentThread()} $book")
            remoteService.addBookOut(book)
            Log.d(TAG, "addBookOut end   ${Thread.currentThread()} $book")
        }

        binding.addBookInOut.setOnClickListener {
            val book = Book().apply {
                id = 100003
                name = "Crazy Android"
            }
            Log.d(TAG, "addBookInOut start ${Thread.currentThread()} $book")
            remoteService.addBookInOut(book)
            Log.d(TAG, "addBookInOut end   ${Thread.currentThread()} $book")
        }

        binding.addBookOneWay.setOnClickListener {
            val book = Book().apply {
                id = 100004
                name = "Code"
            }
            Log.d(TAG, "addBookOneWay start ${Thread.currentThread()} $book")
            remoteService.addBookOneWay(book)
            Log.d(TAG, "addBookOneWay end   ${Thread.currentThread()} $book")
        }

        binding.getBookListOneWay.setOnClickListener {
            Log.d(TAG, "getBookListOneWay start ${Thread.currentThread()}")
            val list = remoteService.bookList
            Log.d(TAG, "getBookListOneWay end   ${Thread.currentThread()} $list")
        }
    }
}