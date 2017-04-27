package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.ViewFlipperAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.PostageBean;
import com.dgkj.fxmall.bean.PostageModelSelectBean;
import com.dgkj.fxmall.bean.SuperPostageBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetAllPostageModelFinishedListener;
import com.dgkj.fxmall.utils.OkhttpUploadUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class PublishProductActivity extends BaseActivity {
    @BindView(R.id.avf_products)
    AdapterViewFlipper avfProducts;
    @BindView(R.id.tv_add_product_photos)
    TextView tvAddProductPhotos;
    @BindView(R.id.et_product_title)
    EditText etProductTitle;
    @BindView(R.id.et_product_destribe)
    EditText etProductDestribe;
    @BindView(R.id.et_product_type)
    EditText etProductType;
    @BindView(R.id.et_product_price)
    EditText etProductPrice;
    @BindView(R.id.et_product_save_count)
    EditText etProductSaveCount;
    @BindView(R.id.et_demand_count)
    EditText etDemandCount;
    @BindView(R.id.ll_add_type_container)
    LinearLayout llAddTypeContainer;
    @BindView(R.id.tv_product_detial)
    TextView tvProductDetial;
    @BindView(R.id.tv_product_classify)
    TextView tvProductClassify;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_add_type)
    TextView tvAddType;
    @BindView(R.id.tv_postage_model)
    TextView tvPostageModel;
    @BindView(R.id.rv_postage_select)
    ListView rvPostageSelect;
    @BindView(R.id.cb_deliver)
    CheckBox cbDeliver;
    @BindView(R.id.activity_publish_product)
    LinearLayout activityPublishProduct;
    private View headerview;
    private String classify = "";
    private List<View> viewList = new ArrayList<>();
    private List<File> mainImages = new ArrayList<>();
    private List<File> detialImages = new ArrayList<>();
    private int classifyId, postageId;
    private List<SuperPostageBean> superPostageList;
    private List<PostageModelSelectBean> postageModelSelectList;
    private CommonAdapter<PostageModelSelectBean> selectAdapter;
    private FXMallControl control = new FXMallControl();
    private OkHttpClient client = new OkHttpClient.Builder().build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_product);
        ButterKnife.bind(this);
        initHeaderView();
        getPostage();
        setListener();

    }

    @Override
    public View getContentView() {
        return activityPublishProduct;
    }

    /**
     * 获取运费模板
     */
    private void getPostage() {
        control.getAllPostage(this, sp.get("token"), client, new OnGetAllPostageModelFinishedListener() {
            @Override
            public void onGetAllPostageModelFinished(List<PostageBean> postageList) {
                //将所有运费模板按照id进行分类，相同的id属于同一个大模板
                superPostageList = new ArrayList<>();
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
                    for (int i = 0; i < superPostageList.size(); i++) {
                        PostageModelSelectBean selectBean = new PostageModelSelectBean();
                        SuperPostageBean superPostageBean = superPostageList.get(i);
                        selectBean.setId(superPostageBean.getId());
                        selectBean.setName(superPostageBean.getPosts().get(0).getModeName());
                        postageModelSelectList.add(selectBean);
                    }
                }
            }
        });
    }

    private void setListener() {
        cbDeliver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvPostageModel.setVisibility(View.VISIBLE);
                    //rvPostageSelect.setVisibility(View.VISIBLE);
                } else {
                    tvPostageModel.setVisibility(View.GONE);
                    rvPostageSelect.setVisibility(View.GONE);
                }
            }
        });

        rvPostageSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostageModelSelectBean model = postageModelSelectList.get(position);
                tvPostageModel.setText(model.getName());
                postageId = model.getId();
                rvPostageSelect.setVisibility(View.GONE);
            }
        });
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "发布商品");

        postageModelSelectList = new ArrayList<>();
    }

    @OnClick(R.id.tv_add_product_photos)
    public void addPotohs() {
        startActivityForResult(new Intent(this, PictrueChooserActivity.class), 130);
    }


    @OnClick(R.id.tv_add_type)
    public void addType() {
        final View view = getLayoutInflater().inflate(R.layout.layout_add_product_type, null, false);
        ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
        llAddTypeContainer.addView(view);
        viewList.add(view);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llAddTypeContainer.removeView(view);
                viewList.remove(view);
            }
        });
    }

    @OnClick(R.id.tv_product_detial)
    public void productDetial() {
        startActivityForResult(new Intent(this, PublishProductDetialActivity.class), 127);
    }

    @OnClick(R.id.btn_submit)
    public void pubish() {
        StringBuffer sbType = new StringBuffer();
        String titel = etProductTitle.getText().toString();
        String describe = etProductDestribe.getText().toString();
        String brokerage = etDemandCount.getText().toString();
        String inventory = etProductSaveCount.getText().toString();
        String colorSize = etProductType.getText().toString();
        String price = etProductPrice.getText().toString();
        if (TextUtils.isEmpty(titel) || TextUtils.isEmpty(describe) || TextUtils.isEmpty(brokerage) || TextUtils.isEmpty(inventory)
                || TextUtils.isEmpty(colorSize) || TextUtils.isEmpty(price)) {
            toast("请输入完整信息");
            return;
        }
        StringBuffer sb = new StringBuffer();//{"content":"黄色","price":20,"inventory":100,"brokerage":5},
        sb.append("{\"content\":\"" + colorSize + "\",\"price\":" + price + ",\"inventory\":" + inventory + ",\"brokerage\":" + brokerage + "}");
        sbType.append("[").append(sb.toString() + ",");
        for (int i = 0; i < viewList.size(); i++) {
            View view = viewList.get(i);
            EditText etColor = (EditText) view.findViewById(R.id.et_product_type);
            EditText etPrice = (EditText) view.findViewById(R.id.et_product_price);
            EditText etInventory = (EditText) view.findViewById(R.id.et_product_save_count);
            EditText etBrokerage = (EditText) view.findViewById(R.id.et_demand_count);
            String addColor = etColor.getText().toString();
            String addPrice = etPrice.getText().toString();
            String addInventory = etInventory.getText().toString();
            String addBrokerage = etBrokerage.getText().toString();
            if (TextUtils.isEmpty(addColor) || TextUtils.isEmpty(addPrice) || TextUtils.isEmpty(addInventory) || TextUtils.isEmpty(addBrokerage)) {
                toast("请输入完整信息");
                return;
            }
            if (classifyId < 0) {
                toast("你还未选择商品分类");
            }

            StringBuffer sbAdd = new StringBuffer();//{"content":"黄色","price":20,"inventory":100,"brokerage":5},
            sb.append("{\"content\":\"" + addColor + "\",\"price\":" + addPrice + ",\"inventory\":" + addInventory + ",\"brokerage\":" + addBrokerage + "}");
            sbType.append(sbAdd.toString() + ",");
        }
        sbType.deleteCharAt(sbType.length() - 1);
        sbType.append("]");

        Map<String, String> params = new HashMap<>();
        params.put("commodity.store.user.token", sp.get("token"));
        params.put("commodity.name", titel);
        params.put("commodity.detail", describe);
        params.put("commodity.subCastegory.id", classifyId + "");
        params.put("commodity.freightModel.id", postageId + "");//TODO 区分：若没有运费模板
        params.put("jsonString", sbType.toString());

        OkhttpUploadUtils.getInstance(this).sendMultipart(FXConst.PUBLISH_PRODUCT_URL, params, "file", mainImages, "commodity.url", detialImages);
        //TODO 添加上传进度提示

        MyApplication.selectedPictures = null;
    }

    @OnClick(R.id.tv_product_classify)
    public void classify() {
        startActivityForResult(new Intent(this, AllClassifyActivity.class), 118);
    }

    @OnClick(R.id.tv_postage_model)
    public void selectPostage() {
        rvPostageSelect.setVisibility(View.VISIBLE);
        selectAdapter = new CommonAdapter<PostageModelSelectBean>(this, R.layout.item_all_classify, postageModelSelectList) {
            @Override
            protected void convert(ViewHolder viewHolder, PostageModelSelectBean item, int position) {
                viewHolder.setText(R.id.tv_base_classify, item.getName());
            }
        };
        rvPostageSelect.setAdapter(selectAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 130 && resultCode == 121) {
            ArrayList<String> images = data.getStringArrayListExtra("images");
            tvAddProductPhotos.setVisibility(View.GONE);
            avfProducts.setAdapter(new ViewFlipperAdapter(images, this));
            avfProducts.setAutoStart(true);
            avfProducts.startFlipping();

            for (String image : images) {
                File file = new File(image);
                mainImages.add(file);
            }
        }

        if (requestCode == 127 && resultCode == 129) {
            ArrayList<String> sort = data.getStringArrayListExtra("sort");
            tvProductDetial.setText("商品详情（已选" + sort.size() + "张照片）");
            MyApplication.selectedPictures = sort;

            for (String path : sort) {
                File file = new File(path);
                detialImages.add(file);
            }

        } else if (requestCode == 118 && resultCode == 122) {
            classify = data.getStringExtra("classify");
            tvProductClassify.setText(classify);
            classifyId = data.getIntExtra("subId", -1);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick(R.id.ib_back)
    public void back() {
        MyApplication.selectedPictures = null;
        finish();
    }


}
