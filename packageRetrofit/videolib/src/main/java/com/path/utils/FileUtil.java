package com.path.utils;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.path.base.AppContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * Created by Jin on 2016/5/25.
 */
public class FileUtil {
    // 缓存文件路径
    public static final String i8CHAT_IMAGE_CACHE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "Android"
            + File.separator
            + "data"
            + File.separator
            + AppUtil.getAppName()
            + File.separator;
    /**
     * 保存文件到本地  如果本地有此图片，则直接返回此图片路径
     *
     * @param bitmap 位图
     * @param picurl 图片的网络路径
     * @return 文件存储路径
     */
    public static String saveJPG(Bitmap bitmap, String picurl) {
        File f = getFile(picurl,null);
        if (f.exists()) {
            if (f.length() > 1)//如果图片存在 且大小大于1kb 则不再下载最新图片
                return f.getPath();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (bitmap == null || bitmap.isRecycled()) {
            Log.e("FileUtil saveImage", "bitmap is recycled!!");
            return null;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            if (fOut != null) fOut.flush();
            else Log.e("FileUtil saveJPG()", "FileOutputStream is null");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (fOut != null) fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.recycle();
        return f.getPath();

    }

    /**
     * 保存文件到本地  如果本地有此图片，则直接返回此图片路径
     *
     * @param bitmap 位图
     * @param picurl 图片的网络路径
     * @param length 图片大小限制  32*1024
     * @return 文件存储路径
     */
    public static String saveJPG(Bitmap bitmap, String picurl, int length) {
        File f = getFile(picurl,null);
        if (f.exists()) {
            if (f.length() > 1)//如果图片存在 且大小大于1kb 则不再下载最新图片
                return f.getPath();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (bitmap == null || bitmap.isRecycled()) {
            Log.e("FileUtil saveImage", "bitmap is recycled!!");
            return null;
        }
        bitmap = ImageUtil.compressBitmap(bitmap, length);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            if (fOut != null) fOut.flush();
            else Log.e("FileUtil saveJPG()", "FileOutputStream is null");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (fOut != null) fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.recycle();
        return f.getPath();

    }
    public static String saveImage(Bitmap bitmap,String picUrl,boolean userCache){
        return  saveImage(bitmap,picUrl,null,userCache);
    }

    public static String saveImage(Bitmap bitmap, String picUrl,String split ,boolean useCache) {
        File f = getFile(picUrl,split);
        if (f.exists()){
            if (useCache && f.length() > 1)//如果图片存在 且大小大于1kb 则不再下载最新图片
                if (bitmap!=null) bitmap.recycle();
                return f.getPath();
        }
        if (bitmap == null || bitmap.isRecycled()) {
            Log.e("FileUtil saveImage", "bitmap is recycled!!");
            return null;
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fOut == null){
            Log.e("FileUtil saveImage", "fOut is null!!");
            return null;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            if (fOut != null) fOut.flush();
            else Log.e("FileUtil saveImage()", "FileOutputStream is null");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (fOut != null) fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.recycle();
        return f.getPath();
    }

    /**
     * 保存文件到本地图库  如果本地有此图片，则直接返回此图片路径
     *
     * @param bitmap 位图
     * @param picurl 图片的网络路径
     * @return 文件存储路径
     */
    public static String saveImage(Bitmap bitmap, String picurl) {
        return saveImage(bitmap, picurl, true);
    }

    /**
     * 获取文件的路径
     *
     * @param url
     * @param url
     * @return
     */
    public static String getFilePath(String url) {
        return getFilePath(url,null);
    }

    public static File getFile(String url){
        return getFile(url,null);
    }

    /**
     *
     * @param url
     * @param split
     * @return
     */
    private static File getFile(String url,String split){
        File appDir;
        if (StringUtil.isEmpty(split)){
            appDir  = new File(getDiskCacheDir());
        }else{
            appDir=new File(getDiskCacheDir(),split);
        }
        if (!appDir.exists()){
            L.e("创建目录："+appDir.mkdir());
        }
        String fileName = NameUtil.getPicName(url, true);
        return new File(appDir, fileName);
    }
    /**
     * 获取文件的路径
     *
     * @param url
     * @param split  文件名
     * @return
     */
    public static String getFilePath(String url,String split) {
        File appDir;
        if (StringUtil.isEmpty(split)){
            appDir  = new File(getDiskCacheDir());
        }else{
            appDir=new File(getDiskCacheDir(),split);
        }
        if (!appDir.exists()){
            appDir.mkdir();
        }
        String fileName = NameUtil.getPicName(url, true);
        File f = new File(appDir, fileName);
        if (f.exists()) {
            if (f.length() > 1)//如果图片存在 且大小大于1kb 则不再下载最新图片
                return f.getPath();
        }
        return null;
    }

    public static InputStream getFileStream(String url) {
        File appDir = new File(getDiskCacheDir());
        if (!appDir.exists()) return null;//如果目录不存在 返回空

        String fileName = NameUtil.getPicName(url, true);
        File f = new File(appDir, fileName);
        if (!f.exists()) return null;//如果文件不存在，返回空
        if (f.length() < 1) return null;//如果图片存在 且大小大于1kb 则不再下载最新图片

        try {
            return new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除指定文件的图片
     *
     * @param path
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) file.delete();
    }

    /**
     * 更新图库
     *
     * @param path
     */
    public static void updateGallery(String path) {
        AppContext.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }

    public static String getDiskCacheDir() {
        File cacheDir=null;
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cacheDir = AppContext.getContext().getExternalCacheDir();
        } else {
            cacheDir = AppContext.getContext().getCacheDir();
        }
        if (cacheDir!=null && cacheDir.exists()){
            return cacheDir.getPath();
        }
        return i8CHAT_IMAGE_CACHE_PATH;
       // return cachePath;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @return
     */
    public static void deleteCacheFile(File dir) throws IOException {
            File[] files = dir.listFiles();
            if (files == null) {
                throw new IOException("not a readable directory: " + dir);
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteCacheFile(file);
                }
                String path=file.getPath();
                L.i("delete file yes? "+file.delete(),"  file path=>"+path);
            }
    }

    /**
     * 获取单个文件的MD5值！
     *
     * @param file
     * @return
     */

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 复制文件
     * @param fromFilePath
     * @param toFilePath
     * @return
     */
    public static boolean copyFile(String fromFilePath, String toFilePath) {
        if(!TextUtils.isEmpty(fromFilePath) && !TextUtils.isEmpty(toFilePath)) {
            if(!(new File(fromFilePath)).exists()) {
                return false;
            } else {
                try {
                    FileInputStream e = new FileInputStream(fromFilePath);
                    FileOutputStream fosto = new FileOutputStream(toFilePath);
                    copyFile(e, fosto);
                    return true;
                } catch (Throwable var4) {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public static void copyFile(FileInputStream src, FileOutputStream dst) throws Throwable {
        byte[] buf = new byte[65536];

        for(int len = src.read(buf); len > 0; len = src.read(buf)) {
            dst.write(buf, 0, len);
        }

        src.close();
        dst.close();
    }

    /**
     * 获取文件大小
     * @param path
     * @return
     * @throws Throwable
     */
    public static long getFileSize(String path) throws Throwable {
        if(TextUtils.isEmpty(path)) {
            return 0L;
        } else {
            File file = new File(path);
            return getFileSize(file);
        }
    }

    /**
     * 获取文件大小
     * @param file
     * @return
     * @throws Throwable
     */
    public static long getFileSize(File file) throws Throwable {
        if(!file.exists()) {
            return 0L;
        } else if(!file.isDirectory()) {
            return file.length();
        } else {
            String[] names = file.list();
            int size = 0;

            for(int i = 0; i < names.length; ++i) {
                File f = new File(file, names[i]);
                size = (int)((long)size + getFileSize(f));
            }

            return (long)size;
        }
    }

    /**
     * 把对象保存成文件
     * @param filePath
     * @param object
     * @return
     */
    public static boolean saveObjectToFile(String filePath, Object object) {
        if(!TextUtils.isEmpty(filePath)) {
            File cacheFile = null;

            try {
                cacheFile = new File(filePath);
                if(cacheFile.exists()) {
                    cacheFile.delete();
                }

                if(!cacheFile.getParentFile().exists()) {
                    cacheFile.getParentFile().mkdirs();
                }

                cacheFile.createNewFile();
            } catch (Throwable var6) {
                var6.printStackTrace();
                cacheFile = null;
            }

            if(cacheFile != null) {
                try {
                    FileOutputStream t = new FileOutputStream(cacheFile);
                    GZIPOutputStream gzos = new GZIPOutputStream(t);
                    ObjectOutputStream oos = new ObjectOutputStream(gzos);
                    oos.writeObject(object);
                    oos.flush();
                    oos.close();
                    return true;
                } catch (Throwable var7) {
                    var7.printStackTrace();
                }
            }
        }

        return false;
    }

    /**
     * 读取文件返回对象
     * @param filePath
     * @return
     */
    public static Object readObjectFromFile(String filePath) {
        if(!TextUtils.isEmpty(filePath)) {
            File cacheFile = null;

            try {
                cacheFile = new File(filePath);
                if(!cacheFile.exists()) {
                    cacheFile = null;
                }
            } catch (Throwable var6) {
                var6.printStackTrace();
                cacheFile = null;
            }

            if(cacheFile != null) {
                try {
                    FileInputStream t = new FileInputStream(cacheFile);
                    GZIPInputStream gzis = new GZIPInputStream(t);
                    ObjectInputStream ois = new ObjectInputStream(gzis);
                    Object object = ois.readObject();
                    ois.close();
                    return object;
                } catch (Throwable var7) {
                    var7.printStackTrace();
                }
            }
        }

        return null;
    }
    /** *//**
     * 文件转化为字节数组

     */
    public static byte[] getBytesFromFile(File f){
        if (f == null){
            return null;
        }
        FileInputStream stream=null;
        ByteArrayOutputStream out=null;
        try{
           stream = new FileInputStream(f);
            out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);

            return out.toByteArray();
        } catch (IOException e){
        }finally {
            try {
                if (stream!=null)stream.close();
                if(out!=null)out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
