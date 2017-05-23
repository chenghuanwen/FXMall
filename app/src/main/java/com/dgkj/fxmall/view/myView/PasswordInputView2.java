package com.dgkj.fxmall.view.myView;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dgkj.fxmall.R;
import com.dgkj.fxmall.listener.InputCompletetListener;

/**
 * Created by Android004 on 2017/4/6.
 */

public class PasswordInputView2 extends RelativeLayout {
    private EditText editText;
    private StringBuffer stringBuffer = new StringBuffer();
    private String inputContent;
    private int count = 6;
    private TextView[] textViews = new TextView[6];
    private InputCompletetListener inputCompletetListener;
    public PasswordInputView2(Context context) {
        this(context,null);
    }

    public PasswordInputView2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PasswordInputView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.layout_password_input,this);
        editText = (EditText) findViewById(R.id.et_setpassword);
        textViews[0] = (TextView) findViewById(R.id.tv_set_number1);
        textViews[1] = (TextView) findViewById(R.id.tv_set_number2);
        textViews[2] = (TextView) findViewById(R.id.tv_set_number3);
        textViews[3] = (TextView) findViewById(R.id.tv_set_number4);
        textViews[4] = (TextView) findViewById(R.id.tv_set_number5);
        textViews[5] = (TextView) findViewById(R.id.tv_set_number6);
        setListener();
    }

    public void setInputCompletetListener(InputCompletetListener inputCompletetListener) {
        this.inputCompletetListener = inputCompletetListener;
    }

    public String getEditContent(){
        return inputContent;
    }


    public void clearEditContent(){
        stringBuffer.delete(0,stringBuffer.length());
        inputContent = stringBuffer.toString();
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setText("");
           // textViews[i].setBackground();
        }
    }
    public void setListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")){
                   if(stringBuffer.length()>5){
                       editText.setText("");
                       return;
                   }else {
                       stringBuffer.append(s);
                       editText.setText("");
                       count = stringBuffer.length();
                       inputContent = stringBuffer.toString();
                       if(stringBuffer.length()==6){
                           if(inputCompletetListener != null){
                               inputCompletetListener.inputComplete();
                           }
                       }
                   }
                    for (int i = 0; i < stringBuffer.length(); i++) {
                        textViews[i].setText(String.valueOf(stringBuffer.charAt(i)));
                       // textViews[i].setBackground();
                    }
                }
            }
        });

        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_DEL && event.getAction()==KeyEvent.ACTION_DOWN){//按下键盘删除键
                    if(onkeyDelete()) return true;
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 删除
     * @return
     */
    public boolean onkeyDelete(){
        if(count==0){
            count = 6;
            return true;
        }
        if(stringBuffer.length()>0){
            stringBuffer.delete(count-1,count);
            count--;
            inputContent = stringBuffer.toString();
            textViews[stringBuffer.length()].setText("");
            //textViews[stringBuffer.length()].setBackground();
            if(inputCompletetListener != null){
                inputCompletetListener.deleteContent(true);
            }
        }

        return false;
    }
}
