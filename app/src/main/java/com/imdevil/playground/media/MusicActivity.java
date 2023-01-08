package com.imdevil.playground.media;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.imdevil.playground.R;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class MusicActivity extends AppCompatActivity {

    private static final String TAG = "MusicActivity";
    private MusicConnection musicConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        startService(new Intent(this, MusicService.class));

        musicConnection = new MusicConnection(this);
    }

    public void sendCustomActionSuccess(View view) {
        musicConnection.<Music>fetchData("Success", new Bundle()).subscribe(new Observer<List<Music>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(@NonNull List<Music> music) {
                Log.d(TAG, "onNext: " + music.size());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }

    public void sendCustomActionFail(View view) {
        musicConnection.<Music>fetchData("Fail", new Bundle()).subscribe(new Observer<List<Music>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(@NonNull List<Music> music) {
                Log.d(TAG, "onNext: " + music.size());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });

    }

    public void subscribeSuccess(View view) {
        musicConnection.subscribe("Success", new Bundle()).subscribe(new Observer<List<MediaBrowserCompat.MediaItem>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(@NonNull List<MediaBrowserCompat.MediaItem> mediaItems) {
                Log.d(TAG, "onNext: " + mediaItems.size());

                mediaItems.forEach(it -> Log.d(TAG, "onNext: " + it));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.toString());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });

    }

    public void subscribeFail(View view) {

        musicConnection.subscribe("Fail", new Bundle()).subscribe(new Observer<List<MediaBrowserCompat.MediaItem>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(@NonNull List<MediaBrowserCompat.MediaItem> mediaItems) {

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.toString());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }
}