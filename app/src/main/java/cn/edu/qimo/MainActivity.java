package cn.edu.qimo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.edu.qimo.db.ConfirmBean;
import cn.edu.qimo.db.DBManager;
import cn.edu.qimo.jizhangfg.JizhangActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "JizhangActivity";
    ListView todayLv;  //展示今日收支情况的ListView
    ImageView searchIv;//搜索
    ImageButton editBtn,moreBtn;//个人中心
    List<ConfirmBean> mDatas;//声明数据源
    MainJizhangAdapter adapter;//声明适配器
    int year,month,day;
    //头布局相关控件
    View headerView;
    TextView topOutTv,topInTv,topbudgetTv,topConTv;
    ImageView topShowIv;
    //
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Time();
        initView();
        mDatas = new ArrayList<>();
        //设置适配器，加载每一行数据到ListView
        adapter = new MainJizhangAdapter(this,mDatas);
        todayLv.setAdapter(adapter);
        //添加listview的头布局
        addLVHeaderView();

    }
    // 当activity获取焦点时，会调用的方法
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }
    // 设置头布局当中文本内容的显示
    private void setTopTvShow() {
        //获取今日支出和收入总金额，显示在view当中
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "今日支出 ￥"+outcomeOneDay+"  收入 ￥"+incomeOneDay;
        topConTv.setText(infoOneDay);
        //获取本月收入和支出总金额
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        topInTv.setText("￥"+incomeOneMonth);
        topOutTv.setText("￥"+outcomeOneMonth);

        //设置显示运算剩余
        float bmoney = preferences.getFloat("bmoney", 0);//预算
        if (bmoney == 0) {
            topbudgetTv.setText("￥ 0");
        }else{
            float syMoney = bmoney-outcomeOneMonth;
            topbudgetTv.setText("￥"+syMoney);
        }
    }

    // 加载数据库数据
    private void loadDBData() {
        List<ConfirmBean> list = DBManager.getListOneDayFromComfirmtb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    //给ListView添加头布局的方法
    private void addLVHeaderView() {
        //将布局转换成View对象
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);//设置头布局
        //查找头布局可用控件
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        topbudgetTv.setOnClickListener( this);//预算点击事件
        headerView.setOnClickListener(this);//头布局整体点击事件，点击后显示图表
        topShowIv.setOnClickListener(this);//隐藏点击事件

    }

    //初始化自带的View的方法
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_my);
        searchIv = findViewById(R.id.main_iv_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
//        setLVLongClickListener();
    }
    private void Time() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }


    public void onClick(View v){
        switch (v.getId()) {
            case R.id.mingxi:
                Intent intent = new Intent(this,MainActivity.class);//明细页面（主页面）
                startActivity(intent);
                break;
            case R.id.main_btn_edit:
                Log.d(TAG,"记账");
                Intent intent2 = new Intent(this, JizhangActivity.class);  //记账界面
                startActivity(intent2);
                break;
            case R.id.item_mainlv_top_tv_budget:

                break;
            case R.id.item_mainlv_top_iv_hide:
                toggleShow();//隐藏或者显示
                break;
//            case R.id.main_btn_my:
//                Intent intent4 = new Intent(this, MyActivity.class);  //个人主页
//                startActivity(intent4);
//                break;
//            case R.id.main_btn_more:
//                MoreDialog moreDialog = new MoreDialog(this);//更多页面
//                moreDialog.show();
//                moreDialog.setDialogSize();
//                break;
//            case R.id.main_iv_search:
//                Intent intent3 = new Intent(this, SearchActivity.class);  //搜索界面
//                startActivity(intent3);
//                break;
//            if (v == headerView) {
//                //头布局被点击了
//                Intent intent5 = new Intent();
//                intent.setClass(this, MonthChartActivity.class);//图表页面
//                startActivity(intent5);
//            }
        }
    }

    //隐藏或者显示金额
    boolean isShow = true;
    private void toggleShow() {
        if (isShow) {   //明文====》密文
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(passwordMethod);   //设置隐藏
            topOutTv.setTransformationMethod(passwordMethod);   //设置隐藏
            topbudgetTv.setTransformationMethod(passwordMethod);   //设置隐藏
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShow = false;   //设置标志位为隐藏状态
        }else{  //密文---》明文
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(hideMethod);   //设置隐藏
            topOutTv.setTransformationMethod(hideMethod);   //设置隐藏
            topbudgetTv.setTransformationMethod(hideMethod);   //设置隐藏
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow = true;   //设置标志位为隐藏状态
        }
    }


}