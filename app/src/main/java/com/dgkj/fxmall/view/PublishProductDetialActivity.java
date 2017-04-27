package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.PublishProductDetialAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.Position;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PublishProductDetialActivity extends BaseActivity {
    @BindView(R.id.tv_add_picture)
    TextView tvAddPicture;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.rv_add_pitures)
    RecyclerView rvAddPitures;
    @BindView(R.id.tv_add_continue)
    TextView tvAddContinue;
    @BindView(R.id.activity_publish_product_detial)
    LinearLayout activityPublishProductDetial;
    private View headerview;
    private PublishProductDetialAdapter adapter;
    private List<String> images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_product_detial);
        ButterKnife.bind(this);
        initHeaderView();
        initData();

        if (MyApplication.selectedPictures != null) {
            tvAddPicture.setVisibility(View.GONE);
            tvAddContinue.setVisibility(View.VISIBLE);
            rvAddPitures.setVisibility(View.VISIBLE);
            adapter.addAll(MyApplication.selectedPictures, true);
        }
    }

    @Override
    public View getContentView() {

        return activityPublishProductDetial;
    }

    private void initData() {
        images = new ArrayList<>();
        adapter = new PublishProductDetialAdapter(this, R.layout.item_add_product_pictures, images);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvAddPitures.setLayoutManager(layoutManager);
        rvAddPitures.setAdapter(adapter);
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "商品详情");
        setHeaderImage(headerview, -1, "完成", Position.RIGHT, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                List<String> sortData = adapter.getSortData();
                intent.putStringArrayListExtra("sort", (ArrayList<String>) sortData);
                setResult(129, intent);
                finish();
            }
        });
    }

    @OnClick(R.id.tv_add_picture)
    public void selectPictures() {
        Intent intent = new Intent(this, PictrueChooserActivity.class);
        startActivityForResult(intent, 126);
    }

    @OnClick(R.id.tv_add_continue)
    public void addContinue() {
        startActivityForResult(new Intent(this, PictrueChooserActivity.class), 128);
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 126 && resultCode == 121) {
            tvAddPicture.setVisibility(View.GONE);
            tvAddContinue.setVisibility(View.VISIBLE);
            rvAddPitures.setVisibility(View.VISIBLE);
            ArrayList<String> images = data.getStringArrayListExtra("images");
            adapter.addAll(images, true);
        }

        if (requestCode == 128 && resultCode == 121) {
            tvAddPicture.setVisibility(View.GONE);
            tvAddContinue.setVisibility(View.VISIBLE);
            rvAddPitures.setVisibility(View.VISIBLE);
            ArrayList<String> images = data.getStringArrayListExtra("images");
            adapter.addAll(images, false);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
