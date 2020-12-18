package cn.edu.qimo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;


public class Beizhu extends Dialog implements View.OnClickListener{
    EditText editText1;
    Button cancelBtn,ensureBtn;
    OnEnsureListener onEnsureListener;//初始化接口对象
    // 设定回调接口的方法(支出和收入引用)
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }
    public Beizhu(@NonNull Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jizhang_beizhu);//设置对话框显示布局
        editText1 = findViewById(R.id.jizhang_beizhu_et);
        cancelBtn = findViewById(R.id.jizhang_beizhu_btn_cancel);
        ensureBtn = findViewById(R.id.jizhang_beizhu_btn_ensure);
        cancelBtn.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    public String getEditText() {
        return editText1.getText().toString().trim();
    }
    //获取输入数据的方法
    public interface OnEnsureListener{
        public void onEnsure();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jizhang_beizhu_btn_cancel:
                cancel();
                break;
            case R.id.jizhang_beizhu_btn_ensure:
                if (onEnsureListener!=null) {//传入接口对象了
                    onEnsureListener.onEnsure();
                }
                break;
        }
    }

}
