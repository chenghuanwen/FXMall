package com.dgkj.fxmall.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.OrderBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.constans.Permission;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.OkhttpUploadUtils;
import com.dgkj.fxmall.utils.PermissionUtil;
import com.dgkj.fxmall.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplyRefundActivity extends BaseActivity implements View.OnClickListener{
    private static final int TM_REQUEST_PERMISSION_CODE = 110;
    private static final int FX_REQUEST_PERMISSION_CODE = 100;
    @BindView(R.id.tv_select_refund_reason)
    TextView tvSelectRefundReason;

    @BindView(R.id.et_refund_money)
    EditText etRefundMoney;
    @BindView(R.id.et_refund_describe)
    EditText etRefundDescribe;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_refund_type)
    TextView tvRefundType;
    @BindView(R.id.tv_select_pictures)
    TextView tvSelectPictures;
    @BindView(R.id.activity_apply_refund)
    LinearLayout activityApplyRefund;
    @BindView(R.id.iv_refund_1)
    ImageView ivRefund1;
    @BindView(R.id.iv_refund_2)
    ImageView ivRefund2;
    @BindView(R.id.iv_refund_3)
    ImageView ivRefund3;
    private View headerview;
    private PopupWindow pw;
    private int refundType = 1, orderId, skuId;
    private double money;
    private FXMallControl control = new FXMallControl();
    private List<File> files = new ArrayList<>();
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_refund);
        ButterKnife.bind(this);
        initHeaderView();

        Intent intent = getIntent();
        orderId = intent.getIntExtra("orderId", -1);
        skuId = intent.getIntExtra("skuId", -1);
        money = intent.getDoubleExtra("money", -1);
        etRefundMoney.setText("¥"+money);
    }

    @Override
    public View getContentView() {
        return activityApplyRefund;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "申请退款");
    }

    @OnClick(R.id.tv_select_refund_reason)
    public void selectReason(){
            showCancleDialog("申请退款");
    }


    @OnClick(R.id.tv_refund_type)
    public void refundType() {
        View contentview = getLayoutInflater().inflate(R.layout.layout_refundtype_select_window, null);
        pw = new PopupWindow(contentview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_search_goods);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvRefundType.setText("退货退款");
                refundType = 1;
                tvSelectPictures.setText("上传三张凭证");
                ivRefund2.setVisibility(View.VISIBLE);
                ivRefund3.setVisibility(View.VISIBLE);
                pw.dismiss();
            }
        });
        TextView tvBoy = (TextView) contentview.findViewById(R.id.tv_search_store);
        tvBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvRefundType.setText("仅退款");
                tvSelectPictures.setText("上传物流信息截图");
                ivRefund2.setVisibility(View.INVISIBLE);
                ivRefund3.setVisibility(View.INVISIBLE);
                refundType = 2;
                pw.dismiss();
            }
        });

        //设置触摸对话框以外区域，对话框消失
        pw.setFocusable(true);
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#00000000"));
        pw.setBackgroundDrawable(cd);
        pw.showAsDropDown(tvRefundType);
    }

    @OnClick(R.id.tv_select_pictures)
    public void selectPictures(){
        if(refundType == 1){
            startActivityForResult(new Intent(this,PictrueChooserActivity.class),175);
        }else {
            selectPhoto();
        }
    }

    @OnClick(R.id.btn_submit)
    public void submit() {
        if(TextUtils.isEmpty(cancleReason)){
            toast("你还未选择退款原因");
            return;
        }

        Map<String, String> paramas = new HashMap<>();
        paramas.put("orderCommodity.orders.user.token", sp.get("token"));
        paramas.put("type", refundType + "");
        paramas.put("money", money + "");
        paramas.put("reason", cancleReason);
        paramas.put("orderCommodity.orders.id", orderId + "");
        paramas.put("orderCommodity.sku.id", skuId + "");

        String describe = etRefundDescribe.getText().toString();
        if(!TextUtils.isEmpty(describe)){
            paramas.put("explain",describe);
        }


        OkhttpUploadUtils.getInstance(this).sendMultipart(FXConst.USER_APPLY_REFUND_URL, paramas, "file", files, null, null,null,null);
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }





    /**
     * 弹出退款或取消原因选择框
     */
    private TextView tvReason1,tvReason2,tvReason3,tvReason4;
    private ImageView iv1,iv2,iv3,iv4;
    private String cancleReason;
    private android.app.AlertDialog pw1;
    private void showCancleDialog(final String type){
        View contentview = getLayoutInflater().inflate(R.layout.layout_reason_of_cancel_order, null);
        pw1 = new android.app.AlertDialog.Builder(this).create();
        pw1.setView(contentview);
        TextView tvType = (TextView) contentview.findViewById(R.id.tv_cancle_type);
        tvType.setText("请选择"+type+"的原因");
        tvReason1 = (TextView) contentview.findViewById(R.id.tv_reason1);
        tvReason4 = (TextView) contentview.findViewById(R.id.tv_reason4);
        tvReason3 = (TextView) contentview.findViewById(R.id.tv_reason3);
        tvReason2 = (TextView) contentview.findViewById(R.id.tv_reason2);
        iv1 = (ImageView) contentview.findViewById(R.id.iv_confirm1);
        iv2 = (ImageView) contentview.findViewById(R.id.iv_confirm2);
        iv3 = (ImageView) contentview.findViewById(R.id.iv_confirm3);
        iv4 = (ImageView) contentview.findViewById(R.id.iv_confirm4);

        tvReason1.setOnClickListener(this);
        tvReason2.setOnClickListener(this);
        tvReason3.setOnClickListener(this);
        tvReason4.setOnClickListener(this);


        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_confirm);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              tvSelectRefundReason.setText(cancleReason);

                pw1.dismiss();
            }
        });
        TextView tvBoy = (TextView) contentview.findViewById(R.id.tv_cancle);
        tvBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw1.dismiss();
            }
        });


        //设置触摸对话框以外区域，对话框消失
        pw1.setCanceledOnTouchOutside(true);
        pw1.show();
    }





    /**
     * 上传凭证
     */
    private void selectPhoto() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final View iconSelect = getLayoutInflater().inflate(R.layout.layout_iconselect_dialog, null);
        TextView tvPicture = (TextView) iconSelect.findViewById(R.id.tv_icon_fromPictures);
        TextView tvCamera = (TextView) iconSelect.findViewById(R.id.tv_icon_fromCarema);
        TextView tvCancle = (TextView) iconSelect.findViewById(R.id.tv_icon_cancle);

        alertDialog.setView(iconSelect);

        tvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//从相册选择
                if (PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(ApplyRefundActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(ApplyRefundActivity.this, "请打开允许访问SD权限", Toast.LENGTH_SHORT).show();
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    PermissionUtil.requestPermissions(ApplyRefundActivity.this, TM_REQUEST_PERMISSION_CODE, permissions);
                    return;
                }
                selectPicture();
                alertDialog.dismiss();
            }
        });
        tvCamera.setOnClickListener(new View.OnClickListener() {//从拍照获取
            @Override
            public void onClick(View view) {
                if (PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(ApplyRefundActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(ApplyRefundActivity.this, "请打开允许访问SD权限", Toast.LENGTH_SHORT).show();
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    PermissionUtil.requestPermissions(ApplyRefundActivity.this, TM_REQUEST_PERMISSION_CODE, permissions);
                    return;
                }

                if (PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(ApplyRefundActivity.this, Manifest.permission.CAMERA)) {
                    Toast.makeText(ApplyRefundActivity.this, "请打开允许访问相机权限", Toast.LENGTH_SHORT).show();
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.CAMERA);
                    PermissionUtil.requestPermissions(ApplyRefundActivity.this, TM_REQUEST_PERMISSION_CODE, permissions);
                    return;
                }

                takePicture();
                alertDialog.dismiss();
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);//设置对话框显示在屏幕下方
        alertDialog.show();
    }


    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //将拍照的图片保存到本地
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
        Uri iconUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            iconUri = FileProvider.getUriForFile(this, "com.dgkj.fxmall.fileprovider", file);
        } else {
            iconUri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iconUri);
        startActivityForResult(intent, 102);
    }

    public void selectPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 101);
    }

    /**
     * 7.0 获取图片文件uri
     *
     * @param context
     * @param file
     * @return
     */
    private Uri getImageContentUri(Context context, File file) {
        String filePath = file.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=?", new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (file.exists()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            }
        }
        return null;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FX_REQUEST_PERMISSION_CODE) {
            permissionsResult(permissions, grantResults);

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /**
     * 响应权限请求结果
     *
     * @param permissions  申请的权限集合
     * @param grantResults 权限授权结果集合
     */
    private void permissionsResult(String[] permissions, int[] grantResults) {
        List<String> needPermission = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    needPermission.add(permissions[i]);
                }

                if (permissions[i].equals(Manifest.permission.CAMERA) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                }
                if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (permissions[i].equals(Manifest.permission.CAMERA) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        takePicture();
                    }
                }
            }
        }

        if (needPermission.size() > 0) {
            StringBuffer permissionMsg = new StringBuffer();
            for (int i = 0; i < needPermission.size(); i++) {
               if (needPermission.get(i).equals(Permission.PERMISSIONS_READ_STORAGE)) {
                    permissionMsg.append("," + "请求访问SD卡");
                } else if (needPermission.get(i).equals(Permission.PERMISSIONS_CAMERA)) {
                    permissionMsg.append("," + "请求打开相机");
                }
            }
            String strMessage = "请允许使用\"" + permissionMsg.substring(1).toString() + "\"权限, 以正常使用APP的定位功能.";

            Toast.makeText(ApplyRefundActivity.this, strMessage, Toast.LENGTH_SHORT).show();
        } else {
           takePicture();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==175 && resultCode==121){
            ArrayList<String> images = data.getStringArrayListExtra("images");
            Bitmap[] bitmaps = new Bitmap[3];
            for (int i = 0; i < 3; i++) {
                File file = new File(images.get(i));
                files.add(file);
                bitmaps[i] = BitmapFactory.decodeFile(images.get(i));
            }
            ivRefund1.setImageBitmap(bitmaps[0]);
            ivRefund2.setImageBitmap(bitmaps[1]);
            ivRefund3.setImageBitmap(bitmaps[2]);
        }

        if (resultCode == RESULT_OK && requestCode == 101) {//从相册获取
            Uri uri = data.getData();

            //根据uri查找图片地址
            ContentResolver resolver = getContentResolver();
            String[] pojo = {MediaStore.Images.Media.DATA};
            Cursor cursor = resolver.query(uri, pojo, null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                cursor.moveToFirst();
                String path = cursor.getString(columnIndex);
                file = new File(path);
                cursor.close();
                files.add(file);
            }
            Glide.with(this).load(uri).into(ivRefund1);

        } else if (resultCode == RESULT_OK && requestCode == 102) {//拍照
            Uri uri1 = null;
            if (Build.VERSION.SDK_INT >= 24) {
                uri1 = getImageContentUri(this, file);
                LogUtil.i("TAG", "7.0uri1111111111====" + uri1);
            } else {
                uri1 = Uri.fromFile(file);
            }
            Glide.with(this).load(uri1).into(ivRefund1);
            files.add(file);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //弹窗点击处理
            case R.id.tv_reason1:
                cancleReason = tvReason1.getText().toString();
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.GONE);
                iv3.setVisibility(View.GONE);
                iv4.setVisibility(View.GONE);

                break;
            case R.id.tv_reason2:
                cancleReason = tvReason2.getText().toString();
                iv1.setVisibility(View.GONE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.GONE);
                iv4.setVisibility(View.GONE);
                break;
            case R.id.tv_reason3:
                cancleReason = tvReason3.getText().toString();
                iv1.setVisibility(View.GONE);
                iv2.setVisibility(View.GONE);
                iv3.setVisibility(View.VISIBLE);
                iv4.setVisibility(View.GONE);
                break;
            case R.id.tv_reason4:
                cancleReason = tvReason4.getText().toString();
                iv1.setVisibility(View.GONE);
                iv2.setVisibility(View.GONE);
                iv3.setVisibility(View.GONE);
                iv4.setVisibility(View.VISIBLE);
                break;
        }
    }
}
