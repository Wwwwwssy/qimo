package cn.edu.qimo.my;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import cn.edu.qimo.MainJizhangAdapter;
import cn.edu.qimo.R;
import cn.edu.qimo.db.ConfirmBean;
import cn.edu.qimo.db.DBManager;

public class HistoryActivity extends AppCompatActivity {
    ListView historyLv;
    EditText searchEt;
    TextView timeTv;
    List<ConfirmBean> mDatas;//数据源还是那个
    MainJizhangAdapter adapter;//适配器还是一天收支的适配器
    int year,month,day;
    ConfirmBean confirmBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
        mDatas = new ArrayList<>();
        adapter = new MainJizhangAdapter(this,mDatas);
        historyLv.setAdapter(adapter);
        Time();
        timeTv.setText(year+"年"+month+"月"+day+"日");
        loadDBData();

    }
    // 加载数据库数据
    private void loadDBData() {
        List<ConfirmBean> list = DBManager.getListOneDayFromConfirmtb(year, month, day);
//        List<ConfirmBean> list2 = DBManager.getListOneMonthFromConfirmtb(year, month);
        mDatas.clear();
        mDatas.addAll(list);
//        mDatas.addAll(list2);
        adapter.notifyDataSetChanged();
    }


    private void Time() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void initView() {
        historyLv = findViewById(R.id.history_lv);
        timeTv = findViewById(R.id.history_tv_time);
    }



    public void onClick(View view) {
        switch (view.getId()){
            case R.id.history_iv_back:
                finish();
                break;
            case R.id.search_iv_sh:
                Log.i("history","历史记录被点击了");
                Search();
                break;

        }
    }

    private void Search() {
        String msg = searchEt.getText().toString().trim();
        timeTv.setText(msg);
        if (TextUtils.isEmpty(msg)) {//判断输入内容是否为空，如果为空，就提示不能搜索
            Toast.makeText(this,"输入内容不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        //开始搜索
        List<ConfirmBean> list2 = DBManager.getListFromConfirmtbBytime(msg);//根据时间搜索收入或者支出的情况列表
        mDatas.clear();
        mDatas.addAll(list2);
        adapter.notifyDataSetChanged();
    }

}