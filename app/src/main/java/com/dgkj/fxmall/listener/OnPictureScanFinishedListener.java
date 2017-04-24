package com.dgkj.fxmall.listener;

import com.dgkj.fxmall.bean.ImageFolder;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Android004 on 2017/3/8.
 */

public interface OnPictureScanFinishedListener {
    void onPictureScanFinish(ArrayList<ImageFolder> folders, int totalCount, File mImgDir);

}
