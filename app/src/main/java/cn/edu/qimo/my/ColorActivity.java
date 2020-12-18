package cn.edu.qimo.my;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import cn.edu.qimo.R;

public class ColorActivity extends AppCompatActivity {
    RelativeLayout mainrelativeLayout1,mainrelativeLayout2;
    ImageButton mingxiBt,myBt,jizhangBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        InitView();
    }

    private void InitView() {
        mainrelativeLayout1 = findViewById(R.id.main_top_layout);
        mainrelativeLayout2 = findViewById(R.id.item_relativelayout);
        mingxiBt = findViewById(R.id.mingxi);
        myBt = findViewById(R.id.main_btn_my);
        jizhangBt = findViewById(R.id.main_btn_edit);


    }


    public void onClick(View view) {
        switch (view.getId()){
            case R.id.color_btn_yellow:
                SettingColor1();
                break;
            case R.id.color_btn_pink:
                Log.i("pink","点击成功");
                SettingColor2();
                break;
        }
    }

    private void SettingColor1() {
        mainrelativeLayout1.setBackgroundColor(getResources().getColor(R.color.yellow));
        mainrelativeLayout2.setBackgroundColor(getResources().getColor(R.color.yellow2));
    }
    private void SettingColor2() {
        mingxiBt.setBackgroundColor(getResources().getColor(R.color.pink));
        myBt.setBackgroundColor(getResources().getColor(R.color.pink));
        jizhangBt.setBackgroundColor(getResources().getColor(R.color.pink));
//        mainrelativeLayout1.setBackgroundColor(getResources().getColor(R.color.pink));
//        mainrelativeLayout2.setBackgroundColor(getResources().getColor(R.color.pink2));
    }


}