package com.example.fosi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private long backBtnTime=0;
    private TextView tv_name;
    private ImageButton btn_sos;
    private TextView tv_sos;
    private CardView cd_soslist,cd_pnnum,cd_map,cd_news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //메인화면 이름 띄우기
        tv_name=findViewById(R.id.tv_name);

        Intent intent=getIntent();
        String userName=intent.getStringExtra("userName");

        tv_name.setText(userName);

        //sos입력
        tv_sos=findViewById(R.id.tv_sos);
        btn_sos=(ImageButton)findViewById(R.id.btn_sos);
        btn_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sos=tv_sos.getText().toString();

                Response.Listener<String> responseListner=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success=jsonObject.getBoolean("success");
                            if(success){
                                Toast.makeText(getApplicationContext(),"성공적으로 접수되었습니다.",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"접수가 실패되었습니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                SOSRequest sosRequest=new SOSRequest(sos,responseListner);
                RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
                queue.add(sosRequest);
            }
        });

        cd_soslist=(CardView) findViewById(R.id.cd_soslist);
        cd_pnnum=(CardView)findViewById(R.id.cd_pnnum);
        cd_map=(CardView)findViewById(R.id.cd_map);
        cd_news=(CardView)findViewById(R.id.cd_news);

        cd_soslist.setOnClickListener(this);
        cd_pnnum.setOnClickListener(this);
        cd_map.setOnClickListener(this);
        cd_news.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {//뒤로가기 버튼 눌렀을 때
        long curTime=System.currentTimeMillis();
        long gapTime=curTime-backBtnTime;

        if(0<=gapTime && 2000>gapTime){//2초안에 한번 더 눌렀을 때
            super.onBackPressed();//뒤로가기 실행
        }else{
            backBtnTime=curTime;//처음 버튼
            Toast.makeText(this,"한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }

    }

    //카드뷰 클릭(화면 전환)
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.cd_soslist:
                intent=new Intent(this,SOSListActivity.class);
                startActivity(intent);
                break;
            case R.id.cd_pnnum:
                intent=new Intent(this,AddressBookActivity.class);
                startActivity(intent);
                break;
            case R.id.cd_map:
                intent=new Intent(this,MapActivity.class);
                startActivity(intent);
                break;
            case R.id.cd_news:
                intent=new Intent(this,NewsActivity.class);
                startActivity(intent);
                break;
        }

    }
}