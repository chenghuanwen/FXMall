package com.dgkj.fxmall.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.PictureChooserAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.ImageFolder;
import com.dgkj.fxmall.constans.Permission;
import com.dgkj.fxmall.constans.Position;
import com.dgkj.fxmall.listener.OnPictureScanFinishedListener;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.PermissionUtil;
import com.dgkj.fxmall.utils.PictureScanUtil;
import com.dgkj.fxmall.view.myView.ListDirPopWindown;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PictrueChooserActivity extends BaseActivity implements ListDirPopWindown.OnFolderSelectListener{

    private static final int FX_REQUEST_PERMISSION_CODE = 101;

    @BindView(R.id.gv_picture)
    GridView gvPicture;
    @BindView(R.id.folder_name)
    TextView folderName;
    @BindView(R.id.sum_count)
    TextView sumCount;
    private View headerview;

    private ProgressDialog pd;
    private File currentDir;//当前文件夹
    private List<String> images;//所有图片
    private PictureChooserAdapter mAdapter;
    private ArrayList<ImageFolder> imageFolders;//所有图片文件夹
    private ListDirPopWindown mDirWindow;
    private int screenHeight;
    private ArrayList<String> mSelectImgs;//已选中的图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictrue_chooser);
        ButterKnife.bind(this);
    //    pd = ProgressDialog.show(this,"","正在加载...");

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;

        initHeaderview();

        if(PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(this, Permission.PERMISSIONS_READ_STORAGE)){
            Toast.makeText(this,"允许访问sd卡，才能进行此操作！",Toast.LENGTH_SHORT).show();
            requestPermissionsAndroidM();
            return;
        }else {
            pictureScan();
        }

    }

    private void initHeaderview() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview,"选择图片");
        setHeaderImage(headerview, R.mipmap.title_return_icon,"", Position.LEFT, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setHeaderImage(headerview, -1,"确定", Position.RIGHT, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectImgs = mAdapter.getSelectImgs();
                //TODO 上传已选则图片
                Intent intent = new Intent();
                intent.putStringArrayListExtra("images",mSelectImgs);
                setResult(121,intent);
                finish();
            }
        });

    }

    private void pictureScan() {

       new PictureScanUtil().scanStart(new OnPictureScanFinishedListener() {
           @Override
           public void onPictureScanFinish(ArrayList<ImageFolder> folders, int totalCount, File mImgDir) {
            //   pd.dismiss();
               imageFolders = folders;
               setData(mImgDir,totalCount);//设置数据，显示图片数量最多的文件夹的所有图片
               initListDirPopupWindw();//初始化文件就列表弹窗
           }
       });
    }

    private void initListDirPopupWindw() {
        mDirWindow = new ListDirPopWindown(ViewGroup.LayoutParams.MATCH_PARENT, (int) (screenHeight*0.7),imageFolders,
                getLayoutInflater().inflate(R.layout.layout_folder_popuwindow,null));

        mDirWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //设置背景色变亮
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.alpha = 1.0f;
                getWindow().setAttributes(layoutParams);
            }
        });
        mDirWindow.setOnFolderSelectListener(this);

    }

    private void setData(final File mImgDir, final int totalCount) {
        images = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //文件夹路径和图片路径分开保存，极大地减少了内存的消耗
                mAdapter = new PictureChooserAdapter(MyApplication.getContext(),R.layout.item_picture_chooser,images,mImgDir.getAbsolutePath());
                gvPicture.setAdapter(mAdapter);
                sumCount.setText(totalCount+"张");
                folderName.setText(mImgDir.getAbsolutePath().substring(mImgDir.getAbsolutePath().lastIndexOf("/")));
            }
        });

    }


    @OnClick(R.id.folder_name)
    public void onFolderNameClick(){
        mDirWindow.showAsDropDown(folderName,0,0);
        //设置背景色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onFolderSelected(ImageFolder folder) {
        currentDir = new File(folder.getDirPath());
        images = Arrays.asList(currentDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")){
                    return true;
                }
                return false;
            }
        }));


        mAdapter = new PictureChooserAdapter(MyApplication.getContext(),R.layout.item_picture_chooser,images,currentDir.getAbsolutePath());
        gvPicture.setAdapter(mAdapter);
        //mAdapter.notifyDataSetChanged();
        sumCount.setText(folder.getCount()+"张");
        folderName.setText(folder.getName());
        mDirWindow.dismiss();

    }




    /**
     * 申请6.0动态权限
     */
    private void requestPermissionsAndroidM() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){
            List<String> needPermissionRequest = new ArrayList<>();
            needPermissionRequest.add(Permission.PERMISSIONS_READ_STORAGE);
           // needPermissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
           // needPermissionRequest.add(Manifest.permission.CAMERA);
            PermissionUtil.requestPermissions(PictrueChooserActivity.this,FX_REQUEST_PERMISSION_CODE,needPermissionRequest);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == FX_REQUEST_PERMISSION_CODE){
            permissionsResult(permissions,grantResults);

        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 响应权限请求结果
     * @param permissions 申请的权限集合
     * @param grantResults 权限授权结果集合
     */
    private void permissionsResult(String[] permissions, int[] grantResults) {
        List<String> needPermission = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    needPermission.add(permissions[i]);
                }
            }
        }

        if(needPermission.size()>0){
            StringBuffer permissionMsg = new StringBuffer();
            for (int i = 0; i < needPermission.size(); i++) {
                if(needPermission.get(i).equals(Permission.PERMISSIONS_READ_STORAGE)){
                    permissionMsg.append(","+"请求访问相册");
                }else if(needPermission.get(i).equals(Permission.PERMISSIONS_WRITE_STORAGE)){
                    permissionMsg.append(","+"请求访问SD卡");
                }else if(needPermission.get(i).equals(Manifest.permission.CAMERA)){
                    permissionMsg.append(","+"请求打开相机");
                }
            }
            String strMessage = "请允许使用\"" + permissionMsg.substring(1).toString() + "\"权限, 以正常使用APP的所有功能.";

            Toast.makeText(PictrueChooserActivity.this, strMessage, Toast.LENGTH_SHORT).show();
        }else {
           pictureScan();
        }
    }


}
