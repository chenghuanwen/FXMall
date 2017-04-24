package com.dgkj.fxmall.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.dgkj.fxmall.R;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** 自定义图库展示adapter
 * Created by 成焕文 on 2017/3/9.
 */

public class PictureChooserAdapter extends CommonAdapter<String> {

    /**
     * 用户选择的图片，储存为图片完整路径
     */
    private List<String> mSelectImgs = new LinkedList<>();
    /**
     * 文件夹路径
     */
    private String mDirPath;

    public PictureChooserAdapter(Context context, int layoutId, List<String> datas,String dirPath) {
        super(context, layoutId, datas);
        this.mDirPath = dirPath;
    }

    @Override
    protected void convert(ViewHolder viewHolder, String item, int position) {
       final String imgPath = mDirPath + "/" + item;//单张图片完整路径
        viewHolder.setImageResource(R.id.iv_chooser,R.mipmap.android_quanzi);//设置默认图片
        viewHolder.setImageByUrl(R.id.iv_chooser, imgPath);//加载图片
       final ImageView imageView = viewHolder.getView(R.id.iv_chooser);
       final CheckBox mSelect = viewHolder.getView(R.id.cb_chooser);
        imageView.setColorFilter(null);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectImgs.contains(imgPath)){//已选中，再次点击取消
                    mSelectImgs.remove(imgPath);
                    mSelect.setChecked(false);
                    imageView.setColorFilter(null);
                }else {//选中，为图片设置阴影
                    mSelectImgs.add(imgPath);
                    mSelect.setChecked(true);
                    imageView.setColorFilter(Color.parseColor("#77000000"));
                }
            }
        });

        //已经选择的图片设置选中效果
        if(mSelectImgs.contains(imgPath)){
            mSelect.setChecked(true);
            imageView.setColorFilter(Color.parseColor("#77000000"));
        }

    }

    public ArrayList<String> getSelectImgs(){
        ArrayList list = new ArrayList();
        list.clear();
        list.addAll(mSelectImgs);
        return list;
    }
}
