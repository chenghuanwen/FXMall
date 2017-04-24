package com.dgkj.fxmall.view.myView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.ImageFolder;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/** 自定义文件夹列表弹窗
 * Created by Android004 on 2017/3/9.
 */

public class ListDirPopWindown extends BasePopupWindowForListView<ImageFolder> {

    private ListView mDirList;

    public ListDirPopWindown(int width, int height, List<ImageFolder> datas,View contentview) {
        super(contentview, width, height,true,datas);
    }





    @Override
    protected void beforeInitWeNeedSomeParams(Object... params) {

    }

    @Override
    public void initViews() {
        mDirList = (ListView) findViewByID(R.id.lv_folder);
        mDirList.setAdapter(new CommonAdapter<ImageFolder>(context,R.layout.item_folder_popuwindow,mDatas) {
            @Override
            protected void convert(ViewHolder viewHolder, ImageFolder item, int position) {
                viewHolder.setText(R.id.tv_folder_name,item.getName());
                viewHolder.setImageByUrl(R.id.iv_first_img,item.getFirstPicturePath());
                viewHolder.setText(R.id.tv_img_count,item.getCount()+"张");

            }
        });

    }


    public interface OnFolderSelectListener{
        void onFolderSelected(ImageFolder folder);
    }

    private OnFolderSelectListener listener;

    public void setOnFolderSelectListener(OnFolderSelectListener listener){
        if(listener != null){
            this.listener = listener;
        }
    }

    @Override
    public void initEvents() {

        mDirList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listener != null){
                    listener.onFolderSelected(mDatas.get(position));
                }
            }
        });

    }

    @Override
    public void init() {

    }
}
