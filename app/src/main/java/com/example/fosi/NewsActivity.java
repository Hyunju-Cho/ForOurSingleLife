package com.example.fosi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        mRecyclerView=findViewById(R.id.rv_news);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        queue= Volley.newRequestQueue(this);
        getNews();
        //1. 화면 로딩 -> 뉴스 정보 받아옴
        //2. 정보 -> 어댑터 넘겨줌
        //3. 어댑터 -> 세팅

    }


    public void getNews(){

        String url="https://newsapi.org/v2/top-headlines?country=kr&apiKey=5c72fa63ec0644c4a88a0bedef324088";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("NEWS",response);

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray arrayArticles=jsonObject.getJSONArray("articles");

                            //response->NewsData Class 분류
                            List<NewsData> news=new ArrayList<>();
                            for(int i=0,j=arrayArticles.length();i<j;i++){
                                JSONObject obj=arrayArticles.getJSONObject(i);

                                Log.d("NEWS",obj.toString());

                                NewsData newsData=new NewsData();
                                newsData.setTitle(obj.getString("title"));
                                newsData.setDescription(obj.getString("description"));
                                newsData.setUrlToImage(obj.getString("urlToImage"));
                                news.add(newsData);
                            }

                            mAdapter=new MyAdapter(news,NewsActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("User-Agent", "Mozilla/5.0");

                return headers;
            }
        };


        queue.add(stringRequest);
    }


}