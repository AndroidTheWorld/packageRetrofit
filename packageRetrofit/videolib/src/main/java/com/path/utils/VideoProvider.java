package com.path.utils;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;

import com.path.base.AppContext;

import java.util.List;

/**
 * Created by 邱锦洋 on 2016/7/25.
 */
public class VideoProvider {

    private static Context mContext= AppContext.getContext();


    private String getVideoImage(String videoPath,int width,int height,int kind){
       Bitmap bitmap= ThumbnailUtils.createVideoThumbnail(videoPath,kind);
       return FileUtil.saveImage(bitmap,videoPath+".jpg",true);
    }

    public interface Callback{
        void onResult(List<Video> lists);
        void onAddItem(Video video);
    }

    public Video getVideo(Uri uri){
        Cursor cursor = mContext.getContentResolver().query(uri, null, null,
                null, null);
        if (cursor != null && cursor.moveToNext()) {
            String path=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            String exit= NameUtil.getFormat(path);
            int id=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            String title=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            String album=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
            String artist=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
            String displayName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
            String mimeType=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
            long duration=cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))/1000;
            long size=cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

            int minute= (int) (duration/60);
            int scend = (int) (duration-minute*60);
            String time= StringUtil.buffer(String.valueOf(minute),":",String.valueOf(scend));
            String image=getVideoImage(path, ResUtil.dip2px(60),ResUtil.dip2px(60), MediaStore.Video.Thumbnails.MINI_KIND);
            cursor.close();
            return new Video(id,title,album,artist,displayName,mimeType,path,size,duration,time,image);
        }
        return null;
    }
}
