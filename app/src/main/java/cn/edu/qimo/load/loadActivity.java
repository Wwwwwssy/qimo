package cn.edu.qimo.load;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;


import androidx.appcompat.app.AppCompatActivity;


import cn.edu.qimo.MainActivity;
import cn.edu.qimo.R;

public class loadActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            // 跳转到主界面
            case R.id.login_button:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
}



