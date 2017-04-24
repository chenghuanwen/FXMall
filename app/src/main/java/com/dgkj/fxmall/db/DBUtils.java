package com.dgkj.fxmall.db;

import android.content.Context;


/** 数据库处理工具
 * Created by Android004 on 2016/8/24.
 */
public class DBUtils {
    //利用DBHelper获取数据源Dao对象，通过Dao对象对数据库进行操作
    private DBHelper dbHelper;
  //  private Dao<ChatMsgBean,String> dao;

    public DBUtils(Context context){
        dbHelper = DBHelper.getInstance(context);
        // dao = dbHelper.getDao(ChatMsgBean.class);


    }

    /**
     * 添加数据
     * @param msg 消息对象
     */
  /*  public void add (ChatMsgBean msg){
        try {
            dao.create(msg);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/



    /**
     * 删除数据
     * @param msg 消息对象
     */
  /*  public void remove(ChatMsgBean msg){
        try {
            dao.delete(msg);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/




    /**
     * 查询历史聊天记录数据
     * @return
     */
   /* public List<ChatMsgBean> query(String friendname,String myname){
        List<ChatMsgBean> msgBeans = new ArrayList<>();
        try {
            QueryBuilder<ChatMsgBean, String> builder = dao.queryBuilder();
            builder.where().eq("senduser", friendname).and().eq("touser", myname);
            msgBeans.addAll(builder.query());
            if(!friendname.equals(myname)){
                QueryBuilder<ChatMsgBean, String> builder1 = dao.queryBuilder();
                builder1.where().eq("senduser",myname).and().eq("touser",friendname);
                msgBeans.addAll(builder1.query());
            }
            return msgBeans;
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException("查询数据库异常");
            Log.i("TAG", "查询数据库异常");
            return null;
        }
    }*/


}
