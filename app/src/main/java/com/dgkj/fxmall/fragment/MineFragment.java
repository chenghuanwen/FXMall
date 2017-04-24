package com.dgkj.fxmall.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.bumptech.glide.Glide;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.constans.FXConst;
import com.dgkj.fxmall.utils.LogUtil;
import com.dgkj.fxmall.utils.SharedPreferencesUnit;
import com.dgkj.fxmall.view.ApplyStoreActivity;
import com.dgkj.fxmall.view.HomePageActivity;
import com.dgkj.fxmall.view.MessageCenterActivity;
import com.dgkj.fxmall.view.MyDemandActivity;
import com.dgkj.fxmall.view.MyOrdersActivity;
import com.dgkj.fxmall.view.PublishDemandActivity;
import com.dgkj.fxmall.view.RechargeActivity;
import com.dgkj.fxmall.view.RefundActivity;
import com.dgkj.fxmall.view.SettingActivity;
import com.dgkj.fxmall.view.ShoppingCarActivity;
import com.dgkj.fxmall.view.TheBalanceOfUserActivity;
import com.dgkj.fxmall.view.UserMsgActivity;
import com.dgkj.fxmall.view.WithdrawalActivity;
import com.dgkj.fxmall.view.myView.ShareInvitaCodeDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    private String balance;
    private String iconUrl;
    private String invitationCode;
    private String phone;
    private String gender;
    private String nickname;
    private String realname;
    private String location;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mine, container, false);
        ButterKnife.bind(this, view);

        getUserInfo();

       // setData();
        refresh2Home();
        return view;
    }

    private void getUserInfo() {
        sp = SharedPreferencesUnit.getInstance(getContext());
        client = new OkHttpClient.Builder().build();
        FormBody body = new FormBody.Builder()
                .add("token",sp.get("token"))
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
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject info = object.getJSONObject("dataset");
                    balance = info.getString("balance");
                    iconUrl = info.getString("headPortrait");
                    invitationCode = info.getString("invitationCode");
                    phone = info.getString("phone");
                    gender = info.getInt("sex")+"";
                    nickname = info.getString("nickname");
                    realname = info.getString("realname");
                    location = info.getString("location");
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
                LogUtil.i("TAG","scrollview距离顶部距离=="+top);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = x;
                        downY = y;
                        LogUtil.i("TAG", "按下时坐标== x=" + downX + "==y==" + downY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = x - downX;
                        float dy = y - downY;
                        LogUtil.i("TAG", "滑动距离 x==" + dx + "y===" + dy);
                        if (dy > 0 && Math.abs(dy) > Math.abs(dx) && dy > 300) {
                            LogUtil.i("TAG", "向下滑动======");
                          /*  layoutParams.height = 200;
                            tvJumpTip.setLayoutParams(layoutParams);*/
                            tvJumpTip.setVisibility(View.VISIBLE);

                        }/* else if(dy>0 && Math.abs(dy)>Math.abs(dx) && (dy>200 && dy<300)) {
                            LogUtil.i("TAG", "向上滑动======");
                            layoutParams.height = 100;
                            tvJumpTip.setLayoutParams(layoutParams);
                            tvJumpTip.setVisibility(View.VISIBLE);
                        }*/else {
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
                startActivity(new Intent(getContext(), HomePageActivity.class));
            }
        });
    }

    private void setData() {
        //TODO 设置邀请码和账户余额
        Glide.with(this).load(iconUrl).into(civMyIcon);
        tvUsername.setText(nickname);
        tvInviteCode.setText(invitationCode);
        tvAccountRest.setText(balance);

    }

    @OnClick(R.id.civ_my_icon)
    public void selectIcon() {
        Intent intent = new Intent(getContext(), UserMsgActivity.class);
        intent.putExtra("icon",iconUrl);
        intent.putExtra("nick",nickname);
        intent.putExtra("gender",gender);
        intent.putExtra("realname",realname);
        intent.putExtra("phone",phone);
        intent.putExtra("address",location);
        getContext().startActivity(intent);
    }

    @OnClick(R.id.iv_setting)
    public void setting() {
        getContext().startActivity(new Intent(getContext(), SettingActivity.class));
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
        getContext().startActivity(new Intent(getContext(), RechargeActivity.class));
    }

    @OnClick(R.id.rl_withdrawal)
    public void withdawal() {
        getContext().startActivity(new Intent(getContext(), WithdrawalActivity.class));
    }

    @OnClick(R.id.rl_apply_store)
    public void applayStore() {
        getContext().startActivity(new Intent(getContext(), ApplyStoreActivity.class));
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
        getContext().startActivity(new Intent(getContext(), TheBalanceOfUserActivity.class));
    }

    /**
     * 跳转到订单界面
     *
     * @param from
     */
    public void jump2Orders(String from) {
        Intent intent = new Intent(getContext(), MyOrdersActivity.class);
        intent.putExtra("from", from);
        getContext().startActivity(intent);
    }


}