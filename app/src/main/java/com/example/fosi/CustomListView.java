package com.example.fosi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

public class CustomListView extends ArrayAdapter<String> {

    private String[] time;
    private Activity context;

    public CustomListView(Activity context, String[] time) {
        super(context, R.layout.layout,time);
        this.context=context;
        this.time=time;
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.layout,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder) r.getTag();
        }

        viewHolder.tvw.setText(time[position]);

        return r;
    }

    class ViewHolder{
        TextView tvw;

        ViewHolder(View v){
            tvw=(TextView)v.findViewById(R.id.tv_list);

        }
    }

}
