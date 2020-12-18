package cn.edu.qimo.my;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import cn.edu.qimo.R;
import androidx.annotation.NonNull;


public class Rili extends Dialog implements View.OnClickListener {
    EditText hourEt,minuteEt;
    DatePicker datePicker;
    Button ensureBtn,cancelBtn;



    public interface OnEnsureListener{
        public void onEnsure(String time, int year, int month, int day);
    }
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_rili);
        hourEt = findViewById(R.id.dialog_time_et_hour);
        minuteEt = findViewById(R.id.dialog_time_et_minute);
        datePicker = findViewById(R.id.rili_time_dp);
        ensureBtn = findViewById(R.id.rili_time_btn_ensure);
        cancelBtn = findViewById(R.id.rili_time_btn_cancel);
        ensureBtn.setOnClickListener(this);  //添加点击监听事件
        cancelBtn.setOnClickListener(this);

    }
    public Rili(@NonNull Context context) {
        super(context);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rili_time_btn_cancel:
                cancel();
                break;
            case R.id.rili_time_btn_ensure:
                int year = datePicker.getYear();  //选择年份
                int month = datePicker.getMonth()+1;
                int dayOfMonth = datePicker.getDayOfMonth();
                String monthStr = String.valueOf(month);
                if (month<10){
                    monthStr = "0"+month;
                }
                String dayStr = String.valueOf(dayOfMonth);
                if (dayOfMonth<10){
                    dayStr="0"+dayOfMonth;
                }
//              获取输入的小时和分钟
                String hourStr = hourEt.getText().toString();
                String minuteStr = minuteEt.getText().toString();
                int hour = 0;
                if (!TextUtils.isEmpty(hourStr)) {
                    hour = Integer.parseInt(hourStr);
                    hour=hour%24;
                }
                int minute = 0;
                if (!TextUtils.isEmpty(minuteStr)) {
                    minute = Integer.parseInt(minuteStr);
                    minute=minute%60;
                }

                hourStr=String.valueOf(hour);
                minuteStr=String.valueOf(minute);
                if (hour<10){
                    hourStr="0"+hour;
                }
                if (minute<10){
                    minuteStr="0"+minute;
                }
                String timeFormat = year+"年"+monthStr+"月"+dayStr+"日 "+hourStr+":"+minuteStr;
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure(timeFormat,year,month,dayOfMonth);
                }
                cancel();
                break;
        }
    }
}
