package com.dgkj.fxmall.view;

import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.dgkj.fxmall.MyApplication;
import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.fragment.MineFragment;
import com.dgkj.fxmall.fragment.ShangpuFragment;
import com.dgkj.fxmall.fragment.YeWuYuanFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.rl_display)
    RelativeLayout rlDisplay;
    @BindView(R.id.rb_shangpu)
    AppCompatRadioButton rbShangpu;
    @BindView(R.id.rb_yewuyuan)
    AppCompatRadioButton rbYewuyuan;
    @BindView(R.id.rb_mine)
    AppCompatRadioButton rbMine;
    @BindView(R.id.rg_footer)
    RadioGroup rgFooter;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private ShangpuFragment shangpuFragment;
    private YeWuYuanFragment yeWuYuanFragment;
    private MineFragment mineFragment;
    private ClipboardManager clipboardManager;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();
        userId = sp.get("userId");

        initDisplay();
        initFooter();

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    @Override
    public View getContentView() {
        return activityMain;
    }


    private void initDisplay() {
        String from = getIntent().getStringExtra("from");
        String login = sp.get("login");
        if (!"true".equals(login)) {
            jumpTo(LoginActivity.class, true);
            return;
        }
        switch (from) {
            case "sp":
                if(userId.equals("3")){
                    rbShangpu.setChecked(true);
                    showSP();
                }else {
                    Intent intent = new Intent(this,HomePageActivity.class);
                    intent.putExtra("from","sp");
                    jumpTo(intent,true);
                }
                break;
            case "yw":
                if(userId.equals("2")){
                    rbYewuyuan.setChecked(true);
                    showYW();
                }else {
                    Intent intent = new Intent(this,HomePageActivity.class);
                    intent.putExtra("from","yw");
                    jumpTo(intent,true);
                }
                break;
            case "mine":
                rbMine.setChecked(true);
                showMine();
                break;
        }

    }

    private void initFooter() {
        rgFooter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_shangpu:
                        showSP();
                        break;
                    case R.id.rb_yewuyuan:
                        showYW();
                        break;
                    case R.id.rb_mine:
                        if (MyApplication.isLogin) {
                            showMine();
                        } else {
                            jumpTo(LoginActivity.class, false);
                        }
                        break;
                }
            }
        });
    }


    private void showSP() {
        fragmentTransaction = fragmentManager.beginTransaction();
        if (shangpuFragment == null) {
            shangpuFragment = new ShangpuFragment();
            fragmentTransaction.add(R.id.rl_display, shangpuFragment);
        }
        fragmentTransaction.commit();
        hideFragment(fragmentTransaction);
        fragmentTransaction.show(shangpuFragment);
    }

    private void showYW() {
        fragmentTransaction = fragmentManager.beginTransaction();
        if (yeWuYuanFragment == null) {
            yeWuYuanFragment = new YeWuYuanFragment();
            fragmentTransaction.add(R.id.rl_display, yeWuYuanFragment);
        }
        fragmentTransaction.commit();
        hideFragment(fragmentTransaction);
        fragmentTransaction.show(yeWuYuanFragment);
    }

    private void showMine() {
        fragmentTransaction = fragmentManager.beginTransaction();
        if (mineFragment == null) {
            mineFragment = new MineFragment();
            fragmentTransaction.add(R.id.rl_display, mineFragment);
        }
        fragmentTransaction.commit();
        hideFragment(fragmentTransaction);
        fragmentTransaction.show(mineFragment);
    }

    /**
     * 隐藏所有Fragment
     *
     * @param fragmentTransaction
     */
    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (shangpuFragment != null) {
            fragmentTransaction.hide(shangpuFragment);
        }
        if (yeWuYuanFragment != null) {
            fragmentTransaction.hide(yeWuYuanFragment);
        }
        if (mineFragment != null) {
            fragmentTransaction.hide(mineFragment);
        }
        if (mineFragment != null) {
            fragmentTransaction.hide(mineFragment);
        }

    }
}
