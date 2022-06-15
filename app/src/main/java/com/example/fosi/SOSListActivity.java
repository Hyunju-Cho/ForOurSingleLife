package com.example.fosi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

public class SOSListActivity extends AppCompatActivity {

    String urladdress="http://IP주소/soslist.php";
    String[] time;
    ListView listView;
    BufferedInputStream is;
    String line=null;
    String result=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soslist);

        listView=(ListView) findViewById(R.id.lview);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData();
        CustomListView customListView=new CustomListView(this,time);
        listView.setAdapter(customListView);

    }
    private void collectData(){
       //Connection
        try {
            URL url=new URL(urladdress);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");//GET방식으로 내용을 가져옴
            is=new BufferedInputStream(con.getInputStream());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        //content
        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder();
            while((line=br.readLine())!=null){
                sb.append(line+"\n");
            }
            is.close();
            result = sb.toString();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        //JSON
        try{
            JSONArray ja=new JSONArray(result);//매개변수로 넘어온 result라는 문자열을 JSONArray타입으로 변환
            JSONObject jo=null;
            time=new String[ja.length()];

            for(int i=0;i<=ja.length();i++){//JSONArray의 값을 읽어와 2차원 String 배열에 저장
                jo=ja.getJSONObject(i);
                time[i]=jo.getString("time");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }




}