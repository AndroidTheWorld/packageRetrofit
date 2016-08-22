package com.path.utils;

/**
 * Created by Jin on 2016/5/25.
 */
public class NameUtil {
    /**
     *
     * @param url   图片路径
     * @param hasexit 是否有扩展名  true 有 false 无
     * @return 名字
     */
    public static String getPicName(String url,boolean hasexit){
        L.e("NameUtil getPicName url==>",url);
        String name=MD5Util.encrypt(url);
        String extension=getFormat(url);
       // L.i("文件扩展名为："+extension);
        if (!StringUtil.isEmpty(extension) && hasexit && extension.length()<5) return StringUtil.buffer(name,extension);
        return name;
       // return extension;
    }

    /**
     * 根据路径得到文件格式
     * @param url
     * @return   返回结果 例如：.png
     */
    public static String getFormat(String url){
        String extension="";
        if (url.length() > 0) {
            int dot = url.lastIndexOf('.');
            if ((dot >-1) && (dot < (url.length() - 1))) {
                extension= url.substring(dot);
            }
        }
        return extension;
    }
}
