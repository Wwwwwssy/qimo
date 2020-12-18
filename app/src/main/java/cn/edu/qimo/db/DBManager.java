package cn.edu.qimo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

//增删改查
public class DBManager {
    private static SQLiteDatabase db;
    /* 初始化数据库对象*/
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);  //得到帮助类对象
        db = helper.getWritableDatabase();      //得到数据库对象
    }
//    读取数据，写入集合里（记账页面）
    public static List<TypeBean> getTypeList(int kind){
        List<TypeBean>list = new ArrayList<>();
        //读取typetb表当中的数据
        String sql = "select * from typetb where kind = "+kind;
        Cursor cursor = db.rawQuery(sql, null);
    //        循环读取游标内容，存储到对象当中
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind1);
            list.add(typeBean);
        }
        return list;
    }
    //向记账表（明细）中插入元素[outcomefragment中]
    public static void insertItemToComfirmtb(ConfirmBean bean){
        ContentValues values = new ContentValues();
        values.put("typename",bean.getTypename());
        values.put("sImageId",bean.getsImageId());
        values.put("beizhu",bean.getBeizhu());
        values.put("money",bean.getMoney());
        values.put("time",bean.getTime());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("kind",bean.getKind());
        db.insert("confirmtb",null,values);
        Log.i("confirmtb","insertItemToConfirmtb is ok");
    }

    //获取记账表中某一天的收支
    public static List<ConfirmBean>getListOneDayFromConfirmtb(int year,int month,int day){
        List<ConfirmBean>list = new ArrayList<>();
        //读取confirmtb中的数据
        String sql = "select * from confirmtb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int year1 = cursor.getInt(cursor.getColumnIndex("year"));
            int month1 = cursor.getInt(cursor.getColumnIndex("month"));
            int day1 = cursor.getInt(cursor.getColumnIndex("day"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            ConfirmBean confirmBean = new ConfirmBean(id, typename, sImageId, beizhu, money,time,year1,month1,day1, kind);
            list.add(confirmBean);
        }
        return list;
    }
    //获取记账表中某月的收支
    public static List<ConfirmBean>getListOneMonthFromConfirmtb(int year,int month){
        List<ConfirmBean>list = new ArrayList<>();
        //读取confirmtb中的数据
        String sql = "select * from confirmtb where year=? and month=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int year1 = cursor.getInt(cursor.getColumnIndex("year"));
            int month1 = cursor.getInt(cursor.getColumnIndex("month"));
            int day1 = cursor.getInt(cursor.getColumnIndex("day"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            ConfirmBean confirmBean = new ConfirmBean(id, typename, sImageId, beizhu, money,time,year1,month1,day1, kind);
            list.add(confirmBean);
        }
        return list;
    }

    //获取某一天的支出或者收入的总金额   kind：支出==0    收入===1
    public static float getSumMoneyOneDay(int year,int month,int day,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from confirmtb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }
    //获取某一月的支出或者收入的总金额   kind：支出==0    收入===1
    public static float getSumMoneyOneMonth(int year,int month,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from confirmtb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }
    //获取某一年的支出或者收入的总金额   kind：支出==0    收入===1
    public static float getSumMoneyOneYear(int year,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from confirmtb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    //根据传入的id，删除confirmtb表当中的一条数据
    public static int deleteItemFromConfirmtbById(int id){
        int i = db.delete("confirmtb", "id=?", new String[]{id + ""});
        return i;
    }
    // 删除Confirmtb表格当中的所有数据
    public static void deleteAllConfirmtb(){
        String sql = "delete from confirmtb";
        db.execSQL(sql);
    }

    //根据备注搜索收入或者支出的情况列表
    public static List<ConfirmBean>getListFromConfirmtbBybeizhu(String beizhu){
        List<ConfirmBean>list = new ArrayList<>();
        String sql = "select * from confirmtb where beizhu like '%"+beizhu+"%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String bz = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            ConfirmBean confirmBean = new ConfirmBean(id, typename, sImageId, bz, money, time, year, month, day, kind);
            list.add(confirmBean);
        }
        return list;
    }
    //根据时间搜索收入或者支出的情况列表
    public static List<ConfirmBean>getListFromConfirmtbBytime(String time){
        List<ConfirmBean>list = new ArrayList<>();
        String sql = "select * from confirmtb where time like '%"+time+"%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String bz = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time1 = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            ConfirmBean confirmBean = new ConfirmBean(id, typename, sImageId, bz, money, time1, year, month, day, kind);
            list.add(confirmBean);
        }
        return list;
    }
    //根据类型搜索收入或者支出的情况列表
    public static List<ConfirmBean>getListFromConfirmtbBytypename(String typename){
        List<ConfirmBean>list = new ArrayList<>();
        String sql = "select * from confirmtb where typename like '%"+typename+"%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename1 = cursor.getString(cursor.getColumnIndex("typename"));
            String bz = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time1 = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            ConfirmBean confirmBean = new ConfirmBean(id, typename1, sImageId, bz, money, time1, year, month, day, kind);
            list.add(confirmBean);
        }
        return list;
    }


}
