package com.dgkj.fxmall.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.base.BaseActivity;
import com.dgkj.fxmall.constans.FXConst;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class SearchActivity extends BaseActivity {

    @BindView(R.id.tv_search_type)
    TextView tvSearchType;
    @BindView(R.id.et_search_content)
    EditText etSearchContent;
    @BindView(R.id.tv_change_batch)
    TextView tvChangeBatch;
    @BindView(R.id.rv_hot_search)
    RecyclerView rvHotSearch;
    @BindView(R.id.activity_search)
    LinearLayout activitySearch;
    @BindView(R.id.tv_cancle_search)
    TextView tvCancleSearch;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    private PopupWindow pw;
    private List<String> hotWords;
    private CommonAdapter<String> adapter;
    private int searchType;
    private int index = 1;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initHotWords();
        search();
        getHotWords(index, searchType);

    }

    @Override
    public View getContentView() {
        return activitySearch;
    }

    private void getHotWords(int index, int searchType) {
        final List<String> words = new ArrayList<>();
        FormBody body = new FormBody.Builder()
                .add("type", searchType + "")
                .add("index", index + "")
                .add("size", 10 + "")
                .build();
        final Request request = new Request.Builder()
                .post(body)
                .url(FXConst.GET_HOT_SEARCH_KEY)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.contains("1000")) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray dataset = object.getJSONArray("dataset");
                        for (int i = 0; i < dataset.length(); i++) {
                            JSONObject jsonObject = dataset.getJSONObject(i);
                            words.add(jsonObject.getString("word"));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addAll(words, true);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void initHotWords() {
        client = new OkHttpClient.Builder().build();

        hotWords = new ArrayList<>();
        adapter = new CommonAdapter<String>(this, R.layout.item_search_hot_key, hotWords) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv_hotword, s);
            }
        };
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        rvHotSearch.setLayoutManager(layoutManager);
        rvHotSearch.setAdapter(adapter);
    }


    public void search() {
        etSearchContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER && etSearchContent.getText().toString() != null) {
                    Intent intent = new Intent(SearchActivity.this, SearchContentActivity.class);
                    intent.putExtra("type", tvSearchType.getText().toString());
                    intent.putExtra("key", etSearchContent.getText().toString());
                    intent.putExtra("from","search");
                    jumpTo(intent, false);
                }
                return false;
            }
        });

    }

    @OnClick(R.id.tv_search_type)
    public void searchType() {
        View contentview = getLayoutInflater().inflate(R.layout.layout_searchtype_select_window, null);
        pw = new PopupWindow(contentview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView tvGirl = (TextView) contentview.findViewById(R.id.tv_search_goods);
        tvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.this.tvSearchType.setText("商品");
                searchType = 0;
                pw.dismiss();
            }
        });
        TextView tvBoy = (TextView) contentview.findViewById(R.id.tv_search_store);
        tvBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.this.tvSearchType.setText("商铺");
                searchType = 1;
                pw.dismiss();
            }
        });


        //设置触摸对话框以外区域，对话框消失
        pw.setFocusable(true);
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#00000000"));
        pw.setBackgroundDrawable(cd);
        pw.showAsDropDown(tvSearchType);
    }

    @OnClick(R.id.tv_change_batch)
    public void changeBatch() {
        index++;
        getHotWords(index, searchType);
    }

    @OnClick(R.id.tv_cancle_search)
    public void cancle() {
        finish();
    }

    @OnClick(R.id.ib_back)
    public void back(){
        finish();
    }
}
