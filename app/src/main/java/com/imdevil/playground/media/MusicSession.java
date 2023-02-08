package com.imdevil.playground.media;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaSessionCompat;

public class MusicSession extends MediaSessionCompat.Callback {

    public MusicSession(Context context) {
        super();
    }

    @Override
    public void onCommand(String command, Bundle extras, ResultReceiver cb) {
        super.onCommand(command, extras, cb);
    }

    @Override
    public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
        return super.onMediaButtonEvent(mediaButtonEvent);
    }

    @Override
    public void onPrepare() {
        super.onPrepare();
    }

    @Override
    public void onPrepareFromMediaId(String mediaId, Bundle extras) {
        super.onPrepareFromMediaId(mediaId, extras);
    }

    @Override
    public void onPrepareFromSearch(String query, Bundle extras) {
        super.onPrepareFromSearch(query, extras);
    }

    @Override
    public void onPrepareFromUri(Uri uri, Bundle extras) {
        super.onPrepareFromUri(uri, extras);
    }

    @Override
    public void onPlay() {
        super.onPlay();
    }

    @Override
    public void onPlayFromMediaId(String mediaId, Bundle extras) {
        super.onPlayFromMediaId(mediaId, extras);
    }

    @Override
    public void onPlayFromSearch(String query, Bundle extras) {
        super.onPlayFromSearch(query, extras);
    }

    @Override
    public void onPlayFromUri(Uri uri, Bundle extras) {
        super.onPlayFromUri(uri, extras);
    }

    @Override
    public void onSkipToQueueItem(long id) {
        super.onSkipToQueueItem(id);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSkipToNext() {
        super.onSkipToNext();
    }

    @Override
    public void onSkipToPrevious() {
        super.onSkipToPrevious();
    }

    @Override
    public void onFastForward() {
        super.onFastForward();
    }

    @Override
    public void onRewind() {
        super.onRewind();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSeekTo(long pos) {
        super.onSeekTo(pos);
    }

    @Override
    public void onSetRating(RatingCompat rating) {
        super.onSetRating(rating);
    }

    @Override
    public void onSetRating(RatingCompat rating, Bundle extras) {
        super.onSetRating(rating, extras);
    }

    @Override
    public void onSetPlaybackSpeed(float speed) {
        super.onSetPlaybackSpeed(speed);
    }

    @Override
    public void onSetCaptioningEnabled(boolean enabled) {
        super.onSetCaptioningEnabled(enabled);
    }

    @Override
    public void onSetRepeatMode(int repeatMode) {
        super.onSetRepeatMode(repeatMode);
    }

    @Override
    public void onSetShuffleMode(int shuffleMode) {
        super.onSetShuffleMode(shuffleMode);
    }

    @Override
    public void onCustomAction(String action, Bundle extras) {
        super.onCustomAction(action, extras);
    }

    @Override
    public void onAddQueueItem(MediaDescriptionCompat description) {
        super.onAddQueueItem(description);
    }

    @Override
    public void onAddQueueItem(MediaDescriptionCompat description, int index) {
        super.onAddQueueItem(description, index);
    }

    @Override
    public void onRemoveQueueItem(MediaDescriptionCompat description) {
        super.onRemoveQueueItem(description);
    }
}
