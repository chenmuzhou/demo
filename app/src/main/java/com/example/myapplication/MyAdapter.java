package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private  List<GuZhuBean.CainixihuanBean> feedBackBeanList = new ArrayList<>();
    private View view;

    public MyAdapter(Context context, List feedBackBeanList) {
        this.context = context;
        this.feedBackBeanList = feedBackBeanList;
    }
    public void addlist(List<GuZhuBean.CainixihuanBean> list) {

        feedBackBeanList.addAll(list);

    }

    public void refresh() {
        feedBackBeanList.clear();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.listview_fwzs,null,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.image_loading)
                .error(R.mipmap.image_erroe)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).load(feedBackBeanList.get(position).getImgurl()).apply(options).into(holder.iv_touxiang);
        Glide.with(context).load(feedBackBeanList.get(position).getImgurl()).apply(options).into(holder.head_img);
        holder.tv_title.setText(feedBackBeanList.get(position).getFuwu_name());

    }

    @Override
    public int getItemCount() {
        return feedBackBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_touxiang;
        private TextView tv_title;
        private TextView tv_money;
        private TextView tv_city;
        private TextView tv_count; //成交量
        private TextView tv_pingfen,gongsiName;
        private CircleImageView head_img;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_touxiang = itemView.findViewById(R.id.rmtjfw_img);
            tv_title = itemView.findViewById(R.id.guzhu_fuwu_title);
            head_img  = itemView.findViewById(R.id.guzhu_fuwu_head_img);
        }
    }
}
