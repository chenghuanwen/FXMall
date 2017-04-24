package com.dgkj.fxmall.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.view.ApplyStoreActivity;
import com.dgkj.fxmall.view.RechargeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_joinus_fragment, container, false);
        ButterKnife.bind(this, view);
        joinOption();
        return view;
    }

    private void joinOption() {
        rgProduct.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.rb_join_store){
                    underline1.setVisibility(View.VISIBLE);
                    underline2.setVisibility(View.GONE);
                    startActivity(new Intent(getContext(), ApplyStoreActivity.class));
                }else {
                    underline1.setVisibility(View.GONE);
                    underline2.setVisibility(View.VISIBLE);
                    startActivity(new Intent(getContext(), RechargeActivity.class));
                }
            }
        });
    }
}
