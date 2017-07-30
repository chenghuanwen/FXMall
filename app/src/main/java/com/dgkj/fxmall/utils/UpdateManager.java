package com.dgkj.fxmall.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.constans.FXConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateManager {

	private Context mContext;
	
	//提示
	private String updateMsg = "发现新版本，请及时更新~";

	//返回的安装包url
	private String versionUrl;//更新地址
	private double v;//当前版本号
	private OkHttpClient client = new OkHttpClient.Builder().build();
	
	private Dialog noticeDialog;
	
	private Dialog downloadDialog;
	 //* 下载包安装路径 */
    private static final String savePath = MyApplication.root;

    private static final String saveFileName = savePath + "UpdateAiGouRelease.apk";

	/* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    
    private static final int DOWN_UPDATE = 1;
    
    private static final int DOWN_OVER = 2;
    
    private int progress;
    
    private Thread downLoadThread;
    
    private boolean interceptFlag = false;
	private Handler uiHandler = new Handler(Looper.getMainLooper());
    
    private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				installApk();
				break;
			default:
				break;
			}
    	};
    };


	public UpdateManager(Context context) {
		this.mContext = context;
	}

	public void checkUpdateInfo(){

		getCurrentVersion(new OnGetVersionFinishListener() {
			@Override
			public void onGetVersionFinish(String version) {
				Log.i("TAG","++++++++++++onGetVersionFinish++++当前版本号+++++++++++"+version);

               if(!version.equals(getVersionName(mContext))){
				   uiHandler.post(new Runnable() {
					   @Override
					   public void run() {
						   showNoticeDialog();
					   }
				   });

				}else {
				   Toast toast = Toast.makeText(mContext, "已是最新版本！", Toast.LENGTH_SHORT);
				   toast.setGravity(Gravity.CENTER,0,0);
				   toast.show();

			   }
			}
		});

	}
	
	
	private void showNoticeDialog(){
		Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		builder.setMessage(updateMsg);
		builder.setPositiveButton("下载", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();			
			}
		});
		builder.setNegativeButton("以后再说˵", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();				
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}
	
	private void showDownloadDialog(){
		Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.progress);
		
		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();
		
		downloadApk();
	}
	
	private Runnable mdownApkRunnable = new Runnable() {	
		@Override
		public void run() {
			try {
				URL url = new URL(versionUrl);
			
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
			//	Log.i("TAG","下载文件大小==="+length);
				File file = new File(savePath);
				if(!file.exists()){
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				
				int count = 0;
				byte buf[] = new byte[1024];
				
				do{   		   		
		    		int numread = is.read(buf);
		    		count += numread;
					//Log.i("TAG","正在下载....");
		    	    progress =(int)(((float)count / length) * 100);
					//更新进度
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){
						//下载完成通知安装


		    			mHandler.sendEmptyMessage(DOWN_OVER);
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}while(!interceptFlag);////点击取消就停止下载.
				
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
			
		}
	};
	
	 /**
     * 下载apk
     */
	
	private void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	 /**安装apk
     */
	private void installApk(){
		File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }    
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        mContext.startActivity(i);
	
	}

	//版本名
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}
	//版本号
	public static int getVersionCode(Context context) {
		return getPackageInfo(context).versionCode;
	}
	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;
		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pi;
	}


	//String url = "http://123.206.58.158/doupaiapp/versionAction_getVersions?version.system=2";
	public void getCurrentVersion(final OnGetVersionFinishListener listener){


		FormBody body = new FormBody.Builder()
				.add("type","1")
				.build();
		Request request = new Request.Builder()
				.post(body)
				.url(FXConst.VERSION_UPDATE_URL)
				.build();
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String string = response.body().string();
				LogUtil.i("TAG","版本信息========"+string);
				if(string.contains("1000")){
					try {
						JSONObject object = new JSONObject(string);
						JSONArray data = object.getJSONArray("data");
						JSONObject jsonObject = data.getJSONObject(0);
						versionUrl = jsonObject.getString("url");
						String version = jsonObject.getString("version");


						listener.onGetVersionFinish(version);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});


	}

	public interface OnGetVersionFinishListener{
		void onGetVersionFinish(String version);
	}
}
