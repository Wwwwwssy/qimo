package cn.edu.qimo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import cn.edu.qimo.R;

public class DBOpenHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    public DBOpenHelper(@Nullable Context context) {
        super(context, "jizhang.db", null, 1);
    }
//创建数据库的方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        //表示类型的表 TyprBean 中的类型
        String sql = "create table typetb(id integer primary key autoincrement,typename varchar(10),imageId integer,sImageId integer,kind integer)";
        db.execSQL(sql);
        //插入
        insertType(db);
        //创建记账页面的表 ConfirmBean 中的类型
        sql = "create table confirmtb(id integer primary key autoincrement,typename varchar(10),sImageId integer,beizhu varchar(80),money float," + "time varchar(60),year integer,month integer,day integer,kind integer)";
        db.execSQL(sql);



    }





    private void insertType(SQLiteDatabase db) {
        String sql = "insert into typetb (typename,imageId,sImageId,kind) values (?,?,?,?)";
        db.execSQL(sql,new Object[]{"书籍", R.drawable.ic_book,R.drawable.ic_book_hs,0});
        db.execSQL(sql,new Object[]{"交通", R.drawable.ic_car,R.drawable.ic_car_hs,0});
        db.execSQL(sql,new Object[]{"餐饮", R.drawable.ic_food,R.drawable.ic_food_hs,0});
        db.execSQL(sql,new Object[]{"游戏", R.drawable.ic_game,R.drawable.ic_game_hs,0});
        db.execSQL(sql,new Object[]{"购物", R.drawable.ic_shopping,R.drawable.ic_shopping_hs,0});
        db.execSQL(sql,new Object[]{"学习", R.drawable.ic_study,R.drawable.ic_study_hs,0});
        db.execSQL(sql,new Object[]{"医疗", R.drawable.ic_yiyuan,R.drawable.ic_yiyuan_hs,0});
        db.execSQL(sql,new Object[]{"其他", R.drawable.qita,R.drawable.qita_hs,0});


        db.execSQL(sql,new Object[]{"工资", R.drawable.in_gongzi,R.drawable.in_gongzi_hs,1});
        db.execSQL(sql,new Object[]{"兼职", R.drawable.in_jianzhi,R.drawable.in_jianzhi_hs,1});
        db.execSQL(sql,new Object[]{"理财", R.drawable.in_licai,R.drawable.in_licai_hs,1});
        db.execSQL(sql,new Object[]{"其他", R.drawable.qita,R.drawable.qita_hs,1});


    }

    //更新时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
