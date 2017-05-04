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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.view.CancelYeWuYuanActivity;
import com.dgkj.fxmall.view.HomePageActivity;
import com.dgkj.fxmall.view.MyVIPActivity;
import com.dgkj.fxmall.view.RechargeActivity;
import com.dgkj.fxmall.view.WithdrawalActivity;
import com.dgkj.fxmall.view.myView.ShareInvitaCodeDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YeWuYuanFragment extends Fragment {

    @BindView(R.id.tv_my_vip_count)
    TextView tvMyVipCount;
    @BindView(R.id.tv_invite)
    TextView tvInvite;
    @BindView(R.id.tv_today_commission)
    TextView tvTodayCommission;
    @BindView(R.id.tv_month_commission)
    TextView tvMonthCommission;
    @BindView(R.id.tv_recharge)
    LinearLayout tvRecharge;
    @BindView(R.id.tv_withDrawal)
    LinearLayout tvWithDrawal;
    @BindView(R.id.scrollview)
    ScrollView scrollView;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.tv_jump_tip)
    TextView tvJumpTip;
    @BindView(R.id.tv_vip)
    TextView tvVip;
    private float downX, downY = 300;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ye_wu_yuan, container, false);
        ButterKnife.bind(this, view);
        initData();
        refresh2Home();
        return view;
    }

    /**
     * 初始化业务员的各基础信息
     */
    private void initData() {

    }

    @OnClick(R.id.tv_recharge)
    public void recharge() {
        Intent intent = new Intent(getContext(), RechargeActivity.class);
        intent.putExtra("", "");//TODO 传入当前用户信息
        getContext().startActivity(intent);
    }

    @OnClick(R.id.tv_withDrawal)
    public void withdrawal() {
        Intent intent = new Intent(getContext(), WithdrawalActivity.class);
        intent.putExtra("rest", "100");//TODO 传入当前用户信息
        getContext().startActivity(intent);
    }

    @OnClick(R.id.tv_invite)
    public void invita() {
        ShareInvitaCodeDialog dialog = new ShareInvitaCodeDialog("FENXIOAMENG1236213");
        dialog.show(getFragmentManager(), "");
    }

    @OnClick({R.id.tv_vip,R.id.tv_my_vip_count})
    public void toVipDetial(){
        getActivity().startActivity(new Intent(getContext(), MyVIPActivity.class));
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
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = x;
                        downY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = x - downX;
                        float dy = y - downY;
                        if (dy > 0 && Math.abs(dy) > Math.abs(dx) && dy > 300) {
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
                startActivity(new Intent(getContext(), HomePageActivity.class));
            }
        });
    }

}
