package cn.edu.qimo.jizhangfg;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import androidx.fragment.app.Fragment;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.qimo.Beizhu;
import cn.edu.qimo.KeyBoardUtils;
import cn.edu.qimo.R;
import cn.edu.qimo.db.ConfirmBean;
import cn.edu.qimo.db.DBManager;
import cn.edu.qimo.db.TypeBean;


public class OutcomeFragment extends Fragment implements View.OnClickListener{
    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv,beizhuTv,timeTv;
    GridView typeGv;
    List<TypeBean>typeList;
    TypeBaseAdapter adapter;
    ConfirmBean confirmBean;//将需要插入到数据库的数据保存成对象的形式

    private void  initView(View view){
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        beizhuTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);
        //让自定义软键盘显示出来
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        boardUtils.showKeyboard();
        //设置接口，监听确定按钮按钮被点击了
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //获取输入金额
                String moneyStr = moneyEt.getText().toString();
                if (TextUtils.isEmpty(moneyStr)||moneyStr.equals("0")) {//当前金额为空或者金额为0，直接关闭活动
                    getActivity().finish();
                    return;
                }
                float money = Float.parseFloat(moneyStr);
                confirmBean.setMoney(money);//设置金额保存
                //获取记录的信息，保存在数据库当中
                saveConfirmToDB();
                // 返回上一级页面
                getActivity().finish();
            }
        });
    }
    public static OutcomeFragment newInstance(String param1, String param2) {
        OutcomeFragment fragment = new OutcomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        confirmBean = new ConfirmBean();//创建对象
        confirmBean.setTypename("其他");
        confirmBean.setsImageId(R.drawable.qita_hs);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        //给GridView填充数据的方法
        loadDataToGV();
        //设置GridView每一项的点击事件
        setGVListener();
        //显示时间
        time();
        return view;
    }
    ////获取系统时间
    private  void time(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = sdf.format(date);
        timeTv.setText(time);
        confirmBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        confirmBean.setYear(year);
        confirmBean.setMonth(month);
        confirmBean.setDay(day);
    }

//设置GridView每一项的点击事件
    private void setGVListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetInvalidated();  //点击颜色发生变化
                //点击后显示对应的图片和文字
                TypeBean typeBean = typeList.get(position);//获取 图片位置
                String typename = typeBean.getTypename();//获取图片的名字
                typeTv.setText(typename);//修改图片名字
                confirmBean.setTypename(typename);
                int simageId = typeBean.getSimageId();
                typeIv.setImageResource(simageId);//修改图片
                confirmBean.setsImageId(simageId);
            }
        });
    }
//给GridView填充数据的方法
    private void loadDataToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);
        //获取数据库中的数据源
        List<TypeBean> outlist = DBManager.getTypeList(0);//支出和收入就是这里不一样
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
    }

    public void saveConfirmToDB() {
        confirmBean.setKind(0);
        DBManager.insertItemToComfirmtb(confirmBean);//该方法插入写在DBManager中
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_record_tv_beizhu:
                showBZDialog();
                break;
        }
    }


    /* 弹出备注对话框*/
    public  void showBZDialog(){
        final Beizhu beizhu = new Beizhu(getContext());
        beizhu.show();
        beizhu.setOnEnsureListener(new Beizhu.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = beizhu.getEditText();
                if (!TextUtils.isEmpty(msg)) {
                    beizhuTv.setText(msg);
                    confirmBean.setBeizhu(msg);
                }
                beizhu.cancel();
            }
        });
    }

}