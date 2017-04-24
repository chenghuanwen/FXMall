package com.dgkj.fxmall.bean;

/** 图片选择器文件夹实体类
 * Created by Android004 on 2017/3/8.
 */

public class ImageFolder {
    /**
     * 文件夹路径
     */
    private String dirPath;
    /**
     * 文件夹名字
     */
    private String name;
    /**
     * 第一张图片路径
     */
    private String firstPicturePath;
    /**
     * 当前文件夹内图片总数
     */
    private int count;

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
        int lastIndexOf = this.dirPath.lastIndexOf("/");
        this.name = this.dirPath.substring(lastIndexOf);
    }

    public String getFirstPicturePath() {
        return firstPicturePath;
    }

    public void setFirstPicturePath(String firstPicturePath) {
        this.firstPicturePath = firstPicturePath;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }
}
