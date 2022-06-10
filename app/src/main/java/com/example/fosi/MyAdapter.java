package com.example.fosi;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView.Adapter를 상속받을 때, Adapter에 대한 ViewHolder를
 * 반드시 구현해야 하고 이를 명시해주어야 함.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<NewsData> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_title;
        public TextView tv_content;
        public SimpleDraweeView iv_news;
        public View rootView;

        public MyViewHolder(View v){
            super(v);
            tv_title=v.findViewById(R.id.tv_title);
            tv_content=v.findViewById(R.id.tv_content);
            iv_news=v.findViewById(R.id.iv_news);
        }
    }
    public MyAdapter(List<NewsData> myDataset,Context context){
        mDataset=myDataset;
        Fresco.initialize(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LinearLayout v=(LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_news,parent,false);
        MyViewHolder vh=new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NewsData news=mDataset.get(position);

        holder.tv_title.setText(news.getTitle());
        holder.tv_content.setText(news.getDescription());

        Uri uri=Uri.parse(news.getUrlToImage());//Fresco
        holder.iv_news.setImageURI(uri);

    }

    @Override
    public int getItemCount() {
        return mDataset==null?0:mDataset.size();
    }

}
