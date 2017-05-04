package com.dgkj.fxmall.view;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import com.dgkj.fxmall.view.myView.ShareCommandDialog;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();
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
        switch (from) {
            case "sp":
                rbShangpu.setChecked(true);
                showSP();
                break;
            case "yw":
                rbYewuyuan.setChecked(true);
                showYW();
                break;
            case "mine":
                rbMine.setChecked(true);
                String login = sp.get("login");
                if ("true".equals(login)) {
                    showMine();
                } else {
                    jumpTo(LoginActivity.class, true);
                }
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
