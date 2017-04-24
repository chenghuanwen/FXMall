package com.dgkj.fxmall.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.PostageDistrictSelectAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.DistrictSelectBean;
import com.dgkj.fxmall.bean.PostageBean;
import com.dgkj.fxmall.bean.SuperPostageBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.ProvinceManagerUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostageSettingEditActivity extends BaseActivity {
    @BindView(R.id.et_mode_name)
    EditText etModeName;
    @BindView(R.id.tv_paost_count)
    EditText tvPaostCount;
    @BindView(R.id.tv_paost_pay)
    EditText tvPaostPay;
    @BindView(R.id.tv_paost_add_count)
    EditText tvPaostAddCount;
    @BindView(R.id.tv_paost_add_pay)
    EditText tvPaostAddPay;
    @BindView(R.id.tv_add_district_postage)
    TextView tvAddDistrictPostage;

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.ll_model_container)
    LinearLayout llModelContainer;
    private View headerview;
    private ProvinceManagerUtil managerUtil = new ProvinceManagerUtil();
    private int position;//地区选择父选项位置
    private String from = "";
    private List<View> viewList = new ArrayList<>();
    private List<String> provinces = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private SuperPostageBean newSuperPost = new SuperPostageBean();
    private Map<Integer,List<String>> provinceMap = new HashMap<>();
    private int mapKey = 0;
    private SuperPostageBean editSuperPost;
    private int editPosition;
    private int postageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postage_setting_edit);
        ButterKnife.bind(this);
        initHeaderView();
        initData();
    }

    private void initData() {
        from = getIntent().getStringExtra("from");
        if ("edit".equals(from)) {
            editSuperPost = (SuperPostageBean) getIntent().getSerializableExtra("postage");
            editPosition = getIntent().getIntExtra("position", -1);
            List<PostageBean> posts = editSuperPost.getPosts();
            PostageBean postage = posts.get(0);
            etModeName.setText(postage.getModeName());
            tvPaostCount.setText(postage.getPostCount());
            tvPaostPay.setText(postage.getPostPay());
            tvPaostAddCount.setText(postage.getAddCount());
            tvPaostAddPay.setText(postage.getAddPay());
            if(posts.size() > 1){
                for (int i = 1; i < posts.size(); i++) {
                    addModel();
                    postageId = posts.get(i).getDistrictId();
                }
                for (int i = 0; i < viewList.size(); i++) {
                    View view = viewList.get(i);
                    TextView tvDistrict = (TextView) view.findViewById(R.id.tv_select_district);
                    EditText etPostCount = (EditText) view.findViewById(R.id.tv_paost_count_distirct);
                    EditText etPostPay = (EditText) view.findViewById(R.id.tv_paost_pay_district);
                    EditText etAddCount = (EditText) view.findViewById(R.id.tv_paost_add_count_district);
                    EditText etAddPay = (EditText) view.findViewById(R.id.tv_paost_add_pay_district);
                    PostageBean postageBean = posts.get(i + 1);
                    List<String> provinces = postageBean.getProvinces();
                    StringBuffer sb = new StringBuffer();
                    for (String province : provinces) {
                        sb.append(province).append("、");
                    }
                    sb.deleteCharAt(sb.length()-1);
                    tvDistrict.setText(sb.toString());
                    etPostCount.setText(postageBean.getPostCount());
                    etPostPay.setText(postageBean.getPostPay());
                    etAddCount.setText(postageBean.getAddCount());
                    etAddPay.setText(postageBean.getAddPay());
                }
            }

        }
    }

    private void initHeaderView() {
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview, "运费设置");
    }


    @OnClick(R.id.tv_add_district_postage)
    public void addModel() {
        final View view = getLayoutInflater().inflate(R.layout.layout_add_postage_model, null, false);
        ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
        final TextView tvDistrictSelect = (TextView) view.findViewById(R.id.tv_select_district);

        llModelContainer.addView(view);
        viewList.add(view);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llModelContainer.removeView(view);
                viewList.remove(view);
                if("edit".equals(from)){
                    //TODO 通知服务器删除该子项模板
                    deleteSubPostage(postageId);
                }
            }
        });
        tvDistrictSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog(tvDistrictSelect);
            }
        });
    }

    /**
     * 删除某个模板中某个地区模板
     * @param postageId
     */
    private void deleteSubPostage(int postageId) {
        FormBody body = new FormBody.Builder()
                .add("store.user.token",sp.get("token"))
                .add("freightProvinces.id",postageId+"")
                .build();
        Request request = new Request.Builder()
                .url(FXConst.DELETE_SUB_POSTAGE_MODEL)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(PostageSettingEditActivity.this,"网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().contains("1000")){
                    toastInUI(PostageSettingEditActivity.this,"删除成功");
                }else {
                    toastInUI(PostageSettingEditActivity.this,"删除失败");
                }
            }
        });
    }


    private void showSelectDialog(final TextView tv) {
        final StringBuffer sb = new StringBuffer();
        View contentview = getLayoutInflater().inflate(R.layout.layout_postage_district_select_dialog, null);
        final AlertDialog pw = new AlertDialog.Builder(this).create();
        pw.setView(contentview);
        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_confirm);
        TextView tvBoy = (TextView) contentview.findViewById(R.id.tv_cancle);
        tvBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });

        final ExpandableListView listView = (ExpandableListView) contentview.findViewById(R.id.elv_districts);

        final List<DistrictSelectBean> parentList = managerUtil.getDistricts();
        final Map<String, List<DistrictSelectBean>> dataSet = new HashMap<>();
        for (int i = 0; i < parentList.size(); i++) {
            dataSet.put(parentList.get(i).getDistrict(), managerUtil.getProvince(i));
        }
        final PostageDistrictSelectAdapter adapter = new PostageDistrictSelectAdapter(parentList, dataSet, this);
        listView.setAdapter(adapter);

        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {//关闭其他展开项
                position = groupPosition;
                for (int i = 0; i < parentList.size(); i++) {
                    if (groupPosition != i) {
                        listView.collapseGroup(i);
                    }
                }
            }
        });


        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sb.append(managerUtil.queryDistrictForProvince(position));
                sb.append("(");
                List<String> selectDistricts = adapter.getSelectDistricts();

                provinceMap.put(mapKey,selectDistricts);
                mapKey++;

                StringBuffer province = new StringBuffer();
                province.append("[");
                for (String selectDistrict : selectDistricts) {
                    province.append(selectDistrict).append(",");
                }
                province.deleteCharAt(province.length() - 1);
                province.append("]");
                provinces.add(province.toString());

                for (int i = 0; i < selectDistricts.size(); i++) {
                    if (i == 0) {
                        sb.append(selectDistricts.get(i) + "、");
                    }
                    if (i == 1) {
                        sb.append(selectDistricts.get(i));
                    }
                }
                sb.append("...");
                tv.setText("地区：" + sb.toString());
                pw.dismiss();
            }
        });


        //设置触摸对话框以外区域，对话框消失
        pw.setCanceledOnTouchOutside(true);
        pw.show();
    }

    @OnClick(R.id.btn_confirm)
    public void addFinish() {
        List<PostageBean> postlist = new ArrayList<>();
        String name = etModeName.getText().toString();
        String postCount = tvPaostCount.getText().toString();
        String postPay = tvPaostPay.getText().toString();
        String addCount = tvPaostAddCount.getText().toString();
        String addPay = tvPaostAddPay.getText().toString();


        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(postCount) || TextUtils.isEmpty(postPay) || TextUtils.isEmpty(addCount) || TextUtils.isEmpty(addPay)) {
            toast("请输入完整信息！");
            return;
        }

        PostageBean post = new PostageBean();
        post.setModeName(name);
        post.setPostCount(postCount);
        post.setPostPay(postPay);
        post.setAddCount(addCount);
        post.setAddPay(addPay);
        StringBuffer sb = new StringBuffer();
        sb.append(postCount).append("件内").append(postPay)
                .append("元").append(",每增加").append(addCount).append("件,增加")
                .append(addPay).append("元");
        post.setModeSetting(sb.toString());
        postlist.add(post);

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("store.user.token", sp.get("token"))
                .add("name", name)
                .add("freightProvinces[0].nums",postCount)
                .add("freightProvinces[0].cost",postPay)
                .add("freightProvinces[0].increaseNum",addCount)
                .add("freightProvinces[0].increaseCost",addPay)
                .add("freightProvinces[0].provinces","[]");
        if("edit".equals(from)){
            builder.add("freightProvinces[0].id",editSuperPost.getPosts().get(0).getId()+"")
                    .add("id",editSuperPost.getId()+"");
        }

        for (int i = 0; i < viewList.size(); i++) {
            View view = viewList.get(i);
            EditText etPostCount = (EditText) view.findViewById(R.id.tv_paost_count_distirct);
            EditText etPostPay = (EditText) view.findViewById(R.id.tv_paost_pay_district);
            EditText etAddCount = (EditText) view.findViewById(R.id.tv_paost_add_count_district);
            EditText etAddPay = (EditText) view.findViewById(R.id.tv_paost_add_pay_district);
            String count = etPostCount.getText().toString();
            String pay = etPostPay.getText().toString();
            String addPost = etAddCount.getText().toString();
            String addPostPay = etAddPay.getText().toString();

            if(TextUtils.isEmpty(count) || TextUtils.isEmpty(pay) || TextUtils.isEmpty(addPost) || TextUtils.isEmpty(addPostPay)){
                toast("请先填写完整模板信息");
                return;
            }

            PostageBean postageBean= new PostageBean();
            postageBean.setPostCount(count);
            postageBean.setPostPay(pay);
            postageBean.setAddCount(addPost);
            postageBean.setAddPay(addPostPay);
            postageBean.setProvinces(provinceMap.get(i));
            postlist.add(postageBean);

            builder.add("freightProvinces["+(i+1)+"].nums",count)
                    .add("freightProvinces["+(i+1)+"].cost",pay)
                    .add("freightProvinces["+(i+1)+"].increaseNum",addPost)
                    .add("freightProvinces["+(i+1)+"].increaseCost",addPostPay)
                    .add("freightProvinces["+(i+1)+"].provinces",provinces.get(i));
            if("edit".equals(from)){
                builder.add("freightProvinces["+(i+1)+"].id",editSuperPost.getPosts().get(i+1).getId()+"")
                        .add("id",editSuperPost.getId()+"");
            }
        }
        //用于返回上个界面显示
        newSuperPost.setPosts(postlist);

        FormBody body = builder.build();
        Request.Builder requestBuild = new Request.Builder();
        requestBuild.post(body);
        if("edit".equals(from)){
            requestBuild.url(FXConst.UPDATE_POSTAGE_MODEL);
        }else {
            requestBuild.url(FXConst.ADD_POSTAGE_MODEL);
        }
        Request request = requestBuild.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(PostageSettingEditActivity.this,"网络错误！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    if(response.body().string().contains("1000")){
                        toastInUI(PostageSettingEditActivity.this,"已成功新增运费模板");
                        uploadPostageFinish();
                    }else { toastInUI(PostageSettingEditActivity.this,"新增运费模板失败");}
            }
        });


    }

    private void uploadPostageFinish() {

        Intent intent = new Intent();
        intent.putExtra("model", newSuperPost);
        if("edit".equals(from)){
            intent.putExtra("from","edit");
            intent.putExtra("position",editPosition);
        }else {
            intent.putExtra("from","new");
        }
        setResult(132, intent);
        finish();
    }

    @OnClick(R.id.ib_back)
    public void back() {
        finish();
    }
}
