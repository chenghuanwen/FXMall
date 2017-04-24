package com.dgkj.fxmall.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限管理工具 (针对Android 6.0 系统)
 * Created by AlexTam on 2016/10/14.
 */
public class PermissionUtil {
    private static PermissionUtil permissionUtil = null;


    public static final boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

    }


    /**
     *
     * @param activity
     * @param permissionName
     * @return
     */
    public static final boolean isPermissionValid(Activity activity, String permissionName) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, permissionName);
                if (checkCallPhonePermission == PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 查找设备禁止的权限.
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public static final List<String> findDeniedPermissions(Activity activity, List<String> permissions) {
        if (permissions == null || permissions.size() == 0) {
            return null;
        } else {
            List<String> denyPermissions = new ArrayList<>();

            for (String value : permissions) {
                try {
                    if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                        denyPermissions.add(value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return denyPermissions;
        }
    }

    /**
     * 申请权限
     * @param activity
     * @param requestCode
     * @param mListPermissions
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public static final void requestPermissions(Activity activity, int requestCode, List<String> mListPermissions) {
        if (mListPermissions == null || mListPermissions.size() == 0) {
            return;
        }

        if (!isOverMarshmallow()) {
            // Android 6.0以下不用申请.
            return;

        } else {
            List<String> deniedPermissionList = findDeniedPermissions(activity, mListPermissions);

            if (deniedPermissionList != null && deniedPermissionList.size() > 0) {
                LogUtil.i("TAG","请求权限==="+deniedPermissionList.toString());
                activity.requestPermissions(deniedPermissionList.toArray(new String[deniedPermissionList.size()]), requestCode);

            }
        }

    }


}
