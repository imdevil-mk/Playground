package com.imdevil.playground.media;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class MusicConnection {

    private static final String TAG = "MusicConnection";

    private MediaBrowserCompat mediaBrowser;

    public MusicConnection(Context context) {

        mediaBrowser = new MediaBrowserCompat(context,
                new ComponentName(context, MusicService.class),
                new MediaBrowserCompat.ConnectionCallback() {
                    @Override
                    public void onConnected() {
                        Log.d(TAG, "onConnected: ");
                    }

                    @Override
                    public void onConnectionSuspended() {
                        Log.d(TAG, "onConnectionSuspended: ");
                    }

                    @Override
                    public void onConnectionFailed() {
                        Log.d(TAG, "onConnectionFailed: ");
                    }
                }, null);
        mediaBrowser.connect();
    }


    public <T extends Parcelable> Observable<List<T>> fetchData(String action, Bundle extras) {
        Log.d(TAG, "fetchData: " + action);

        return Observable.create(emitter -> {
            Log.d(TAG, "fetchData: emitter");
            MediaBrowserCompat.CustomActionCallback callback = new MediaBrowserCompat.CustomActionCallback() {
                @Override
                public void onProgressUpdate(String action, Bundle extras, Bundle data) {
                    Log.d(TAG, "onProgressUpdate: ");
                }

                @Override
                public void onResult(String action, Bundle extras, Bundle resultData) {
                    Log.d(TAG, "onResult: ");

                    List<T> musics = resultData.getParcelableArrayList("Success");

                    emitter.onNext(musics);
                }

                @Override
                public void onError(String action, Bundle extras, Bundle data) {
                    Log.d(TAG, "onError: ");
                    String errorMsg = data.getString("error");
                    emitter.onError(new Throwable(errorMsg));
                }
            };

            mediaBrowser.sendCustomAction(action, extras, callback);
        });
    }

    public Observable<List<MediaBrowserCompat.MediaItem>> subscribe(String parentId, Bundle options) {
        Log.d(TAG, "subscribe: ");
        return Observable.create(emitter -> {

            MediaBrowserCompat.SubscriptionCallback callback = new MediaBrowserCompat.SubscriptionCallback() {

                @Override
                public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children, @NonNull Bundle options) {
                    Log.d(TAG, "onChildrenLoaded: " + parentId + " " + children.size());

                    emitter.onNext(children);

                    mediaBrowser.unsubscribe(parentId, this);
                }

                @Override
                public void onError(@NonNull String parentId) {
                    Log.d(TAG, "onError: " + parentId);

                    mediaBrowser.unsubscribe(parentId, this);
                }

                @Override
                public void onError(@NonNull String parentId, @NonNull Bundle options) {
                    Log.d(TAG, "onError: " + parentId);

                    mediaBrowser.unsubscribe(parentId, this);
                }
            };

            mediaBrowser.subscribe(parentId, options, callback);
        });
    }
}
