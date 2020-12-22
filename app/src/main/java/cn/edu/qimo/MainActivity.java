package cn.edu.qimo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.edu.qimo.db.ConfirmBean;
import cn.edu.qimo.db.DBManager;
import cn.edu.qimo.jizhangfg.JizhangActivity;
import cn.edu.qimo.my.MyActivity;
import cn.edu.qimo.search.SearchActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "JizhangActivity";
    ListView todayLv;  //展示今日收支情况的ListView
    ImageView searchIv;//搜索
    TextView timeTv1;
    ImageButton editBtn,moreBtn;//个人中心
    List<ConfirmBean> mDatas;//声明数据源
    MainJizhangAdapter adapter;//声明适配器
    int year,month,day;
    //头布局相关控件
    View headerView;
    TextView topOutTv,topInTv,topbudgetTv,topConTv;
    ImageView topShowIv;
    //预算
    SharedPreferences preferences;
    boolean isShow = true;//默认可以显示金额
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Time();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);//初始化preferences，设置为私有化
        mDatas = new ArrayList<>();
        //设置适配器，加载每一行数据到ListView
        adapter = new MainJizhangAdapter(this,mDatas);
        todayLv.setAdapter(adapter);
        //添加listview的头布局
        addLVHeaderView();
        //长按删除
        setLVLongClickListener();

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

        //设置显示预算剩余
        float bmoney = preferences.getFloat("bmoney", 0);//预算
        if (bmoney == 0) {
            topbudgetTv.setText("￥ 0");
        }else{
            float symoney = bmoney-outcomeOneMonth;
            topbudgetTv.setText("￥"+symoney);
        }
    }

    // 加载数据库数据
    private void loadDBData() {
        List<ConfirmBean> list = DBManager.getListOneDayFromConfirmtb(year, month, day);
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
        timeTv1 = findViewById(R.id.item_mainlv_top_tv_riqi);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
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
            case R.id.main_btn_my:
                Log.d(TAG,"个人中心");
                Intent intent3 = new Intent(this, MyActivity.class);  //个人主页
                startActivity(intent3);
                break;
            case R.id.main_iv_search:
                Intent intent4 = new Intent(this, SearchActivity.class);  //搜索界面
                startActivity(intent4);
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudget();//设置预算
                break;
            case R.id.item_mainlv_top_iv_hide:
                toggleShow();//隐藏或者显示
                break;


//            if (v == headerView) {
//                //头布局被点击了
//                Intent intent5 = new Intent();
//                intent.setClass(this, MonthChartActivity.class);//图表页面
//                startActivity(intent5);
//            }
        }
    }
    //显示设置预算对话框
    private void showBudget() {
        Budget budget = new Budget(this);
        budget.show();
        budget.setOnEnsureListener(new Budget.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //将预算金额写入到共享参数当中，进行存储
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney",money);
                editor.commit();
                //计算剩余金额
                float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float symoney = money-outcomeOneMonth;//预算剩余
                topbudgetTv.setText("￥"+symoney);
            }
        });
    }

    //隐藏或者显示金额
    private void toggleShow() {
        if (isShow) {   //明文====》密文
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();//得到密文对象
            topInTv.setTransformationMethod(passwordMethod);//收入设置隐藏
            topOutTv.setTransformationMethod(passwordMethod);//支出设置隐藏
            topbudgetTv.setTransformationMethod(passwordMethod);//预算设置隐藏
            topShowIv.setImageResource(R.mipmap.ih_hide);//修改图标样式
            isShow = false;   //设置标志位为隐藏状态
        }else{  //密文---》明文
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();//隐藏返回
            topInTv.setTransformationMethod(hideMethod);
            topOutTv.setTransformationMethod(hideMethod);
            topbudgetTv.setTransformationMethod(hideMethod);
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow = true;   //设置标志位为隐藏状态
        }
    }

    //显示删除页面对话框
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {  //点击了头布局
                    return false;
                }
                int pos = position-1;
                ConfirmBean clickBean = mDatas.get(pos);  //获取正在被点击的这条信息

                //弹出提示用户是否删除的对话框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }
    //删除界面
    private void showDeleteItemDialog(final  ConfirmBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("是否删除这条记录？")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        //执行删除的操作
                        DBManager.deleteItemFromConfirmtbById(click_id);
                        mDatas.remove(clickBean);   //实时刷新，移除集合当中的对象
                        adapter.notifyDataSetChanged();   //提示适配器更新数据
                        setTopTvShow();   //改变头布局TextView显示的内容
                    }
                });
        builder.create().show();   //显示对话框
    }


}