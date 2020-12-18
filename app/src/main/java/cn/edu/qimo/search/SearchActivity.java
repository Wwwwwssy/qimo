package cn.edu.qimo.search;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.qimo.MainJizhangAdapter;
import cn.edu.qimo.R;
import cn.edu.qimo.db.ConfirmBean;
import cn.edu.qimo.db.DBManager;

public class SearchActivity extends AppCompatActivity {
    ListView searchLv;
    EditText searchEt;
    TextView emptyTv;
    List<ConfirmBean> mDatas;   //数据源
    MainJizhangAdapter adapter;    //适配器对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        mDatas = new ArrayList<>();
        adapter = new MainJizhangAdapter(this,mDatas);
        searchLv.setAdapter(adapter);
    }

    private void initView() {
        searchEt = findViewById(R.id.search_et);
        searchLv = findViewById(R.id.search_lv);
        emptyTv = findViewById(R.id.search_tv_empty);
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_sh:   //执行搜索的操作
                String msg = searchEt.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {//判断输入内容是否为空，如果为空，就提示不能搜索
                    Toast.makeText(this,"输入内容不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                //开始搜索
                List<ConfirmBean> list = DBManager.getListFromConfirmtbBybeizhu(msg);//根据备注搜索收入或者支出的情况列表
                List<ConfirmBean> list2 = DBManager.getListFromConfirmtbBytime(msg);//根据时间搜索收入或者支出的情况列表
                List<ConfirmBean> list3 = DBManager.getListFromConfirmtbBytypename(msg);//根据类型搜索收入或者支出的情况列表
                mDatas.clear();
                mDatas.addAll(list);
                mDatas.addAll(list2);
                mDatas.addAll(list3);
                adapter.notifyDataSetChanged();
                break;
        }
    }
}