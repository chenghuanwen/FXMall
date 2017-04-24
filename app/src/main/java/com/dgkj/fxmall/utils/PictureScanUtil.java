package com.dgkj.fxmall.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.bean.ImageFolder;
import com.dgkj.fxmall.listener.OnPictureScanFinishedListener;

import java.io.File;

import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * 图片扫描工具
 * Created by 成焕文 on 2017/3/8.
 */

public class PictureScanUtil {
    private  Context context;
    /**
     * 图片总数量
     */
    private int totalCount;
    /**
     * 相册中图片数量
     */
    private int mPicsize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();
    private ArrayList<ImageFolder> imageFolders = new ArrayList<>();
    public  void scanStart(final OnPictureScanFinishedListener listener){
        context = MyApplication.getContext();
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Toast.makeText(context,"暂无外部储存！",Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(){
            @Override
            public void run() {
                String firstImage = null;
                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver resolver = context.getContentResolver();
                Cursor mCursor = resolver.query(imageUri,null,MediaStore.Images.Media.MIME_TYPE+"=? or "+ MediaStore.Images.Media.MIME_TYPE+"=?",
                        new String[]{"image/jpeg","image/png"},MediaStore.Images.Media.DATE_MODIFIED);
                LogUtil.i("TAG","mCursorCount=="+mCursor.getCount());
                while (mCursor.moveToNext()){
                    //获取图片路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                  //  if(firstImage == null)
                        firstImage = path;
                    //获取第一张图片的父文件夹路径
                    File parentFile = new File(path).getParentFile();
                    if(parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFolder imageFolder = null;
                    //利用一个HashSet防止多次扫描同一个文件夹
                    if(mDirPaths.contains(dirPath)){
                        continue;
                    }else {
                        mDirPaths.add(dirPath);
                        imageFolder = new ImageFolder();
                        imageFolder.setDirPath(dirPath);
                        imageFolder.setFirstPicturePath(firstImage);
                    }
                    if(parentFile.list()==null)continue;
                    int pictureSize = parentFile.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            if(name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith("png"))
                                return true;
                            return false;
                        }
                    }).length;
                    totalCount += pictureSize;
                    imageFolder.setCount(pictureSize);
                    imageFolders.add(imageFolder);
                    if(pictureSize > mPicsize){
                        mPicsize = pictureSize;
                        mImgDir = parentFile;

                    }
                }
                mCursor.close();
                //扫描完成，辅助的hashSet也可以释放了
                mDirPaths = null;
                //通知调用者图片扫描完成
                listener.onPictureScanFinish(imageFolders,totalCount,mImgDir);

            }
        }.start();
    }
}
