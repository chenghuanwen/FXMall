package com.dgkj.fxmall.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.PermissionUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StoreInfoEditActivity extends BaseActivity {
    @BindView(R.id.tv_select_store_iocn)
    TextView tvSelectStoreIocn;
    @BindView(R.id.et_store_introduce)
    EditText etStoreIntroduce;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.civ_usemsg_icon)
    CircleImageView civUsemsgIcon;
    @BindView(R.id.rl_icon)
    RelativeLayout rlIcon;
    @BindView(R.id.activity_store_information)
    LinearLayout activityStoreInformation;
    @BindView(R.id.tv_product_banner)
    TextView tvProductBanner;
    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    private View headerview;
    private static final int TM_REQUEST_PERMISSION_CODE = 110;
    private File file;
    private ByteArrayOutputStream baos;
    private byte[] imageData;
    private File iconFile,bannerFile;
    private OkHttpClient client = new OkHttpClient.Builder().build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_information);
        ButterKnife.bind(this);
        initHeaderView();
        setData();
    }

    private void setData() {
        Intent intent = getIntent();
        String logo = intent.getStringExtra("logo");
        String banner = intent.getStringExtra("banner");
        String introduce = intent.getStringExtra("introduce");
        Glide.with(this).load(logo).error(R.mipmap.android_quanzi).into(civUsemsgIcon);
        etStoreIntroduce.setText(introduce);
        if(!TextUtils.isEmpty(banner)){
            ivBanner.setVisibility(View.VISIBLE);
            Glide.with(this).load(banner).into(ivBanner);
        }
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "店铺资料");
    }

    @OnClick({R.id.tv_select_store_iocn, R.id.rl_icon})
    public void selectIcon() {
        selectPhoto();
    }

    @OnClick(R.id.tv_product_banner)
    public void addBanner(){
        if (PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(StoreInfoEditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(StoreInfoEditActivity.this, "请打开允许访问SD权限", Toast.LENGTH_SHORT).show();
            List<String> permissions = new ArrayList<>();
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            PermissionUtil.requestPermissions(StoreInfoEditActivity.this, TM_REQUEST_PERMISSION_CODE, permissions);
            return;
        }
        selectPicture(104);
    }


    @OnClick(R.id.btn_save)
    public void save() {
        String introduce = etStoreIntroduce.getText().toString();
        if (TextUtils.isEmpty(introduce)) {
            toast("请先填写完整信息");
            return;
        }

        FormBody body = new FormBody.Builder()
                .add("user.token", sp.get("token"))
                .add("intro", introduce)
                .build();
        Request request = new Request.Builder()
                .url(FXConst.UPLOAD_STORE_INTRODUCE)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(StoreInfoEditActivity.this, "网络异常！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body().string().contains("1000")) {
                    toastInUI(StoreInfoEditActivity.this, "店铺信息更新成功！");
                }
            }
        });
    }

    /**
     * 上传头像
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
                if (PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(StoreInfoEditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(StoreInfoEditActivity.this, "请打开允许访问SD权限", Toast.LENGTH_SHORT).show();
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    PermissionUtil.requestPermissions(StoreInfoEditActivity.this, TM_REQUEST_PERMISSION_CODE, permissions);
                    return;
                }
                selectPicture(101);
                alertDialog.dismiss();
            }
        });
        tvCamera.setOnClickListener(new View.OnClickListener() {//从拍照获取
            @Override
            public void onClick(View view) {
                if (PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(StoreInfoEditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(StoreInfoEditActivity.this, "请打开允许访问SD权限", Toast.LENGTH_SHORT).show();
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    PermissionUtil.requestPermissions(StoreInfoEditActivity.this, TM_REQUEST_PERMISSION_CODE, permissions);
                    return;
                }

                if (PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(StoreInfoEditActivity.this, Manifest.permission.CAMERA)) {
                    Toast.makeText(StoreInfoEditActivity.this, "请打开允许访问相机权限", Toast.LENGTH_SHORT).show();
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.CAMERA);
                    PermissionUtil.requestPermissions(StoreInfoEditActivity.this, TM_REQUEST_PERMISSION_CODE, permissions);
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
        LogUtil.i("TAG", "准备拍照==========");
        Uri iconUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            iconUri = FileProvider.getUriForFile(this, "com.dgkj.fxmall.fileprovider", file);
            LogUtil.i("TAG", "7.0uri====" + iconUri);
        } else {
            iconUri = Uri.fromFile(file);
        }
        LogUtil.i("TAG", "准备拍照URI==========");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iconUri);
        startActivityForResult(intent, 102);
    }


    public void selectPicture(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, requestCode);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 101) {//从相册获取头像

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
            }
            crop(uri);//裁剪头像
        }else if (resultCode == RESULT_OK && requestCode == 104) {//从相册获取banner
            Uri uri = data.getData();

            //根据uri查找图片地址
            ContentResolver resolver = getContentResolver();
            String[] pojo = {MediaStore.Images.Media.DATA};
            Cursor cursor = resolver.query(uri, pojo, null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                cursor.moveToFirst();
                String path = cursor.getString(columnIndex);
                bannerFile = new File(path);
                cursor.close();
            }
            uploadIcon(bannerFile,"bFile",FXConst.UPLOAD_STORE_BANANER_URL);
            ivBanner.setVisibility(View.VISIBLE);
            Glide.with(this).load(uri).into(ivBanner);
        } else if (resultCode == RESULT_OK && requestCode == 102) {//拍照
            Uri uri1 = null;
            if (Build.VERSION.SDK_INT >= 24) {
                uri1 = getImageContentUri(this, file);
                LogUtil.i("TAG", "7.0uri1111111111====" + uri1);
            } else {
                uri1 = Uri.fromFile(file);
            }
            crop(uri1);//裁剪头像
        }
        if (resultCode == RESULT_OK && requestCode == 103) {//收到截取后的图像
            Bundle bundle = data.getExtras();
            Bitmap bitmap = bundle.getParcelable("data");
            if (bitmap != null) {
                civUsemsgIcon.setImageBitmap(bitmap);//设置头像
                //将头像转为二进制数组写入临时文件传给服务器
                baos = new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight() * 4);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imageData = baos.toByteArray();
                try {
                    iconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), System.currentTimeMillis() + "icon.png");
                    if (!iconFile.exists()) {
                        iconFile.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(iconFile);
                    out.write(imageData);
                    out.flush();
                    out.close();
                    uploadIcon(iconFile,"file",FXConst.UPLOAD_STORE_LOGO);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    /***
     * 调用android 自带的裁剪功能，对图片进行裁剪
     *
     * @param uri 被裁剪图片的路径
     */
    private void crop(Uri uri) {
        //隐式意图调用android自身的图片截取界面
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 103);//强截取的头像返回（bundle）
    }


    /**
     * 上传头像或banner
     * @param iconFile
     */
    private void uploadIcon(final File iconFile,String key,String url) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("user.token", sp.get("token"))
                .addPart(Headers.of("Content-Disposition", "form-data; name="+key+";filename=" + iconFile.getName()), RequestBody.create(MediaType.parse("image/png"), iconFile));
        MultipartBody body = builder.build();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(StoreInfoEditActivity.this, "网络繁忙！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    toastInUI(StoreInfoEditActivity.this, "上传成功！");
                    if (iconFile != null) {
                        iconFile.delete();
                    }

                } else {
                    toastInUI(StoreInfoEditActivity.this, "上传失败！");
                }
            }
        });
    }



    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
