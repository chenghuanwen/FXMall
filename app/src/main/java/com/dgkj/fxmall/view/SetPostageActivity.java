package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.SetPostageAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.PostageBean;
import com.dgkj.fxmall.bean.SuperClassifyBean;
import com.dgkj.fxmall.bean.SuperPostageBean;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetAllPostageModelFinishedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class SetPostageActivity extends BaseActivity {
    @BindView(R.id.rv_postage_setting)
    RecyclerView rvPostageSetting;
    @BindView(R.id.tv_postage_introduce)
    TextView tvPostageIntroduce;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_addmode)
    Button btnAddModle;
    private View headerview;
    private SetPostageAdapter adapter;
    private List<SuperPostageBean> postageList;
    private FXMallControl control = new FXMallControl();
    private OkHttpClient client = new OkHttpClient.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_postage);
        ButterKnife.bind(this);
        initHeaderView();
        refresh();
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void refresh() {
        control.getAllPostage(this, sp.get("token"), client, new OnGetAllPostageModelFinishedListener() {
            @Override
            public void onGetAllPostageModelFinished(List<PostageBean> postageList) {
                //将所有运费模板按照id进行分类，相同的id属于同一个大模板
                List<SuperPostageBean> superPostageList = new ArrayList<>();
                Map<PostageBean, List<PostageBean>> map = new HashMap<>();
                PostageBean post = new PostageBean();
                if (postageList.size() == 0) {
                  return;
                } else {
                    for (int i = 0; i < postageList.size(); i++) {
                        int key = postageList.get(i).getId();//获取当条数据的id值
                        if (post.getId() >= 0) {
                            boolean b = key == post.getId();//当该id值与key值中的id值不同时，则创建新的key,保证key值唯一
                            if (b) {
                                post = new PostageBean();
                            }
                        }
                        post.setId(key);//为key值设置id

                        //将相同id 的key所有的数据都指向一个数据集合
                        List<PostageBean> posts = map.get(post);//key值变时，集合也会变
                        //当第一次次集合没有初始化时，创建一个新集合，此后这相同的数据全部添加到此集合
                        if (posts == null) {
                            posts = new ArrayList<>();
                        }
                        posts.add(postageList.get(i));//将相同数据添加到集合
                        map.put(post, posts);//将相同的数据放入map（不断覆盖，直至最后一次得到相同全部数据）
                    }

                    //TODO 集成数据:遍历map，将数据添加到listView实体类集合
                    Iterator iterator = map.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        PostageBean postageBean = (PostageBean) entry.getKey();

                        SuperPostageBean superPost = new SuperPostageBean();
                        int id = postageBean.getId();
                        ArrayList<PostageBean> posts = (ArrayList<PostageBean>) entry.getValue();
                        superPost.setId(id);
                        superPost.setPosts(posts);

                        superPostageList.add(superPost);
                    }

                    adapter.addAll(superPostageList, true);//刷新模板列表数据
                }
            }
        });
    }


    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "运费设置");
        postageList = new ArrayList<>();
       /* PostageBean postageBean = new PostageBean();
        postageBean.setModeName("默认运费模板");
        postageBean.setModeSetting("默认模板：10件内0元，每增加一件，运费增加1元");
        postageList.add(postageBean);*/
        adapter = new SetPostageAdapter(this, this, R.layout.item_postage_mode_setting, postageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvPostageSetting.setLayoutManager(layoutManager);
        rvPostageSetting.setAdapter(adapter);
    }


    @OnClick(R.id.btn_addmode)
    public void addModle() {
        Intent intent = new Intent(this, PostageSettingEditActivity.class);
        intent.putExtra("from", "add");
        startActivityForResult(intent, 131);
    }


    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 131 && resultCode == 132) {
            SuperPostageBean postage = (SuperPostageBean) data.getSerializableExtra("model");
            String from = data.getStringExtra("from");
            if("edit".equals(from)){
                int position = data.getIntExtra("position", -1);
                postageList.remove(position);
                postageList.add(position,postage);
                adapter.notifyItemChanged(position);
            }else {
                List<SuperPostageBean> list = new ArrayList<>();
                list.addAll(postageList);
                list.add(postage);
                adapter.addAll(list, true);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
