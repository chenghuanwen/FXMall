package com.dgkj.fxmall.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.bean.StoreBean;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.LoadProgressDialogUtil;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.utils.ToastUtil;
import com.dgkj.fxmall.view.ApplyStoreActivity;
import com.dgkj.fxmall.view.HomePageActivity;
import com.dgkj.fxmall.view.MainActivity;
import com.dgkj.fxmall.view.MessageCenterActivity;
import com.dgkj.fxmall.view.MyDemandActivity;
import com.dgkj.fxmall.view.MyOrdersActivity;
import com.dgkj.fxmall.view.PublishDemandActivity;
import com.dgkj.fxmall.view.RechargeActivity;
import com.dgkj.fxmall.view.RefundActivity;
import com.dgkj.fxmall.view.ReviewProgressActivity;
import com.dgkj.fxmall.view.SettingActivity;
import com.dgkj.fxmall.view.ShoppingCarActivity;
import com.dgkj.fxmall.view.StoreMainPageActivity;
import com.dgkj.fxmall.view.TheBalanceOfUserActivity;
import com.dgkj.fxmall.view.UserMsgActivity;
import com.dgkj.fxmall.view.WithdrawalActivity;
import com.dgkj.fxmall.view.myView.ShareInvitaCodeDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MineFragment extends Fragment {

    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_msg_count)
    TextView tvMsgCount;
    @BindView(R.id.fl_msg_center)
    FrameLayout flMsgCenter;
    @BindView(R.id.civ_my_icon)
    CircleImageView civMyIcon;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_invite_code)
    TextView tvInviteCode;
    @BindView(R.id.tv_share_code)
    TextView tvShareCode;
    @BindView(R.id.tv_all_orders)
    TextView tvAllOrders;
    @BindView(R.id.tv_wait_pay)
    TextView tvWaitPay;
    @BindView(R.id.tv_wait_fahuo)
    TextView tvWaitFahuo;
    @BindView(R.id.tv_wait_shouhuo)
    TextView tvWaitShouhuo;
    @BindView(R.id.tv_wait_comment)
    TextView tvWaitComment;
    @BindView(R.id.tv_tuihuo)
    TextView tvTuihuo;
    @BindView(R.id.tv_account_rest)
    TextView tvAccountRest;
    @BindView(R.id.rl_recharge)
    RelativeLayout rlRecharge;
    @BindView(R.id.rl_withdrawal)
    RelativeLayout rlWithdrawal;
    @BindView(R.id.rl_my_demand)
    RelativeLayout rlMyDemand;
    @BindView(R.id.rl_apply_store)
    RelativeLayout rlApplyStore;
    @BindView(R.id.rl_post_demand)
    RelativeLayout rlPostDemand;
    @BindView(R.id.rl_shoppingcar)
    RelativeLayout rlShoppingcar;
    @BindView(R.id.rl_balance)
    RelativeLayout rlBalance;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.tv_jump_tip)
    TextView tvJumpTip;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private float downX, downY = 300;
    private SharedPreferencesUnit sp;
    private OkHttpClient client;
    private double balance;
    private String iconUrl;
    private String invitationCode;
    private String phone;
    private String gender;
    private String nickname;
    private String realname;
    private String location;
    private LoadProgressDialogUtil progressDialogUtil;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mine, container, false);
        ButterKnife.bind(this, view);

        progressDialogUtil = new LoadProgressDialogUtil(getContext());
        progressDialogUtil.buildProgressDialog();

        getUserInfo();
        //setData();
        refresh2Home();

        if (MyApplication.msgCount == 0) {
            tvMsgCount.setVisibility(View.GONE);
        } else {
            tvMsgCount.setVisibility(View.VISIBLE);
            tvMsgCount.setText(MyApplication.msgCount + "");
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void getUserInfo() {
        sp = SharedPreferencesUnit.getInstance(getContext());
        client = new OkHttpClient.Builder().build();
        final FormBody body = new FormBody.Builder()
                .add("token", sp.get("token"))
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_USER_INFO)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.i("TAG","用户信息=="+result);
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject info = object.getJSONObject("dataset");
                    balance = info.getDouble("balance");
                    MyApplication.balance = balance;
                    LogUtil.i("TAG", "用户信息===" + result + "账户余额==" + balance);
                    if(info.has("headPortrait")){
                        iconUrl = info.getString("headPortrait");
                    }
                    invitationCode = info.getString("invitationCode");
                    sp.put("invite",invitationCode);
                    phone = info.getString("phone");
                    gender = info.getInt("sex") + "";
                    nickname = info.getString("nickname");
                    realname = info.getString("realname");
                    location = info.getString("location");
                    double cashPledge = info.getDouble("cashPledge");
                    if(cashPledge > 0){//已充值押金
                        sp.put("ywy","true");//已成为业务员
                    }
                    setData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void refresh2Home() {
        srlRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        srlRefresh.setNestedScrollingEnabled(true);
        srlRefresh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                int top = scrollView.getTop();
                // LogUtil.i("TAG","scrollview距离顶部距离=="+top);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = x;
                        downY = y;
                        //  LogUtil.i("TAG", "按下时坐标== x=" + downX + "==y==" + downY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = x - downX;
                        float dy = y - downY;
                        //  LogUtil.i("TAG", "滑动距离 x==" + dx + "y===" + dy);
                        if (dy > 0 && Math.abs(dy) > Math.abs(dx) && dy > 300) {
                            //   LogUtil.i("TAG", "向下滑动======");
                          /*  layoutParams.height = 200;
                            tvJumpTip.setLayoutParams(layoutParams);*/
                            tvJumpTip.setVisibility(View.VISIBLE);

                        }/* else if(dy>0 && Math.abs(dy)>Math.abs(dx) && (dy>200 && dy<300)) {
                            LogUtil.i("TAG", "向上滑动======");
                            layoutParams.height = 100;
                            tvJumpTip.setLayoutParams(layoutParams);
                            tvJumpTip.setVisibility(View.VISIBLE);
                        }*/ else {
                            tvJumpTip.setVisibility(View.GONE);
                        }
                        break;
                }
                return false;
            }
        });
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlRefresh.setRefreshing(false);
                tvJumpTip.setVisibility(View.GONE);
                Intent intent = new Intent(getContext(), HomePageActivity.class);
                intent.putExtra("from", "");
                startActivity(intent);
            }
        });
    }

    private void setData() {
        //TODO 设置邀请码和账户余额
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(getContext()).load(iconUrl).error(R.mipmap.wd_mrtx).into(civMyIcon);
                try {
                    if (!TextUtils.isEmpty(nickname)) {
                        String nick = URLDecoder.decode(nickname, "utf-8");
                        tvUsername.setText(nick);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                tvInviteCode.setText("邀请码:"+invitationCode);
                tvAccountRest.setText(balance + "");
            }
        });
        progressDialogUtil.cancelProgressDialog();
    }

    @OnClick(R.id.civ_my_icon)
    public void selectIcon() {
        Intent intent = new Intent(getContext(), UserMsgActivity.class);
        intent.putExtra("icon", iconUrl);
        intent.putExtra("nick", nickname);
        intent.putExtra("gender", gender);
        intent.putExtra("realname", realname);
        intent.putExtra("phone", phone);
        intent.putExtra("address", location);
        getContext().startActivity(intent);
    }

    @OnClick(R.id.iv_setting)
    public void setting() {
        Intent intent = new Intent(getContext(), SettingActivity.class);
        intent.putExtra("icon", iconUrl);
        intent.putExtra("nick", nickname);
        intent.putExtra("code", invitationCode);
        intent.putExtra("gender", gender);
        intent.putExtra("realname", realname);
        intent.putExtra("phone", phone);
        intent.putExtra("address", location);
        getContext().startActivity(intent);

    }

    @OnClick(R.id.fl_msg_center)
    public void msgNotify() {
        getContext().startActivity(new Intent(getContext(), MessageCenterActivity.class));
    }

    @OnClick({R.id.tv_invite_code, R.id.tv_share_code})
    public void shareInvitaCode() {
        ShareInvitaCodeDialog dialog = new ShareInvitaCodeDialog(tvInviteCode.getText().toString());
        dialog.show(getFragmentManager(), "");
    }


    @OnClick(R.id.tv_all_orders)
    public void allOrders() {
        jump2Orders("all");
    }

    @OnClick(R.id.tv_wait_pay)
    public void waitPay() {
        jump2Orders("pay");
    }

    @OnClick(R.id.tv_wait_fahuo)
    public void waitDeliver() {
        jump2Orders("deliver");
    }

    @OnClick(R.id.tv_wait_shouhuo)
    public void waitTakeGoods() {
        jump2Orders("take");
    }

    @OnClick(R.id.tv_wait_comment)
    public void waitComment() {
        jump2Orders("comment");
    }

    @OnClick(R.id.tv_tuihuo)
    public void refund() {
        getContext().startActivity(new Intent(getContext(), RefundActivity.class));
    }

    @OnClick(R.id.rl_recharge)
    public void recharge() {
        Intent intent = new Intent(getContext(), RechargeActivity.class);
        intent.putExtra("from","mine");
        getContext().startActivity(intent);
    }

    @OnClick(R.id.rl_withdrawal)
    public void withdawal() {
        Intent intent = new Intent(getContext(),HomePageActivity.class);
        intent.putExtra("from","yw");
        getContext().startActivity(intent);
       // getContext().startActivity(new Intent(getContext(), WithdrawalActivity.class));
    }

    @OnClick(R.id.rl_apply_store)
    public void applayStore() {
        getMyStoreInfo();
    }

    /**
     * 获取店铺申请审核状态
     */
    private void getMyStoreInfo() {
        FormBody body = new FormBody.Builder()
                .add("user.token", sp.get("token"))
                .build();
        Request request = new Request.Builder()
                .url(FXConst.GET_STORE_DETIAL_INFO)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                LogUtil.i("TAG", "店铺信息===" + string);
                Intent intent = new Intent(getContext(), ReviewProgressActivity.class);
                if (string.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(string);
                        JSONObject dataset = object.getJSONObject("dataset");
                        StoreBean storeBean = new StoreBean();
                        storeBean.setName(dataset.getString("storeName"));
                        storeBean.setAdress(dataset.getString("address"));
                        storeBean.setLicence(dataset.getString("license"));
                        storeBean.setKeeper(dataset.getString("storekeeper"));
                        storeBean.setPhone(dataset.getString("phone"));
                        String pictrues = dataset.getString("storePictrue");
                        if(dataset.has("logo")){
                            storeBean.setIconUrl(dataset.getString("logo"));
                        }
                        if(dataset.has("banana")){
                            sp.put("sp","true");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(getContext(), getActivity(), "您已创建过店铺");
                                    getContext().sendBroadcast(new Intent("toSP"));
                                }
                            });

                            return;
                        }

                        JSONArray storePictrues = new JSONArray(pictrues);
                        List<String> urls = new ArrayList<>();
                        for (int i = 0; i < storePictrues.length(); i++) {
                            urls.add(storePictrues.getString(i));
                        }
                        storeBean.setMainUrls(urls);

                        int status = dataset.getInt("status");
                        if (status == 0) {//待审核
                            intent.putExtra("statu", "wait");
                            intent.putExtra("store", storeBean);
                            getContext().startActivity(intent);
                        } else if (status == 1) {//审核通过
                            intent.putExtra("statu", "ok");
                            intent.putExtra("store", storeBean);
                            getContext().startActivity(intent);
                        } else if (status == 2) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "你的店铺已被禁用，如有疑问请想平台反馈！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (string.contains("1008")) {
                    getContext().startActivity(new Intent(getContext(), ApplyStoreActivity.class));
                }
            }
        });
    }

    @OnClick(R.id.rl_post_demand)
    public void postDemand() {
        getContext().startActivity(new Intent(getContext(), PublishDemandActivity.class));
    }

    @OnClick(R.id.rl_my_demand)
    public void myDemand() {
        getContext().startActivity(new Intent(getContext(), MyDemandActivity.class));
    }

    @OnClick(R.id.rl_shoppingcar)
    public void shoppingCar() {
        getContext().startActivity(new Intent(getContext(), ShoppingCarActivity.class));
    }


    @OnClick(R.id.rl_balance)
    public void balance() {
        Intent intent = new Intent(getContext(), TheBalanceOfUserActivity.class);
        intent.putExtra("balance", balance);
        getContext().startActivity(intent);
    }

    /**
     * 跳转到订单界面
     * @param from
     */
    public void jump2Orders(String from) {
        Intent intent = new Intent(getContext(), MyOrdersActivity.class);
        intent.putExtra("from", from);
        getContext().startActivity(intent);
    }


}
