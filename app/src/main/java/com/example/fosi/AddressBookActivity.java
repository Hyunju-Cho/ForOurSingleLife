package com.example.fosi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.people.PeopleManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookActivity extends AppCompatActivity {

    ListView listView=null;
    MyAdapter adapter;
    public static ArrayList<PhoneNumber> phoneNumberArrayList=new ArrayList<>();
    String url="http://192.168.78.126/RetrieveAddress.php";
    PhoneNumber phoneNumber;
    Button bt_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);

        listView=findViewById(R.id.lv_nums);
        bt_add=findViewById(R.id.bt_add);


        adapter=new MyAdapter(this,phoneNumberArrayList);
        retrieveData();

        listView.setAdapter(adapter);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent=new Intent(getApplicationContext(),AddressBookModifyActivity.class);
                startActivity(intent);
            }
        });

        //리스트뷰 목록 짧게 클릭
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                //전화걸기
                String item=phoneNumberArrayList.get(position).getContact();
                Toast.makeText(AddressBookActivity.this,item,Toast.LENGTH_SHORT).show();
                String service="tel:" +item;
                Uri uri=Uri.parse(service);
                Intent dialIntent=new Intent(Intent.ACTION_DIAL,uri);
                startActivity(dialIntent);
            }
        });
        //리스트뷰 목록 길게 클릭
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteData(phoneNumberArrayList.get(position).getContact());
                phoneNumberArrayList.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    //데이터 지우기
    private void deleteData(final String num) {

        StringRequest request=new StringRequest(Request.Method.POST, "http://192.168.78.126/DeleteAddress.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("Data Deleted")){
                    Toast.makeText(AddressBookActivity.this,"성공적으로 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddressBookActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String >params=new HashMap<String,String>();

                params.put("num",num);

                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    //데이터 가져오기
    private void retrieveData() {
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                phoneNumberArrayList.clear();
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    if(success.equals("1")){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            String name=object.getString("name");
                            String contact=object.getString("contact");

                            phoneNumber=new PhoneNumber(name,contact);
                            phoneNumberArrayList.add(phoneNumber);
                            adapter.notifyDataSetChanged();
                        }
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddressBookActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    //mysql에 있는 데이터 띄우기
    public class MyAdapter extends ArrayAdapter<PhoneNumber>{

        Context context;
        List<PhoneNumber> arrayListPhoneNumber;

        public MyAdapter(@NonNull Context context, List<PhoneNumber> arrayListPhoneNumber) {
            super(context, R.layout.listview_number,arrayListPhoneNumber);

            this.context=context;
            this.arrayListPhoneNumber=arrayListPhoneNumber;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

           // view 재활용하는 구조로 감
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_number,null,true);
            TextView tvname=view.findViewById(R.id.tv_names);
            TextView tvnum=view.findViewById(R.id.tv_num);

            tvname.setText(arrayListPhoneNumber.get(position).getName());
            tvnum.setText(arrayListPhoneNumber.get(position).getContact());

            return view;
        }
    }


}

