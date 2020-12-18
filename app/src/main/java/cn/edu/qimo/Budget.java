package cn.edu.qimo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class Budget extends Dialog implements View.OnClickListener {
    Button cancelBtn,ensureBtn;
    EditText moneyEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingbudget);
        cancelBtn = findViewById(R.id.budget_btn_cancel);
        ensureBtn = findViewById(R.id.budget_btn_ensure);
        moneyEt = findViewById(R.id.budget_et);
        cancelBtn.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }
    public  interface OnEnsureListener{
        public void onEnsure(float money);
    }
    OnEnsureListener onEnsureListener;
    //接口回调，在主活动中点击预算进行调用
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public Budget(@NonNull Context context) {
        super(context);
    }

    public Budget(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected Budget(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.budget_btn_cancel:
                cancel();//取消对话框
                break;
            case R .id.budget_btn_ensure:
                String data = moneyEt.getText().toString();//得到输入的数值
                if (TextUtils.isEmpty(data)) {
                    Toast.makeText(getContext(),"输入数据不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                float money = Float.parseFloat(data);
                if (money<=0) {
                    Toast.makeText(getContext(),"预算金额必须大于0",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure(money);
                }
                cancel();
                break;
        }
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
