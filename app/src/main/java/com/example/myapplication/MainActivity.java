 package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

 public class MainActivity extends AppCompatActivity {
      private XRecyclerView recy_list;
      private Context context;
      private int curr;
     private List<GuZhuBean.CainixihuanBean> feedBackBeanList = new ArrayList<>();
     private MyAdapter myAdapter;
     private final static int SUCCES = 0X123;
     private final static int ERROR = 0X120;
     private final static int LODAEMORE = 0X124;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        recy_list = findViewById(R.id.recy_list);
        //LinearLayoutManager是线性布局，setOrientation可以设置他的方向是横向还是纵向。
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //添加布局管理器
        recy_list.setLayoutManager(layoutManager);
       myAdapter = new MyAdapter(context,feedBackBeanList);
       recy_list.setAdapter(myAdapter);
       guzhuData();
        recy_list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            //下拉刷新
            public void onRefresh() {
                //当下拉刷新的时候，重新获取数据，所有curr要变回0，并且把集合list清空
                guzhuData();
                recy_list.refreshComplete();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        curr=1; //当前页码
//                        /**加载数据处理**/
//                        recy_list.refreshComplete();
//                    }
//                },2000);

            }
            @Override
            //上拉加载
            public void onLoadMore() {
                loadMore();
                recy_list.loadMoreComplete();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        curr++;//当前页码
//                        /**加载数据处理**/
//                        recy_list.loadMoreComplete();
//                    }
//                },2000);

            }
        });
    }
     /**
      * 雇主页面网络数据
      */
     private Map<String, Object> guzhuMap = new HashMap<>();
     private GuZhuBean guZhuBean = new GuZhuBean();

     private void guzhuData() {
         if (LhtTool.isLogin) {
             guzhuMap.put("userid", MyApplication.userInfo.getUserID() + "");
         } else {
             guzhuMap.put("userid", "0");
         }
         OkhttpTool.getOkhttpTool().post(UrlConfig.GUZHUPAGE, guzhuMap, new Callback() {
             @Override
             public void onFailure(Call call, IOException e) {

             }

             @Override
             public void onResponse(Call call, Response response) throws IOException {
                 String s = response.body().string();
                 Log.e("guzhupage", s + "");
                 guZhuBean = new Gson().fromJson(s, GuZhuBean.class);
                 if (guZhuBean.getErr_code().equals("0")) {
                     guzhuHd.sendEmptyMessage(SUCCES);
                 } else {
                     guzhuHd.sendEmptyMessage(ERROR);
                 }
             }
         });
     }
     private GuZhuMoreBean guZhuMoreBean = new GuZhuMoreBean();
     private List<GuZhuBean.CainixihuanBean> fuwuLoad;
     private void loadMore(){
         curr++;
         Map<String, Object> map = new HashMap<>();
         if (LhtTool.isLogin) {
             if (MyApplication.userInfo != null) {
                 map.put("userid", MyApplication.userInfo.getUserID() + "");
             }
         } else {
             map.put("userid", "0");
         }

         if (curr == 2) {
             map.put("pages", curr + "");
         } else if (curr > 2 && guZhuMoreBean.getPagerInfo().getNextPageIndex() != 0) {
             map.put("pages", curr + "");
         } else {
             Toast.makeText(context, "已经是最后一页了!", Toast.LENGTH_SHORT);
             recy_list.setNoMore(false);
             return;
         }
         map.put("num", "10");
         OkhttpTool.getOkhttpTool().post(UrlConfig.GUZHUMORE, map, new Callback() {
             @Override
             public void onFailure(Call call, IOException e) {
             }

             @Override
             public void onResponse(Call call, Response response) throws IOException {
                 String s = response.body().string();
                 guZhuMoreBean = new Gson().fromJson(s, GuZhuMoreBean.class);
                 fuwuLoad = new ArrayList<>();
                 if (guZhuMoreBean != null && guZhuMoreBean.getCainixihuan().size() > 0) {
                     for (int i = 0; i < guZhuMoreBean.getCainixihuan().size(); i++) {
                         GuZhuBean.CainixihuanBean listBean = new GuZhuBean.CainixihuanBean();
                         listBean.setImgurl(guZhuMoreBean.getCainixihuan().get(i).getImgurl());
                         listBean.setFuwu_name(guZhuMoreBean.getCainixihuan().get(i).getFuwu_name());
                         listBean.setPrice(guZhuMoreBean.getCainixihuan().get(i).getPrice());
                         listBean.setUnit(guZhuMoreBean.getCainixihuan().get(i).getUnit());
                         listBean.setCityname(guZhuMoreBean.getCainixihuan().get(i).getCityname());
                         listBean.setDeal_count(guZhuMoreBean.getCainixihuan().get(i).getDeal_count());
                         listBean.setFuwu_id(guZhuMoreBean.getCainixihuan().get(i).getFuwu_id());
                         listBean.setDianpu_name(guZhuMoreBean.getCainixihuan().get(i).getDianpu_name());
                         listBean.setPingfen(guZhuMoreBean.getCainixihuan().get(i).getPingfen());
                         fuwuLoad.add(listBean);
                     }
                     guzhuHd.sendEmptyMessage(LODAEMORE);
                 }
             }
         });
     }

     @SuppressLint("HandlerLeak")
     private Handler guzhuHd =new Handler(){
         @Override
         public void handleMessage(@NonNull Message msg) {
             super.handleMessage(msg);
             switch (msg.what){
                 case SUCCES:
                     if (myAdapter.getItemCount()> 0){
                         myAdapter.refresh();
                     }
                     myAdapter.addlist(guZhuBean.getCainixihuan());
                     myAdapter.notifyDataSetChanged();
                 case ERROR:

                     break;
                 case LODAEMORE:
                     myAdapter.addlist(fuwuLoad);
                     myAdapter.notifyDataSetChanged();

                     break;
             }
         }
     };
}
