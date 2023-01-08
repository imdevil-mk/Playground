package com.imdevil.playground.media;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Music implements Parcelable {
    public String title;
    public String singer;
    public long duration;
    public String url;

    public Music(String title, String singer, long duration, String url) {
        this.title = title;
        this.singer = singer;
        this.duration = duration;
        this.url = url;
    }

    protected Music(Parcel in) {
        title = in.readString();
        singer = in.readString();
        duration = in.readLong();
        url = in.readString();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(singer);
        dest.writeLong(duration);
        dest.writeString(url);
    }

    @Override
    public String toString() {
        return "Music{" +
                "title='" + title + '\'' +
                ", singer='" + singer + '\'' +
                ", duration=" + duration +
                ", url='" + url + '\'' +
                '}';
    }

    public static ArrayList<Music> mock() {
        int size = 10;
        ArrayList<Music> musics = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            musics.add(new Music("music" + i, "singer", System.currentTimeMillis(), ""));
        }
        return musics;
    }

    public static Music map(MediaDescriptionCompat mdc) {
        Bundle extras = mdc.getExtras();
        return new Music(mdc.getTitle().toString(), extras.getString("singer"), extras.getLong("duration"), mdc.getIconUri().toString());
    }

    public static Music fromMediaItem(MediaBrowserCompat.MediaItem mediaItem) {
        return map(mediaItem.getDescription());
    }

    public static MediaBrowserCompat.MediaItem toMediaItem(Music music) {
        Bundle extras = new Bundle();
        extras.putString("singer", music.singer);
        extras.putLong("duration", music.duration);

        MediaDescriptionCompat mdc = new MediaDescriptionCompat.Builder()
                .setMediaId(music.title)
                .setTitle(music.title)
                .setIconUri(Uri.parse(music.url))
                .setExtras(extras)
                .build();

        MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mdc, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);

        return mediaItem;
    }
}
