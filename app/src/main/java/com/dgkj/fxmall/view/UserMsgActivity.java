package com.dgkj.fxmall.view;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.PermissionUtil;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.myView.TransparentDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

public class UserMsgActivity extends BaseActivity {

    @BindView(R.id.civ_usemsg_icon)
    CircleImageView civUsemsgIcon;
    @BindView(R.id.rl_icon)
    RelativeLayout rlIcon;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.rl_nickname)
    RelativeLayout rlNickname;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.rl_gender)
    RelativeLayout rlGender;
    @BindView(R.id.tv_raalname)
    TextView tvRaalname;
    @BindView(R.id.rl_realname)
    RelativeLayout rlRealname;
    @BindView(R.id.tv_phonenumber)
    TextView tvPhonenumber;
    @BindView(R.id.rl_phonenumber)
    RelativeLayout rlPhonenumber;
    @BindView(R.id.rl_current_address)
    RelativeLayout rlCurrentAddress;
    @BindView(R.id.rl_takegoods_address)
    RelativeLayout rlTakegoodsAddress;
    @BindView(R.id.ib_back)
    ImageButton ibBack;

    private static final int TM_REQUEST_PERMISSION_CODE = 110;
    private File file;
    private ByteArrayOutputStream baos;
    private byte[] imageData;
    private File iconFile;
    private View headerview;
    private String gender = "", phone = "", nick = "", name = "", icon = "", address = "";
    private String from = "";
    private OkHttpClient client;
    private SharedPreferencesUnit sp;
    private int genderCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_msg);
        ButterKnife.bind(this);
        initHeaderview();

        client = new OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(this);
        Intent intent = getIntent();
        gender = intent.getStringExtra("gender");
        name = intent.getStringExtra("realname");
        nick = intent.getStringExtra("nick");
        //  name = URLDecoder.decode(name,"utf-8");
        phone = intent.getStringExtra("phone");
        address = intent.getStringExtra("address");
        icon = intent.getStringExtra("icon");
        //  nick = URLDecoder.decode(nick,"utf-8");

        setData();
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void setData() {
        //TODO 获取个人信息填充
        Glide.with(this).load(icon).error(R.mipmap.sz_tx).into(civUsemsgIcon);
        tvNickname.setText(nick);
        if ("1".equals(gender)) {
            tvGender.setText("男");
        } else {
            tvGender.setText("女");
        }
        tvRaalname.setText(name);
        tvPhonenumber.setText(phone);
    }

    @OnClick(R.id.rl_icon)
    public void setIcon() {
        selectIcon();
    }

    @OnClick(R.id.rl_nickname)
    public void setNickname() {
        Intent intent = new Intent(this, SetNicknameActivity.class);
        intent.putExtra("from", "nick");
        intent.putExtra("nick", nick);
        startActivityForResult(intent, 111);
    }

    @OnClick(R.id.rl_realname)
    public void setRealName() {
        Intent intent = new Intent(this, SetNicknameActivity.class);
        intent.putExtra("from", "name");
        intent.putExtra("name", name);
        startActivityForResult(intent, 113);
    }

    @OnClick(R.id.rl_phonenumber)
    public void changePhone() {
        Intent intent = new Intent(this, ChangePhoneActivity.class);
        intent.putExtra("phone", phone);
        jumpTo(intent, false);
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.rl_current_address)
    public void address() {
        Intent intent = new Intent(this, LocationCurrenterCityActivity.class);
        //TODO 将个人信息中的所在地传递下去
        intent.putExtra("location", address);
        jumpTo(intent, false);
    }

    @OnClick(R.id.rl_takegoods_address)
    public void takegoods() {
        jumpTo(TakeGoodsAdressActivity.class, false);
    }


    private void initHeaderview() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "个人资料");

        from = getIntent().getStringExtra("from");
        if ("newphone".equals(from)) {
            tvPhonenumber.setText(getIntent().getStringExtra("phone"));
        }
    }

    @OnClick(R.id.rl_gender)
    public void geneder() {
        selectGender();
    }

    /**
     * 头像选择
     */
    private void selectIcon() {
        // final TransparentDialog alertDialog = new TransparentDialog.Builder(this).create();
        final Dialog alertDialog = new Dialog(this, R.style.transparentDialog);
        final View iconSelect = getLayoutInflater().inflate(R.layout.layout_iconselect_dialog, null);
        TextView tvPicture = (TextView) iconSelect.findViewById(R.id.tv_icon_fromPictures);
        TextView tvCamera = (TextView) iconSelect.findViewById(R.id.tv_icon_fromCarema);
        TextView tvCancle = (TextView) iconSelect.findViewById(R.id.tv_icon_cancle);

        alertDialog.setContentView(iconSelect);

        tvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//从相册选择
                if (PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(UserMsgActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(UserMsgActivity.this, "请打开允许访问SD权限", Toast.LENGTH_SHORT).show();
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    PermissionUtil.requestPermissions(UserMsgActivity.this, TM_REQUEST_PERMISSION_CODE, permissions);
                    return;
                }
                selectPicture();
                alertDialog.dismiss();
            }
        });
        tvCamera.setOnClickListener(new View.OnClickListener() {//从拍照获取
            @Override
            public void onClick(View view) {
                if (PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(UserMsgActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(UserMsgActivity.this, "请打开允许访问SD权限", Toast.LENGTH_SHORT).show();
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    PermissionUtil.requestPermissions(UserMsgActivity.this, TM_REQUEST_PERMISSION_CODE, permissions);
                    return;
                }

                if (PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(UserMsgActivity.this, Manifest.permission.CAMERA)) {
                    Toast.makeText(UserMsgActivity.this, "请打开允许访问相机权限", Toast.LENGTH_SHORT).show();
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.CAMERA);
                    PermissionUtil.requestPermissions(UserMsgActivity.this, TM_REQUEST_PERMISSION_CODE, permissions);
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

    //TODO 弹出对话框进行性别选择
    private void selectGender() {
        final AlertDialog alertDialog = new AlertDialog.Builder(UserMsgActivity.this).create();
        alertDialog.setCanceledOnTouchOutside(true);
        View dialog = getLayoutInflater().inflate(R.layout.layout_gender_selector_dialog, null);
        TextView tvBoy = (TextView) dialog.findViewById(R.id.tv_boy);
        TextView tvGirl = (TextView) dialog.findViewById(R.id.tv_girl);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_cancle);
        TextView tvSave = (TextView) dialog.findViewById(R.id.tv_save);
        final ImageView select1 = (ImageView) dialog.findViewById(R.id.iv_selected2);
        final ImageView select2 = (ImageView) dialog.findViewById(R.id.iv_selected1);
        alertDialog.setView(dialog);
        tvBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "男";
                genderCode = 1;
                select1.setVisibility(View.VISIBLE);
                select2.setVisibility(View.INVISIBLE);
            }
        });
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "女";
                genderCode = 2;
                select1.setVisibility(View.INVISIBLE);
                select2.setVisibility(View.VISIBLE);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "";
                alertDialog.dismiss();
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 将性别上传到服务器
                FormBody body = new FormBody.Builder()
                        .add("token", sp.get("token"))
                        .add("sex", genderCode + "")
                        .build();
                Request request = new Request.Builder()
                        .post(body)
                        .url(FXConst.CHANGE_USER_GENDER)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        toastInUI(UserMsgActivity.this, "网络异常！");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        if (result.contains("1000")) {
                            toastInUI(UserMsgActivity.this, "性别修改成功！");
                        } else {
                            toastInUI(UserMsgActivity.this, "性别修改失败！");
                        }
                    }
                });
                tvGender.setText(gender);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            }
            crop(uri);//裁剪头像
        } else if (resultCode == RESULT_OK && requestCode == 102) {//拍照
            Uri uri1 = null;
            if (Build.VERSION.SDK_INT >= 24) {
                uri1 = getImageContentUri(this, file);
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
                //baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imageData = baos.toByteArray();
                try {
                    //iconFile = iconFile.createTempFile("icon", ".jpg", null);
                    iconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), System.currentTimeMillis() + "icon.png");
                    if (!iconFile.exists()) {
                        iconFile.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(iconFile);
                    out.write(imageData);
                    out.flush();
                    out.close();
                    uploadIcon(iconFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        if (requestCode == 111 && resultCode == 112) {
            tvNickname.setText(data.getStringExtra("nick"));
            LogUtil.i("TAG", "设置昵称返回===" + data.getStringExtra("nick"));
        }
        if (requestCode == 113 && resultCode == 114) {
            tvRaalname.setText(data.getStringExtra("name"));
            LogUtil.i("TAG", "设置真名返回===" + data.getStringExtra("name"));
        }


        super.onActivityResult(requestCode, resultCode, data);
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

    private void uploadIcon(final File iconFile) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", sp.get("token"))
                .addPart(Headers.of("Content-Disposition", "form-data; name=file;filename=" + iconFile.getName()), RequestBody.create(MediaType.parse("image/png"), iconFile));
        MultipartBody body = builder.build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.CHANGE_USER_ICON)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(UserMsgActivity.this, "网络繁忙！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    toastInUI(UserMsgActivity.this, "头像上传成功！");
                    try {
                        JSONObject object = new JSONObject(result);
                        String iconUrl = object.getString("dataset");
                        if (iconFile != null) {
                            iconFile.delete();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    toastInUI(UserMsgActivity.this, "头像上传失败！");
                }
            }
        });
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == TM_REQUEST_PERMISSION_CODE) {
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
                if (needPermission.get(i).equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    permissionMsg.append("," + getResources().getString(R.string.string004));
                } else if (needPermission.get(i).equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    permissionMsg.append("," + getResources().getString(R.string.string005));
                } else if (needPermission.get(i).equals(Manifest.permission.CAMERA)) {
                    permissionMsg.append("," + getResources().getString(R.string.string006));
                }
            }
            String strMessage = getResources().getString(R.string.string007) + permissionMsg.substring(1).toString() + getResources().getString(R.string.string008) + getResources().getString(R.string.string009);

            Toast.makeText(this, strMessage, Toast.LENGTH_SHORT).show();
        } else {
            return;
        }
    }


}
