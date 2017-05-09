package com.dgkj.fxmall.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuideActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<ImageView> lists;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!sp.get("first").equals("false")){
            setContentView(R.layout.activity_guide);
            ButterKnife.bind(this);
            inits();
            initevent();
        }else {
            jumpTo(LoadingActivity.class,true);
        }

    }


    private void inits(){

        lists = new ArrayList<>();
        //将引导图片导入数据
        addImageview(R.drawable.spgm);
        addImageview(R.drawable.dpsq);
        addImageview(R.drawable.ywy);
        addImageview(R.drawable.fbxq);
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                if (lists != null) {
                    return lists.size();
                }
                return 0;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //super.destroyItem(container, position, object);
                container.removeView(lists.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                //return super.instantiateItem(container, position);
                container.addView(lists.get(position));
                return lists.get(position);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });



    }


    protected void addImageview(int iResId) {
        ImageView imageView = new ImageView(this);
        //把图片不按比例扩大/缩小到View的大小显示
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(iResId);
        lists.add(imageView);

    }


    private void initevent(){
        setStatuBarColor("#1194fe");
        //对滑动页面进行监听
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当滑动到最后一页把Button按钮设置到图片下面
                switch (position){
                    case 0:
                        setStatuBarColor("#1194fe");
                        break;
                    case 1:
                        setStatuBarColor("#fd455d");
                        break;
                    case 2:
                        setStatuBarColor("#6d62fc");
                        break;
                    case 3:
                        setStatuBarColor("#6d44be");
                        sp.put("first","false");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                jumpTo(HomePageActivity.class,true);
                            }
                        },1000);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public View getContentView() {
        return null;
    }

    public void setStatuBarColor(String color){
        if(Build.VERSION.SDK_INT >= 21 ){
            getWindow().setStatusBarColor(Color.parseColor(color));
        }
    }
}
