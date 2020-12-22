package cn.edu.qimo.my;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.edu.qimo.MainJizhangAdapter;
import cn.edu.qimo.R;
import cn.edu.qimo.db.ConfirmBean;
import cn.edu.qimo.db.DBManager;

public class ZhangdanActivity extends AppCompatActivity {
    ImageView backIv;
    TextView moneyTv;
    ListView zhangdanLv;
    List<ConfirmBean> mDatas;   //数据源
    MainJizhangAdapter adapter;    //适配器对象
    int year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhangdan);
        Time();
        initView();
        Showmoney();
//        loadDBData();
        mDatas = new ArrayList<>();
        adapter = new MainJizhangAdapter(this,mDatas);
        zhangdanLv.setAdapter(adapter);

    }
    // 加载数据库数据
    private void loadDBData() {
        List<ConfirmBean> list = DBManager.getListOneYearFromConfirmtb(year);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }
    private void Time() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
    }
    private void Showmoney() {
        float income = DBManager.getSumMoneyOneYear(year, 1);
        float outcome = DBManager.getSumMoneyOneYear(year,0);
        String infoOneDay = "今年支出 ￥"+outcome+"  收入 ￥"+income;
        moneyTv.setText(infoOneDay);
    }

    private void initView() {
        backIv = findViewById(R.id.zhangdan_iv_back);
        moneyTv = findViewById(R.id.zhangdan_tv_money);
        zhangdanLv = findViewById(R.id.zhangdan_lv);
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.zhangdan_iv_back:
                finish();
                break;
            case R.id.zhangdan_bt:
                loadDBData();
                break;
            case R.id.zhangdan_bt1:
                loadDBData();
                break;
        }
    }



}