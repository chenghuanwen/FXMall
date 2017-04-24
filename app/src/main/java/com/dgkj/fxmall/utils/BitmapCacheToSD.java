package com.dgkj.fxmall.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/** 将图片缓存到本地
 * Created by 成焕文 on 2017/3/7.
 */
public class BitmapCacheToSD {
    public static double FREE_SD_SPACE_NEEDED_TO_CACHE = 50;//50MB
    public static double MB = 1024 * 1024;

    public void saveBitmap2Sd(Bitmap bm, String url) {
        if (bm == null || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            return;
        }
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }
        String filename = convertUrl2Filename(url);

     //   Log.e("sf", "++++filename+++++" + filename);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filename);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            out.write(baos.toByteArray());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取缓存文件夹
     *
     * @param
     * @return
     */
    private File getDirectory(String filename) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filename);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 将请求路径添加到文件名中，作为存取时的标识
     *
     * @param url
     * @return
     */
    private String convertUrl2Filename(String url) {
        if (url == null || url.length() == 0) {
            return "";
        }
        return url.substring(40);//http://123.206.58.158/doupaiapp//upload/201609282007207811475064407436.mp4
    }

    /**
     * 计算sd卡剩余空间
     *
     * @return
     */
    private double freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        LogUtil.i("TAG", "sd卡剩余空间========" + sdFreeMB);
        return sdFreeMB;
    }

    /**
     * 更新文件最后修改的时间
     *
     * @param dir      文件夹名
     * @param filename 文件名
     */
    public void updateFileTime(String dir, String filename) {
        File file = new File(dir, filename);
        long updateTime = System.currentTimeMillis();
        file.setLastModified(updateTime);
    }

    /**
     * 计算存储目录下的文件大小，当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
     * 那么删除40%最近没有被使用的文件
     *
     * @param
     * @param
     */
    public void removeCache() {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();

        File dir = new File(root);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
      //  Log.i("TAG", "共缓存数量=======" + files.length);
        int dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            dirSize += files[i].length();
        }
        if (dirSize > FREE_SD_SPACE_NEEDED_TO_CACHE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            int removeFactor = (int) ((0.5 * files.length) + 1);
         //   Log.i("TAG", "开始清除缓存==========");
            Arrays.sort(files, new FileLastModifSort());

            for (int i = 0; i < removeFactor; i++) {
                files[i].delete();

            }

        }

    }

    /**
     * 删除过期文件
     *
     * @param
     * @param
     */
    public void removeExpiredCache(String filenaem) {

        File file = getDirectory(filenaem);

        long mTimeDiff = 2 * 60 * 60 * 1000;
        if (System.currentTimeMillis() - file.lastModified() > mTimeDiff) {

            LogUtil.i("TAG", "Clear some expiredcache files ");

            file.delete();

        }

    }

    /**
     * TODO 根据文件的最后修改时间进行排序 *
     */
    class FileLastModifSort implements Comparator<File>

    {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /**
     * 从缓存获取图片
     * @param url
     * @return
     */
    public Bitmap getBitmapFromCache(String url){
        Bitmap bm = null;
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),convertUrl2Filename(url));
        if(file.exists()){
           // bm =  BitmapFactory.decodeFile(file.getAbsolutePath());
            try {
             bm  = BitmapFactory.decodeStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    /**
     * 从缓存获取图片
     * @param imgPath
     * @return
     */
    public Bitmap getcopressImage(String imgPath) {
        Bitmap bitmap=null;
        File picture = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), convertUrl2Filename(imgPath));
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        bitmap =  BitmapFactory.decodeFile(picture.getAbsolutePath(),newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(picture.getAbsolutePath(), newOpts);
        //return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
        return bitmap;
    }

    /**删除缓存
     * @param path
     */
    public void deleteCache(String path){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),convertUrl2Filename(path));
        if(file.exists()){
           file.delete();
        }
    }
}