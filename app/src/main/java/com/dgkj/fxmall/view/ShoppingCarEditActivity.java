package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.ShoppingCarEditAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.EditCarSelectedBean;
import com.dgkj.fxmall.bean.ShoppingCarBean;
import com.dgkj.fxmall.bean.ShoppingGoodsBean;
import com.dgkj.fxmall.bean.ShoppingcarProductCountChangeBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.constans.Position;
import com.dgkj.fxmall.listener.OnSelectColorSizeFinishedListener;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.view.myView.SelectColorAndSizeDialog;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShoppingCarEditActivity extends BaseActivity {

    @BindView(R.id.rv_shoppingCar_edit)
    RecyclerView rvShoppingCarEdit;
    @BindView(R.id.cb_car_deit_all)
    CheckBox cbCarDeitAll;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.activity_shopping_car_edit)
    LinearLayout activityShoppingCarEdit;

    private View headerview;
    private int goodsCount;
    private List<ShoppingCarBean> carBeanList;
    private ShoppingCarEditAdapter adapter;
    private List<ShoppingGoodsBean> changeList;
    private List<EditCarSelectedBean> selectPositions = new ArrayList<>();
    private List<ShoppingcarProductCountChangeBean> countChangeBeanList;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://选择某个商品
                    EditCarSelectedBean editBean = new EditCarSelectedBean();
                    editBean.setSubPosition(msg.arg2);
                    editBean.setSuperPosition(msg.arg1);
                    selectPositions.add(editBean);
                    adapter.notifyDataSetChanged();
                    break;
                case 2://取消选中
                    EditCarSelectedBean editBean1 = new EditCarSelectedBean();
                    editBean1.setSubPosition(msg.arg2);
                    editBean1.setSuperPosition(msg.arg1);
                    selectPositions.remove(editBean1);
                    adapter.notifyDataSetChanged();
                    break;
                case 3://购买数量改变
                    ArrayList<ShoppingGoodsBean> currentItemGoods = carBeanList.get(msg.arg1).getGoods();
                    ShoppingGoodsBean goods = (ShoppingGoodsBean) msg.obj;
                    currentItemGoods.remove(msg.arg2);
                    currentItemGoods.add(msg.arg2, goods);
                    carBeanList.get(msg.arg1).setGoods(currentItemGoods);

                    ShoppingcarProductCountChangeBean changeBean = new ShoppingcarProductCountChangeBean();
                    changeBean.setCarId(goods.getCarId());
                    changeBean.setChangeCount(goods.getCount());
                    countChangeBeanList.add(changeBean);
                    break;
                case 4:
                    //TODO 弹出颜色尺寸选择框
                    final ShoppingGoodsBean product = (ShoppingGoodsBean) msg.obj;
                    SelectColorAndSizeDialog dialog = new SelectColorAndSizeDialog(ShoppingCarEditActivity.this, R.layout.layout_edit_select_color_size, product, "edit", new OnSelectColorSizeFinishedListener() {
                        @Override
                        public void selectCompelete(String result) {
                            product.setColor(result);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    dialog.showPopupWindow(activityShoppingCarEdit);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_car_edit);
        ButterKnife.bind(this);

        initHeaderview();
        selectAll();
        initDisplayData();

    }

    @Override
    public View getContentView() {
        return activityShoppingCarEdit;
    }


    /**
     * 设置头部信息
     */
    private void initHeaderview() {

        countChangeBeanList = new ArrayList<>();

        headerview = findViewById(R.id.headerview);
        setHeaderImage(headerview, -1, "完成", Position.RIGHT, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 编辑完成，通知服务器数量改变
                for (ShoppingcarProductCountChangeBean changeBean : countChangeBeanList) {
                    notifyProductCountChange(changeBean);
                }

                //返回购物车
                Intent intent = new Intent(ShoppingCarEditActivity.this, ShoppingCarActivity.class);
                intent.putParcelableArrayListExtra("data", (ArrayList<ShoppingCarBean>) carBeanList);
                setResult(130, intent);
                finish();
            }
        });
    }

    /**
     * 上传购物车中商品改变数量
     */
    private void notifyProductCountChange(ShoppingcarProductCountChangeBean bean) {
        FormBody body = new FormBody.Builder()
                .add("user.token", sp.get("token"))
                .add("num", bean.getChangeCount() + "")
                .add("ids", bean.getCarId() + "")
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(FXConst.CHAGNE_PRODUCT_COUNT_IN_CAR)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


    private void initDisplayData() {
        carBeanList = getIntent().getParcelableArrayListExtra("data");

        //TODO 获取购物车商品总数
        goodsCount = carBeanList.size();
        String title = String.format("购物车（%s", goodsCount + ")");
        setHeaderTitle(headerview, title);

        changeList = new ArrayList<>();
        adapter = new ShoppingCarEditAdapter(this, R.layout.item_shoppingcar, carBeanList, handler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvShoppingCarEdit.setLayoutManager(layoutManager);
        rvShoppingCarEdit.setAdapter(adapter);

    }

    /**
     * 全选操作
     */
    private void selectAll() {
        cbCarDeitAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.selectAll();
                } else {
                    adapter.cancleAll();
                }
            }
        });
    }

    @OnClick(R.id.btn_delete)
    public void delete() {
        //TODO 通知服务器删除选中商品
        int size = selectPositions.size();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("user.token", sp.get("token"));
        for (EditCarSelectedBean selectPosition : selectPositions) {
            builder.add("ids", carBeanList.get(selectPosition.getSuperPosition()).getGoods().get(selectPosition.getSubPosition()).getCarId() + "");
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(FXConst.DELETE_PRODUCT_COUNT_IN_CAR)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastInUI(ShoppingCarEditActivity.this, "网络异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body().string().contains("1000")) {
                    toastInUI(ShoppingCarEditActivity.this, "删除成功");
                } else {
                    toastInUI(ShoppingCarEditActivity.this, "删除失败");
                }
            }
        });

        //删除本地数据
        if (size > 0) {
            LogUtil.i("TAG", selectPositions.toString());
            for (EditCarSelectedBean selectPosition : selectPositions) {
                ArrayList<ShoppingGoodsBean> currentItemGoods = carBeanList.get(selectPosition.getSuperPosition()).getGoods();
                currentItemGoods.remove(selectPosition.getSubPosition());
                carBeanList.get(selectPosition.getSuperPosition()).setGoods(currentItemGoods);
                if (carBeanList.get(selectPosition.getSuperPosition()).getGoods().size() == 0) {
                    carBeanList.remove(selectPosition.getSuperPosition());
                }
            }
        }
        adapter.notifyDataSetChanged();
        // adapter.addAll(carBeanList,true);
    }
}
