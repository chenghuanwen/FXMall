package com.dgkj.fxmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.adapter.ShoppingCarAdapter;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.bean.PostageBean;
import com.dgkj.fxmall.bean.ShoppingCarBean;
import com.dgkj.fxmall.bean.ShoppingGoodsBean;
import com.dgkj.fxmall.bean.SuperPostageBean;
import com.dgkj.fxmall.constans.Position;
import com.dgkj.fxmall.control.FXMallControl;
import com.dgkj.fxmall.listener.OnGetShoppingCarDataListener;
import com.dgkj.fxmall.listener.OnGetShoppingcarProductsFinishedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class ShoppingCarActivity extends BaseActivity {

    @BindView(R.id.rv_shopping_car)
    RecyclerView rvShoppingCar;
    @BindView(R.id.iv_car_back)
    ImageView ivCarBack;
    @BindView(R.id.cb_select_all)
    CheckBox cbSelectAll;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.btn_pay)
    Button btnPay;

    private FXMallControl control;
    private View headerview;
    private int goodsCount;//商品总数
    private ShoppingCarAdapter adapter;
    private List<ShoppingCarBean> carBeanList;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private int sumPrice = 0;
    private int index = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    sumPrice += msg.arg1 * msg.arg2;
                    tvTotalPrice.setText("¥" + sumPrice);
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    sumPrice -= msg.arg1 * msg.arg2;
                    tvTotalPrice.setText("¥" + sumPrice);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_car);
        ButterKnife.bind(this);
        initHeaderview();
        initShoppingCar();
        selectAll();
        refresh();
    }

    private void refresh() {
        control.getShopingCarData(new OnGetShoppingCarDataListener() {
            @Override
            public void onGetShoppingCarDataFinished(List<ShoppingCarBean> carBeanList) {
                goodsCount = carBeanList.size();
                adapter.addAll(carBeanList,true);
            }
        });
        control.getShoppingcarProducts(this, sp.get("token"), index, 10, client, new OnGetShoppingcarProductsFinishedListener() {
            @Override
            public void onGetShoppingcarProductsFinished(List<ShoppingGoodsBean> carBeanList) {
                goodsCount = carBeanList.size();
                //将所有商品按照店名进行分类，相同的店名放入同一集合1
                List<ShoppingCarBean> carList = new ArrayList<>();
                Map<ShoppingGoodsBean, List<ShoppingGoodsBean>> map = new HashMap<>();
                ShoppingGoodsBean product = new ShoppingGoodsBean();
                if (carBeanList.size() == 0) {
                    return;
                } else {
                    for (int i = 0; i < carBeanList.size(); i++) {
                        String key = carBeanList.get(i).getStoreName();//获取当条数据的店名值
                        if (product.getStoreId() >= 0) {
                            boolean b = key.equals(product.getStoreName());//当该店名值与key值中的店名值不同时，则创建新的key,保证key值唯一
                            if (b) {
                                product = new ShoppingGoodsBean();
                            }
                        }
                        product.setStoreName(key);//为key值设置店名

                        //将相同店名 的key所有的数据都指向一个数据集合
                        List<ShoppingGoodsBean> products = map.get(product);//key值变时，集合也会变
                        //当第一次次集合没有初始化时，创建一个新集合，此后这相同的数据全部添加到此集合
                        if (products == null) {
                            products = new ArrayList<>();
                        }
                        products.add(carBeanList.get(i));//将相同数据添加到集合
                        map.put(product, products);//将相同的数据放入map（不断覆盖，直至最后一次得到相同全部数据）
                    }

                    //TODO 集成数据:遍历map，将数据添加到listView实体类集合
                    Iterator iterator = map.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        ShoppingGoodsBean goods = (ShoppingGoodsBean) entry.getKey();

                        ShoppingCarBean carBean = new ShoppingCarBean();
                        String name = goods.getStoreName();
                        ArrayList<ShoppingGoodsBean> goodsList = (ArrayList<ShoppingGoodsBean>) entry.getValue();
                        carBean.setStoreName(name);
                        carBean.setGoods(goodsList);

                        carList.add(carBean);
                    }

                    adapter.addAll(carList, true);//刷新购物车列表数据
                }
            }
        });
    }



    /**
     * 初始化数据列表
     */
    private void initShoppingCar() {
        carBeanList = new ArrayList<>();
        adapter = new ShoppingCarAdapter(this,R.layout.item_shoppingcar,carBeanList,handler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvShoppingCar.setLayoutManager(layoutManager);
        rvShoppingCar.setAdapter(adapter);

    }

    /**
     * 全选操作
     */
    private void selectAll() {
        cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    adapter.selectAll();
                }else {
                    adapter.cancleAll();
                }
            }
        });
    }

    /**
     * 设置头部信息
     */
    private void initHeaderview() {
        control = new FXMallControl();
        //TODO 获取购物车商品总数
        String title = String.format("购物车（%s",goodsCount+")");
        headerview = findViewById(R.id.headerview);
        setHeaderTitle(headerview,title);
        setHeaderImage(headerview, -1,"编辑", Position.RIGHT, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //TODO 跳转到编辑界面
                Intent intent = new Intent(ShoppingCarActivity.this,ShoppingCarEditActivity.class);
                intent.putExtra("data", (Serializable) carBeanList);
                startActivityForResult(intent,129);
            }
        });
    }


    @OnClick(R.id.iv_car_back)
    public void back(){
        finish();
    }

    @OnClick(R.id.btn_pay)
    public void pay(){
        //TODO 计算总价格付账
        Intent intent = new Intent(this,ConfirmOrderActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==129 && resultCode==130){
            List<ShoppingCarBean> list = (List<ShoppingCarBean>) data.getSerializableExtra("data");
            adapter.addAll(list,true);
        }
    }
}
