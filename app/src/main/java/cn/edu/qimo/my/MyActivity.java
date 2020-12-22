package cn.edu.qimo.my;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.qimo.MainActivity;
import cn.edu.qimo.R;

import static java.security.AccessController.getContext;

public class MyActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView hBack;
    ImageView hHead;
    ImageView userLine;
    TextView userName;
    TextView userVal;
    nav_bar history;
    nav_bar shoucang;
    nav_bar version;
    nav_bar about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        initView();
    }
    //初始化自带的View的方法
    private void initView() {
        hBack = findViewById(R.id.h_back);
        hHead= findViewById(R.id.h_head);
        userLine = findViewById(R.id.user_line);
        userName = findViewById(R.id.user_name);
        userVal = findViewById(R.id.user_val);
//        history = findViewById(R.id.history);
//        about = findViewById(R.id.about);

        hBack.setOnClickListener(this);
        hHead.setOnClickListener(this);
        userName.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_history:
                Log.i("history","点击历史成功");
                Intent intent = new Intent(this,HistoryActivity.class);//历史界面
                startActivity(intent);
                break;
            case R.id.my_shezhi:
                Intent intent2 = new Intent(this,SettingActivity.class);//历史界面
                startActivity(intent2);
                break;
            case R.id.my_zhangdan:
                Intent intent3 = new Intent(this,ZhangdanActivity.class);//年账单界面
                startActivity(intent3);
                break;
//            case R.id.about:
//                Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(this, AboutActivity.class);//关于页面（主页面）
//                startActivity(intent);
//                break;

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}