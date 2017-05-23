package com.dgkj.fxmall.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.view.ApplyStoreActivity;
import com.dgkj.fxmall.view.RechargeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("ValidFragment")
public class JoinUsFragment extends Fragment {

    @BindView(R.id.rb_join_store)
    RadioButton rbJoinStore;
    @BindView(R.id.rb_join_yewuyuan)
    RadioButton rbJoinYewuyuan;
    @BindView(R.id.rg_product)
    RadioGroup rgProduct;
    @BindView(R.id.underline1)
    View underline1;
    @BindView(R.id.underline2)
    View underline2;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.ll_joinSP)
    LinearLayout llJoinSP;
    @BindView(R.id.ll_joinYWY)
    LinearLayout llJoinYWY;
    private String from = "";

    public JoinUsFragment() {
    }

    public JoinUsFragment(String from) {
        this.from = from;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_joinus_fragment, container, false);
        ButterKnife.bind(this, view);
        joinOption();
        if ("sp".equals(from)) {
            rbJoinStore.setChecked(true);
        } else {
            rbJoinYewuyuan.setChecked(true);
        }
        return view;
    }

    private void joinOption() {
        rgProduct.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_join_store) {
                    underline1.setVisibility(View.VISIBLE);
                    underline2.setVisibility(View.GONE);
                    llJoinSP.setVisibility(View.VISIBLE);
                    llJoinYWY.setVisibility(View.GONE);
                } else {
                    underline1.setVisibility(View.GONE);
                    underline2.setVisibility(View.VISIBLE);
                    llJoinSP.setVisibility(View.GONE);
                    llJoinYWY.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        if ("sp".equals(from) || rbJoinStore.isChecked()) {
            startActivity(new Intent(getContext(), ApplyStoreActivity.class));
        } else {
            Intent intent = new Intent(getContext(), RechargeActivity.class);
            intent.putExtra("from","ywy");
            startActivity(intent);
        }
    }
}
