package com.example.fosi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressBookModifyActivity extends AppCompatActivity {

    ListView mListView=null;
    CustomAdapter mAdapter=null;
    ArrayList<People> mData=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book_modify);

        mListView=(ListView) findViewById(R.id.lv_newnums);

        mData=new ArrayList<People>();

        mAdapter=new CustomAdapter(this,mData);
        mListView.setAdapter(mAdapter);

    }

    public class People{
        String mName;//이름
        String mNumber;//전화번호
    }

    //전화번호 데이터를 BaseAdapter에 전달하고 이 어댑터는 전달 받은 데이터를 이용해서 리스트뷰에게 전달할 뷰 생성(사용자정의)
    public class CustomAdapter extends BaseAdapter {
        Context mcontext = null;
        ArrayList<People> mData=null;
        LayoutInflater mLayoutInflater = null;

        public CustomAdapter(Context context,ArrayList<People> data) {//context 넘김

            mcontext=context;
            mData=data;
            mLayoutInflater=LayoutInflater.from(mcontext);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder{
            TextView mNameTv;
            TextView mNumberTv;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {//리스트뷰의 아이템 하나에 들어갈 뷰를 전달하는 함수
            View itemLayout=convertView;//재활용뷰
            ViewHolder viewHolder=null;

            //리스트뷰를 빠르게 위아래로 플리킹하면 많은 뷰들이 순간적으로 생성하여 메모리 부족으로 이어짐->자식뷰 재사용
            // 1. 어댑터뷰가 재사용할 뷰를 넘겨주지 않은 경우에만 새로운 뷰 생성
            if(itemLayout==null){
                itemLayout=mLayoutInflater.inflate(R.layout.listview_number,null);//인플레이션은 한번만

                //textview률 findviewbyid()계속 사용하기보다는 findviewbyid()로 얻은 뷰를 뷰홀더에 세팅하여 convertview에 tag 저장
                //view holder 생성 자식뷰 찾아 view holder에 참조
                //생성된 view holder는 아이템에 설정, 다음에 아이템 재사용시 참조
                viewHolder=new ViewHolder();

                viewHolder.mNameTv=(TextView) itemLayout.findViewById(R.id.tv_names);
                viewHolder.mNumberTv=(TextView) itemLayout.findViewById(R.id.tv_num);

                itemLayout.setTag(viewHolder);
            }else{
                //재사용 아이템에는 이전에 view holder 객체를 설정
                // 설정된 view holder 객체를 이용해서 findviewbyid 함수를 사용하지 않고 원하는 뷰 참조 가능
                viewHolder=(ViewHolder) itemLayout.getTag();
            }
            // 2. 이름, 전화번호 데이터를 참조하여 레이아웃 갱신
            viewHolder.mNameTv.setText(mData.get(position).mName);//변수에 데이터 넣음
            viewHolder.mNumberTv.setText(mData.get(position).mNumber);
            return itemLayout;//그 레이아웃 보내줌, 뷰홀더랑 아이템 레이아웃이 가리키는 곳이 동일함
        }
        public void add(int index, People addData){
            mData.add(index,addData);
            notifyDataSetChanged();//notify 공지, 공지 후 리스트뷰에게 넘겨줌
        }
        public void delete(int index){
            mData.remove(index);
            notifyDataSetChanged();
        }
        public void clear(){
            mData.clear();
            notifyDataSetChanged();
        }
    }
    public void onClick(View v){
        switch (v.getId()){//아이디 가지고옴
            case R.id.btn_add: //추가
            {
                EditText etname = (EditText) findViewById(R.id.et_names);
                EditText etnum = (EditText) findViewById(R.id.et_pnnum);

                People addData = new People();

                addData.mName = etname.getText().toString();
                addData.mNumber = etnum.getText().toString();

                mAdapter.add(0, addData);//첫 번째 인덱스에 People을 넘겨줌
                insertData();

                break;
            }

        }

    }
    private void insertData() {
        EditText etname = (EditText) findViewById(R.id.et_names);
        EditText etnum = (EditText) findViewById(R.id.et_pnnum);

        final String name=etname.getText().toString().trim();
        String num=etnum.getText().toString().trim();

        if(name.isEmpty()){
            Toast.makeText(this,"이름을 입력하세요",Toast.LENGTH_SHORT).show();
            return;
        }else if(num.isEmpty()){
            Toast.makeText(this,"전화번호를 입력하세요",Toast.LENGTH_SHORT).show();
        }else{
            StringRequest request=new StringRequest(Request.Method.POST, "http://IP주소/AddressWrite.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equalsIgnoreCase("Data Inserted")){
                        Toast.makeText(AddressBookModifyActivity.this,"성공적으로 전화번호가 등록되었습니다.",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(AddressBookModifyActivity.this,"전화번호 등록에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddressBookModifyActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String >params=new HashMap<String,String>();

                    params.put("name",name);
                    params.put("num",num);

                    return params;
                }
            };
            RequestQueue requestQueue=Volley.newRequestQueue(AddressBookModifyActivity.this);
            requestQueue.add(request);
        }

    }

}